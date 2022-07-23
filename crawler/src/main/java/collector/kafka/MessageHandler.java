package collector.kafka;

import data.CandleStick;

public interface MessageHandler {

    void onMessage(CandleStick candleStick);
}
