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
        long totalHours = window.toHours();
        ArrayList<CandleStick> allHoursCandleList = new ArrayList<>();
        if(minutesCandle.size() > 0) {
            CandleStick tempCandle = new CandleStick();
            tempCandle.setClose(getAverageOfCandleList(minutesCandle));
            allHoursCandleList.add(tempCandle);
            totalHours -= 1;
        }
        for(CandleStick hourCandle : hoursCandle) {
            long startHour = Duration.ofMillis(hourCandle.getStartTime()).toHours();
            if(startHour >= totalHours)
                allHoursCandleList.add(hourCandle);
        }
        return getAverageOfCandleList(allHoursCandleList);
    }


    @Override
    public void Evaluate() {
        double averageOfFirstWindow = getAverageOfWindow(this.rule.getFirstWindow());
        double averageOfSecondWindow = getAverageOfWindow(this.rule.getSecondWindow());
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
            // TODO add correct rule for this
            sum += candle.getClose();
        }
        return sum / candleList.size();
    }
    @Override
    public void cleanData() {

    }
}
