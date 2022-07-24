package evaluator.collector;

import data.CandleStick;

public interface ReceiveDataHandler {
    public void onReceive(CandleStick candleStick);
}
