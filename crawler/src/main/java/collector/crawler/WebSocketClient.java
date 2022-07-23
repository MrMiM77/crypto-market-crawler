package collector.crawler;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.market.CandlestickInterval;
import collector.kafka.MessageHandler;

public class WebSocketClient extends Client {
    private final BinanceApiWebSocketClient apiWebSocketClient;
    private final String symbol;
    private final CallBackHandler callBackHandler;

    public WebSocketClient(String symbol, MessageHandler messageHandler) {
        super(messageHandler);
        apiWebSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();

        this.symbol = symbol;
        callBackHandler = new CallBackHandler(messageHandler);
    }

    public void startClient() {
        apiWebSocketClient.onCandlestickEvent(symbol, CandlestickInterval.ONE_MINUTE,callBackHandler);
    }
}
