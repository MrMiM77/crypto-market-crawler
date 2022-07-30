package collector.crawler;
import api.Main;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.domain.event.CandlestickEvent;
import data.CandleStick;
import collector.kafka.MessageHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CallBackHandler implements BinanceApiCallback{
    private MessageHandler messageHandler;
    private static final Logger logger = LogManager.getLogger(CallBackHandler.class);
    public CallBackHandler(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void onResponse(Object o) {
        CandlestickEvent event = (CandlestickEvent) o;
        CandleStick candleStick = new CandleStick();
        if(!event.getBarFinal())
            return;
        candleStick.setSymbol(event.getSymbol());
        candleStick.setClose(Double.parseDouble(event.getClose()));
        candleStick.setOpen(Double.parseDouble(event.getOpen()));
        candleStick.setHigh(Double.parseDouble(event.getHigh()));
        candleStick.setLow(Double.parseDouble(event.getHigh()));


        candleStick.setStartTime(event.getOpenTime());
        candleStick.setFinishTime(event.getCloseTime());
        candleStick.setAverage((candleStick.getOpen() + candleStick.getClose()) / 2);
        messageHandler.onMessage(candleStick);
        logger.info("new candle received from api :" + candleStick);

    }

    @Override
    public void onFailure(Throwable cause) {
        System.out.println("failure");
        BinanceApiCallback.super.onFailure(cause);
    }
}
