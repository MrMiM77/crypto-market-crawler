package collector.crawler;

import api.Main;
import collector.kafka.MessageHandler;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import data.CandleStick;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class RestApiClient extends Client{
    private final BinanceApiRestClient binanceApiRestClient;
    private final String symbol;
    private static final Logger logger = LogManager.getLogger(RestApiClient.class);
    public RestApiClient(MessageHandler messageHandler, String symbol) {
        super(messageHandler);
        this.symbol = symbol;
        binanceApiRestClient = BinanceApiClientFactory.newInstance().newRestClient();
    }
    public void startClient() {
        System.out.println(symbol.toUpperCase());
        List<com.binance.api.client.domain.market.Candlestick> allHistoryCandles = binanceApiRestClient.
                getCandlestickBars(symbol.toUpperCase(), CandlestickInterval.ONE_MINUTE);
        for(com.binance.api.client.domain.market.Candlestick candleStick : allHistoryCandles) {
            CandleStick dataCandleStick = new CandleStick();
            dataCandleStick.setSymbol(symbol);
            dataCandleStick.setClose(Double.parseDouble(candleStick.getClose()));
            dataCandleStick.setOpen(Double.parseDouble(candleStick.getOpen()));
            dataCandleStick.setHigh(Double.parseDouble(candleStick.getHigh()));
            dataCandleStick.setLow(Double.parseDouble(candleStick.getHigh()));

            dataCandleStick.setStartTime(candleStick.getOpenTime());
            dataCandleStick.setFinishTime(candleStick.getCloseTime());

            dataCandleStick.setAverage((dataCandleStick.getOpen() + dataCandleStick.getClose()) / 2);
            super.getMessageHandler().onMessage(dataCandleStick);
            logger.info("new message is received from api: " + candleStick);

        }
    }
}
