package info.bitrich.xchangestream.gateio.config.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.time.Duration;

/**
 * Converts {@code Duration} to Gate.io kline interval string.
 * <p>
 * Supported values: "10s", "1m", "5m", "15m", "30m", "1h", "4h", "8h", "1d", "7d"
 */
public class DurationToIntervalStringConverter extends StdConverter<Duration, String> {

  @Override
  public String convert(Duration value) {
    long totalSeconds = value.getSeconds();

    if (totalSeconds % 604800 == 0) {
      return (totalSeconds / 604800) + "d";
    } else if (totalSeconds % 86400 == 0) {
      return (totalSeconds / 86400) + "d";
    } else if (totalSeconds % 3600 == 0) {
      return (totalSeconds / 3600) + "h";
    } else if (totalSeconds % 60 == 0) {
      return (totalSeconds / 60) + "m";
    } else {
      return totalSeconds + "s";
    }
  }
}
