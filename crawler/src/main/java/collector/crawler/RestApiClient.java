package collector.crawler;

import collector.kafka.MessageHandler;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import data.CandleStick;

import java.util.List;

public class RestApiClient extends Client{
    private final BinanceApiRestClient binanceApiRestClient;
    private final String symbol;
    public RestApiClient(MessageHandler messageHandler, String symbol) {
        super(messageHandler);
        this.symbol = symbol;
        binanceApiRestClient = BinanceApiClientFactory.newInstance().newRestClient();
    }
    public void startClient() {
        List<com.binance.api.client.domain.market.Candlestick> allHistoryCandles = binanceApiRestClient.
                getCandlestickBars(symbol, CandlestickInterval.ONE_MINUTE);
        for(com.binance.api.client.domain.market.Candlestick candleStick : allHistoryCandles) {
            CandleStick dataCandleStick = new CandleStick();
            dataCandleStick.setClose(Long.parseLong(candleStick.getClose()));
            dataCandleStick.setOpen(Long.parseLong(candleStick.getOpen()));
            dataCandleStick.setHigh(Long.parseLong(candleStick.getHigh()));
            dataCandleStick.setLow(Long.parseLong(candleStick.getHigh()));

            dataCandleStick.setStartTime(candleStick.getOpenTime());
            dataCandleStick.setFinishTime(candleStick.getCloseTime());

            dataCandleStick.setAverage((dataCandleStick.getOpen() + dataCandleStick.getClose()) / 2);
            super.getMessageHandler().onMessage(dataCandleStick);
        }
    }
}
