package info.bitrich.xchangestream.gateio;

import info.bitrich.xchangestream.core.*;
import info.bitrich.xchangestream.gateio.config.FuturesConfig;
import io.reactivex.rxjava3.core.Completable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.gateio.GateioExchange;

public class GateioStreamingFuturesExchange extends GateioExchange implements StreamingExchange {

  private GateioStreamingFuturesService streamingService;
  private GateioStreamingFuturesMarketDataService streamingMarketDataService;
  private StreamingTradeService streamingTradeService;
  private StreamingAccountService streamingAccountService;

  public GateioStreamingFuturesExchange() {}

  @Override
  public Completable connect(ProductSubscription... args) {
    streamingService =
        new GateioStreamingFuturesService(
            exchangeSpecification.getSslUri(),
            exchangeSpecification.getApiKey(),
            exchangeSpecification.getSecretKey());
    applyStreamingSpecification(exchangeSpecification, streamingService);
    streamingMarketDataService = new GateioStreamingFuturesMarketDataService(streamingService);


    return streamingService.connect();
  }

  @Override
  public Completable disconnect() {
    GateioStreamingFuturesService service = streamingService;
    streamingService = null;
    streamingMarketDataService = null;
    streamingTradeService = null;
    streamingAccountService = null;
    return service.disconnect();
  }

  @Override
  public StreamingMarketDataService getStreamingMarketDataService() {
    return streamingMarketDataService;
  }

  @Override
  public StreamingTradeService getStreamingTradeService() {
    return streamingTradeService;
  }

  @Override
  public StreamingAccountService getStreamingAccountService() {
    return streamingAccountService;
  }

  @Override
  public boolean isAlive() {
    return streamingService != null && streamingService.isSocketOpen();
  }

  @Override
  public void useCompressedMessages(boolean compressedMessages) {
    streamingService.useCompressedMessages(compressedMessages);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification specification = super.getDefaultExchangeSpecification();
    specification.setShouldLoadRemoteMetaData(false);
    specification.setSslUri(FuturesConfig.V4_URL);
    return specification;
  }
}
