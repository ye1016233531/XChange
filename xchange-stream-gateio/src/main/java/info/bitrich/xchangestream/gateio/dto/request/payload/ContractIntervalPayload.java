package info.bitrich.xchangestream.gateio.dto.request.payload;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import info.bitrich.xchangestream.gateio.config.converter.DurationToIntervalStringConverter;
import java.time.Duration;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gateio.config.converter.CurrencyPairToStringConverter;

@Data
@SuperBuilder
@Jacksonized
@JsonPropertyOrder({"interval", "contract"})
public class ContractIntervalPayload {

  @JsonSerialize(converter = DurationToIntervalStringConverter.class)
  private Duration interval;

  /**
   * 合约名称，如 BTC_USDT
   */
  @JsonSerialize(converter = CurrencyPairToStringConverter.class)
  private CurrencyPair contract;
}
