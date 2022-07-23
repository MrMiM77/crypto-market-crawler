package crawler;
import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.CandlestickInterval;
import data.CandleStick;
import kafka.MessageHandler;


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
        messageHandler.onMessage(candleStick);
    }

    @Override
    public void onFailure(Throwable cause) {
        BinanceApiCallback.super.onFailure(cause);
    }
}
