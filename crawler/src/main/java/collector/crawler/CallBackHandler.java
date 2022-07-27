package collector.crawler;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.domain.event.CandlestickEvent;
import data.CandleStick;
import collector.kafka.MessageHandler;


public class CallBackHandler implements BinanceApiCallback{
    private MessageHandler messageHandler;
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
        candleStick.setClose(Long.parseLong(event.getClose()));
        candleStick.setOpen(Long.parseLong(event.getOpen()));
        candleStick.setHigh(Long.parseLong(event.getHigh()));
        candleStick.setLow(Long.parseLong(event.getHigh()));


        candleStick.setStartTime(event.getOpenTime());
        candleStick.setFinishTime(event.getCloseTime());
        candleStick.setAverage((candleStick.getOpen() + candleStick.getClose()) / 2);
        messageHandler.onMessage(candleStick);
    }

    @Override
    public void onFailure(Throwable cause) {
        BinanceApiCallback.super.onFailure(cause);
    }
}
