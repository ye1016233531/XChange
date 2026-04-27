package info.bitrich.xchangestream.gateio.dto.response.ticker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;

@Data
public class KlinePayload {
    @JsonProperty("n")
    private String n;

    @JsonIgnore
    public String getInterval() {
        if (n == null) return null;
        int idx = n.indexOf('_');
        return idx > 0 ? n.substring(0, idx) : null;
    }

    @JsonIgnore
    public CurrencyPair getCurrencyPair() {
        if (n == null) return null;
        int idx = n.indexOf('_');
        if (idx > 0) {
            String pair = n.substring(idx + 1);
            return new CurrencyPair(pair.replace('_', '/'));
        }
        return null;
    }

    @JsonProperty("t")
    private Long timestamp;
    @JsonProperty("o")
    private BigDecimal open;
    @JsonProperty("c")
    private BigDecimal close;
    @JsonProperty("h")
    private BigDecimal high;
    @JsonProperty("l")
    private BigDecimal low;
    @JsonProperty("v")
    private BigDecimal volume;
    @JsonProperty("a")
    private BigDecimal amount;
    @JsonProperty("w")
    private Boolean isClose;

}
