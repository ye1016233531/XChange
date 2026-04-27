package info.bitrich.xchangestream.gateio.dto.response.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.gateio.dto.response.GateioWsNotification;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@Jacksonized
public class GateioMultipleKlinesNotification extends GateioWsNotification {

    @JsonProperty("result")
    private List<KlinePayload> result;

    public List<GateioSingleFuturesKlinesNotification> toSingleNotifications() {
        return result.stream()
                .map(
                        klinePayload ->
                                GateioSingleFuturesKlinesNotification.builder()
                                        .result(klinePayload)
                                        .time(getTime())
                                        .timeMs(getTimeMs())
                                        .channel(getChannel())
                                        .event(getEvent())
                                        .error(getError())
                                        .build())
                .collect(Collectors.toList());
    }
}
