package org.knowm.xchange.gateio.dto.marketdata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class GateioContractCandleStick {

  @JsonProperty("t")
  Double timestamp;

  @JsonProperty("o")
  String open;

  @JsonProperty("h")
  String high;

  @JsonProperty("l")
  String low;

  @JsonProperty("c")
  String close;

  @JsonProperty("v")
  String volume;

  @JsonProperty("sum")
  String sum;
}
