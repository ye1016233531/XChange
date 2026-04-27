package info.bitrich.xchangestream.gateio;

import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.gateio.config.FuturesConfig;
import info.bitrich.xchangestream.gateio.dto.response.ticker.GateioMultipleKlinesNotification;
import info.bitrich.xchangestream.gateio.dto.response.ticker.GateioSingleFuturesKlinesNotification;
import io.reactivex.rxjava3.core.Observable;
import org.apache.commons.lang3.ArrayUtils;
import org.knowm.xchange.dto.marketdata.CandleStick;
import org.knowm.xchange.instrument.Instrument;

import java.time.Duration;
import java.time.Instant;

public class GateioStreamingFuturesMarketDataService implements StreamingMarketDataService {

  private final GateioStreamingFuturesService service;

  public GateioStreamingFuturesMarketDataService(GateioStreamingFuturesService service) {
    this.service = service;
  }

  @Override
  public Observable<CandleStick> getKlines(Instrument instrument, boolean isClose, Object ...args) {
    Duration interval = (Duration) ArrayUtils.get(args, 0);

    return service
            .subscribeChannel(FuturesConfig.FUTURES_KLINES_CHANNEL, instrument, interval )
            .map(GateioSingleFuturesKlinesNotification.class::cast)
            .filter(notification -> isClose && notification.getResult().getIsClose())
            .map(GateioStreamingAdapters::toCandleStick);
  }
}
