package evaluator.collector;

import data.CandleStick;

import java.util.ArrayList;

public class DataCollectorFactory implements ReceiveDataHandler{
    ArrayList<StockDataCollector> collectors;

    public DataCollectorFactory(ArrayList<StockDataCollector> collectors) {
        this.collectors = collectors;
    }

    public DataCollectorFactory() {
        this.collectors = new ArrayList<>();
    }

    @Override
    public void onReceive(CandleStick candleStick) {
        insert(candleStick);
    }
    public synchronized void insert(CandleStick candleStick) {
        for(StockDataCollector collector : collectors)
            if(collector.getSymbol().equals(candleStick.getSymbol()))
                collector.insert(candleStick);
    }
}
