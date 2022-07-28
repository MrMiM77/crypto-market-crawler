package evaluator.collector;

import data.CandleStick;

import java.util.ArrayList;

public class DataCollectorFactory implements ReceiveDataHandler{
    ArrayList<StockDataCollector> collectors;

    private static DataCollectorFactory factory;
    public DataCollectorFactory(ArrayList<StockDataCollector> collectors) {
        this.collectors = collectors;
    }

    public static DataCollectorFactory getInstance() {
        if(factory == null)
            factory = new DataCollectorFactory();
        return factory;
    }
    public DataCollectorFactory() {
        this.collectors = new ArrayList<>();
    }

    @Override
    public void onReceive(CandleStick candleStick) {
        insert(candleStick);
    }
    public void addCollector(String symbol) {
        StockDataCollector collector = new StockDataCollector(symbol);
        collectors.add(collector);
    }
    public synchronized void insert(CandleStick candleStick) {
        System.out.println(candleStick);
        for(StockDataCollector collector : collectors)
            if(collector.getSymbol().equals(candleStick.getSymbol()))
                collector.insert(candleStick);
    }
    public ArrayList<CandleStick> getHoursCandleOfSymbol(String symbol) {
        for(StockDataCollector collector : collectors) {
            if(collector.getSymbol().equals(symbol))
                return collector.getHourCandles();
        }
        return null;
    }
    public ArrayList<CandleStick> getMinutesCandleOfSymbol(String symbol) {
        for(StockDataCollector collector : collectors) {
            if(collector.getSymbol().equals(symbol))
                return collector.getMinuteCandles();
        }
        return null;
    }
}
