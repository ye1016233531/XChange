package org.knowm.xchange.gateio.service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.meta.ExchangeHealth;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.gateio.GateioAdapters;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.gateio.dto.marketdata.GateioContractCandleStick;
import org.knowm.xchange.service.marketdata.FuturesMarketDataService;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParam;
import org.knowm.xchange.service.trade.params.DefaultCandleStickParamWithLimit;

public class GateioFuturesMarketDataService extends GateioBaseService implements FuturesMarketDataService {

  private static final int MAX_LIMIT = 1000;
  private static final int MAX_POINTS = 100000;

  private final String settle = "usdt";

  public GateioFuturesMarketDataService(GateioExchange exchange) {
    super(exchange);
  }

  @Override
  public ExchangeHealth getExchangeHealth() {
    return FuturesMarketDataService.super.getExchangeHealth();
  }

  @Override
  public CandleStickData getCandleStickData(CurrencyPair currencyPair, CandleStickDataParams params)
      throws IOException {

    if (!(params instanceof DefaultCandleStickParam)) {
      throw new NotYetImplementedForExchangeException("Only DefaultCandleStickParam is supported");
    }

    DefaultCandleStickParam candleStickParam = (DefaultCandleStickParam) params;
    String interval = toInterval(candleStickParam.getPeriodInSecs());
    String contract = GateioAdapters.toString(currencyPair);
    long periodSecs = candleStickParam.getPeriodInSecs();

    if (params instanceof DefaultCandleStickParamWithLimit) {
      int limit = ((DefaultCandleStickParamWithLimit) params).getLimit();
      List<GateioContractCandleStick> raw =
          gateio.getFuturesCandlesticks(settle, contract, null, null, limit, interval);
      return GateioAdapters.toCandleStickData(raw, currencyPair);
    }

    long from = candleStickParam.getStartDate().getTime() / 1000;
    long to = candleStickParam.getEndDate().getTime() / 1000;

    // Gate.io 限制最多查询最近 10000 个点，修正 from 不能早于此限制
    long earliestAllowed = to - periodSecs * MAX_POINTS;
    if (from < earliestAllowed) {
      from = earliestAllowed;
    }

    List<GateioContractCandleStick> all = new ArrayList<>();
    long currentFrom = from;

    while (currentFrom < to) {
      long chunkTo = Math.min(currentFrom + periodSecs * MAX_LIMIT, to);
      System.out.println("Fetching candlesticks from " + currentFrom + " to " + chunkTo);
      List<GateioContractCandleStick> chunk =
          gateio.getFuturesCandlesticks(settle, contract, currentFrom, chunkTo, null, interval);
      if (chunk == null || chunk.isEmpty()) {
        break;
      }
      all.addAll(chunk);
      // 使用最后一条数据的时间 + interval 作为下一段的起始，避免重复
      long lastTs = chunk.get(chunk.size() - 1).getTimestamp().longValue();
      currentFrom = lastTs + periodSecs;
      // 如果返回的数据不足一批，说明已经到末尾了
      if (chunk.size() < MAX_LIMIT) {
        break;
      }
    }

    // 过滤最后一条未闭合的K线（结束时间 >= 当前时间）
    long now = (Instant.now().toEpochMilli() / Instant.ofEpochSecond(periodSecs).toEpochMilli()) * Instant.ofEpochSecond(periodSecs).toEpochMilli();
    List<GateioContractCandleStick> closed = all.stream()
        .filter(candle -> {
          Instant endTime = Instant.ofEpochSecond(candle.getTimestamp().longValue());
            return endTime.toEpochMilli() < now;
        })
        .collect(Collectors.toList());

    return GateioAdapters.toCandleStickData(closed, currencyPair);
  }

  private String toInterval(long periodInSecs) {
    if (periodInSecs < 60) {
      return periodInSecs + "s";
    } else if (periodInSecs < 3600) {
      return (periodInSecs / 60) + "m";
    } else if (periodInSecs < 86400) {
      return (periodInSecs / 3600) + "h";
    } else {
      return (periodInSecs / 86400) + "d";
    }
  }
}
