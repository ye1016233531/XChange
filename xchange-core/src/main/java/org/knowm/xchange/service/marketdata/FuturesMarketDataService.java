package org.knowm.xchange.service.marketdata;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.CandleStickData;
import org.knowm.xchange.dto.meta.ExchangeHealth;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.trade.params.CandleStickDataParams;

import java.io.IOException;

/**
 * 合约市场数据服务
 * @author leafsource
 */
public interface FuturesMarketDataService {
    /**
     * Get exchange health
     *
     * @return The exchange health
     */
    default ExchangeHealth getExchangeHealth() {
        return ExchangeHealth.ONLINE;
    }


    /**
     * Get the CandleStickData for given currency between startDate to endDate.
     *
     * @param currencyPair currencyPair.
     * @param params Params for query, including start(e.g. march 2022.) and end date, period etc.,
     * @return The CandleStickData, null if some sort of error occurred. Implementers should log the
     *     error.
     * @throws ExchangeException - Indication that the exchange reported some kind of error with the
     *     request or response
     * @throws NotAvailableFromExchangeException - Indication that the exchange does not support the
     *     requested function or data
     * @throws NotYetImplementedForExchangeException - Indication that the exchange supports the
     *     requested function or data, but it has not yet been implemented
     * @throws IOException - Indication that a networking error occurred while fetching JSON data
     */
    default CandleStickData getCandleStickData(
            CurrencyPair currencyPair, CandleStickDataParams params) throws IOException {
        throw new NotYetImplementedForExchangeException("getCandleStickData");
    }

}
