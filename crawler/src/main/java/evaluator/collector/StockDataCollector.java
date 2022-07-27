package evaluator.collector;

import data.CandleStick;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

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

    public ArrayList<CandleStick> getHourCandles() {
        return hourCandles;
    }

    public void setHourCandles(ArrayList<CandleStick> hourCandles) {
        this.hourCandles = hourCandles;
    }

    public ArrayList<CandleStick> getMinuteCandles() {
        return minuteCandles;
    }

    public void setMinuteCandles(ArrayList<CandleStick> minuteCandles) {
        this.minuteCandles = minuteCandles;
    }
    public void extractHourCandleFromMinuteCandles() {
        // TODO make it complete synchronize
        CandleStick firstMinuteCandle = minuteCandles.get(0);
        CandleStick lastMinuteCandle = minuteCandles.get(minuteCandles.size() - 1);
        int firstMinuteCandleHour = getHourOfCandle(firstMinuteCandle);
        int lastMinuteCandleHour = getHourOfCandle(lastMinuteCandle);
        int lastSameHourIndex = 0;
        if(firstMinuteCandleHour == lastMinuteCandleHour)
            return;
        for (CandleStick candle : minuteCandles) {
            if (getHourOfCandle(candle) == firstMinuteCandleHour)
                lastSameHourIndex++;
            else break;
        }
        ArrayList<CandleStick> firstHourCandles = (ArrayList<CandleStick>) minuteCandles.subList(0, lastSameHourIndex);
        CandleStick convertedHourCandleStick = convertMinutesCandleToHourCandle(firstHourCandles);
        hourCandles.add(convertedHourCandleStick);
        minuteCandles.subList(0, lastMinuteCandleHour).clear();

    }

    private CandleStick convertMinutesCandleToHourCandle(ArrayList<CandleStick> candleSticks) {
        long startTime = candleSticks.get(0).getStartTime();
        long finishTime = candleSticks.get(candleSticks.size() - 1).getFinishTime();
        double open = candleSticks.get(0).getOpen();
        double close = candleSticks.get(candleSticks.size() - 1).getClose();
        double high = 0;
        double low = 0;
        double sum = 0;
        for(CandleStick candleStick : candleSticks) {
            if(candleStick.getHigh() > high)
                high = candleStick.getHigh();
            if(candleStick.getLow() < low)
                low = candleStick.getLow();
            sum += candleStick.getAverage();
        }
        CandleStick resultCandleStick = new CandleStick();
        resultCandleStick.setAverage(sum / candleSticks.size());
        resultCandleStick.setOpen(open);
        resultCandleStick.setClose(close);
        resultCandleStick.setStartTime(startTime);
        resultCandleStick.setFinishTime(finishTime);
        resultCandleStick.setLow(low);
        resultCandleStick.setHigh(high);
        resultCandleStick.setSymbol(symbol);

        return resultCandleStick;

    }

    public synchronized void insert(CandleStick candle) {
            minuteCandles.add(candle);
            sortCandles();
            extractHourCandleFromMinuteCandles();
            sortCandles();
    }
    private int getHourOfCandle(CandleStick candle) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(candle.getStartTime());
        return calendar.get(Calendar.HOUR);
    }

    private void sortCandles() {
        minuteCandles.sort(new Comparator<CandleStick>() {
            @Override
            public int compare(CandleStick o1, CandleStick o2) {
                return Long.compare(o1.getStartTime(), o2.getStartTime());
            }
        });

        hourCandles.sort(new Comparator<CandleStick>() {
            @Override
            public int compare(CandleStick o1, CandleStick o2) {
                return Long.compare(o1.getStartTime(), o2.getStartTime());
            }
        });
    }
}
