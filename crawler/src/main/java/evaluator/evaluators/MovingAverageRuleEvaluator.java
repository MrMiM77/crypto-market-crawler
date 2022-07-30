package evaluator.evaluators;

import api.Main;
import data.CandleStick;
import data.EvaluatedRule;
import data.MovingAverageRule;
import data.Rule;
import evaluator.collector.DataCollectorFactory;
import evaluator.collector.StockDataCollector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
public class MovingAverageRuleEvaluator extends RuleEvaluator{
    private MovingAverageRule rule;
    private static final Logger logger = LogManager.getLogger(MovingAverageRuleEvaluator.class);
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
            }
        }
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
            logger.info("find new rule: " + evaluatedRule);
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
