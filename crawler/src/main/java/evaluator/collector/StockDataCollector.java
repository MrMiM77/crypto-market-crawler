package evaluator.collector;

import data.CandleStick;

import java.util.ArrayList;

public class StockDataCollector {
    private String symbol;
    private ArrayList<CandleStick> hourCandles;
    private ArrayList<CandleStick> minuteCandles;

    public StockDataCollector(String symbol) {
        this.symbol = symbol;
        hourCandles = new ArrayList<>();
        minuteCandles = new ArrayList<>();
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void insert(CandleStick candle) {
        //TODO
    }
}
