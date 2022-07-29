package evaluator.evaluators;

import data.CandleStick;
import data.EvaluatedRule;
import data.MovingAverageRule;
import data.Rule;
import evaluator.collector.DataCollectorFactory;
import evaluator.collector.StockDataCollector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MovingAverageRuleEvaluator extends RuleEvaluator{
    private MovingAverageRule rule;
    public MovingAverageRuleEvaluator(MovingAverageRule rule, RuleEvaluatedHandler handler) {
        this.rule = rule;
        this.handler = handler;
    }


    public double getAverageOfWindow(Duration window) {
        ArrayList<CandleStick> minutesCandle = DataCollectorFactory.getInstance().
                getMinutesCandleOfSymbol(this.rule.getSymbol());
        ArrayList<CandleStick> hoursCandle = DataCollectorFactory.getInstance().
                getHoursCandleOfSymbol(this.rule.getSymbol());
        System.out.println("minute candle size is: " + minutesCandle);
        System.out.println("hour candle size is: " + hoursCandle);

        long totalHours = window.toHours();
        long currentHour = getCurrentHour(minutesCandle, hoursCandle);
        ArrayList<CandleStick> allHoursCandleList = new ArrayList<>();
        if(minutesCandle.size() > 0) {
            CandleStick tempCandle = new CandleStick();
            tempCandle.setAverage(getAverageOfCandleList(minutesCandle));
            allHoursCandleList.add(tempCandle);
            totalHours -= 1;
        }
        for(CandleStick hourCandle : hoursCandle) {
            long startHour = Duration.ofMillis(hourCandle.getStartTime()).toHours();
            if(currentHour - startHour <= totalHours) {
                allHoursCandleList.add(hourCandle);
                System.out.println(window + "  " + hourCandle);
            }
        }
        System.out.println(allHoursCandleList);
        return getAverageOfCandleList(allHoursCandleList);
    }
    private long getCurrentHour(ArrayList<CandleStick> minuteCandles, ArrayList<CandleStick> hourCandles) {
        CandleStick lastCandle = null;
        if(minuteCandles.size() > 0) {
            lastCandle = minuteCandles.get(minuteCandles.size() - 1);

            return Duration.ofMillis(lastCandle.getFinishTime()).toHours();

        }
        else if(hourCandles.size() > 0) {
            lastCandle = hourCandles.get(hourCandles.size() - 1);
            return Duration.ofMillis(lastCandle.getFinishTime()).toHours();
        }
        else
            return -1;

    }


    @Override
    public void Evaluate() {
        double averageOfFirstWindow = getAverageOfWindow(this.rule.getFirstWindow());
        double averageOfSecondWindow = getAverageOfWindow(this.rule.getSecondWindow());
        System.out.println("rule is: " + rule);
        System.out.println("average of first window is: " + averageOfFirstWindow);
        System.out.println("average of second window is: " + averageOfSecondWindow);
        if(averageOfSecondWindow > averageOfFirstWindow)
        {
            EvaluatedRule evaluatedRule = new EvaluatedRule();
            evaluatedRule.setRule(this.rule);
            Calendar calendar = Calendar.getInstance();

            Date finishTime = calendar.getTime();
            Date startTime = new Date(calendar.getTimeInMillis() - this.rule.getFirstWindow().toMillis());
            evaluatedRule.setFinish(finishTime);
            evaluatedRule.setStart(startTime);
            handler.onEvaluateRule(evaluatedRule);
        }
    }

    private double getAverageOfCandleList(ArrayList<CandleStick> candleList) {
        double sum = 0;
        if(candleList.size() == 0)
            return 0.0;
        for(CandleStick candle : candleList) {
            sum += candle.getAverage();
        }
        return sum / candleList.size();
    }
    @Override
    public void cleanData() {

    }
}
