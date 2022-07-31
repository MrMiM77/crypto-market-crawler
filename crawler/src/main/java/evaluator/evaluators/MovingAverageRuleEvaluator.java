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
    private ArrayList<CandleStick> minutesCandle;
    private ArrayList<CandleStick> hoursCandle;
    private long startTime;
    public MovingAverageRuleEvaluator(MovingAverageRule rule, RuleEvaluatedHandler handler) {
        this.rule = rule;
        this.handler = handler;
    }


    public double getAverageOfWindow(Duration window) {

        long totalHours = window.toHours();
        long currentHour = getCurrentHour(minutesCandle, hoursCandle);
        logger.warn("total hour is: " + totalHours);
        logger.warn("current hour is: " + currentHour);
        ArrayList<CandleStick> allHoursCandleList = new ArrayList<>();
        if(minutesCandle.size() > 0) {
            CandleStick tempCandle = new CandleStick();
            tempCandle.setAverage(getAverageOfCandleList(minutesCandle));
            allHoursCandleList.add(tempCandle);
            if(minutesCandle.get(0).getStartTime() < startTime) {
                logger.warn("start time is updated for minute candle and symbol  is " + new Date(minutesCandle.get(0).getStartTime()));
                logger.warn("symbol is: " + this.rule.getSymbol());
                startTime = minutesCandle.get(0).getStartTime();
            }
            totalHours -= 1;
        }
        logger.warn("total hour after change is: " + totalHours);
        for(CandleStick hourCandle : hoursCandle) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(hourCandle.getStartTime());
            long startHour = calendar.get(Calendar.HOUR);
            logger.info("hour candle date is: " + new Date(hourCandle.getFinishTime()));
            logger.warn("start hour is: " + startHour);
            logger.warn("total hour is: " + totalHours);
            logger.warn("current hour is: " + currentHour);
            if(currentHour - startHour <= totalHours) {
                allHoursCandleList.add(hourCandle);
                if(hourCandle.getStartTime() < startTime) {
                    logger.warn("start time is updated by hour candle and symbol is: " + new Date(hourCandle.getStartTime()));
                    logger.warn("symbol is : " + this.rule.getSymbol());
                    startTime = hourCandle.getStartTime();
                }
            }
        }
        return getAverageOfCandleList(allHoursCandleList);
    }
    public long getLastCandleTime(){
        if(minutesCandle.size() > 0)
            return minutesCandle.get(minutesCandle.size() -1).getFinishTime();
        else if(hoursCandle.size() > 0)
            return hoursCandle.get(hoursCandle.size() - 1).getFinishTime();
        else
            return -1;
    }
    private long getCurrentHour(ArrayList<CandleStick> minuteCandles, ArrayList<CandleStick> hourCandles) {
        CandleStick lastCandle = null;
        Calendar calendar = Calendar.getInstance();
        if(minuteCandles.size() > 0) {
            lastCandle = minuteCandles.get(minuteCandles.size() - 1);

            calendar.setTimeInMillis(lastCandle.getStartTime());
            return calendar.get(Calendar.HOUR);

        }
        else if(hourCandles.size() > 0) {
            lastCandle = hourCandles.get(hourCandles.size() - 1);
            calendar.setTimeInMillis(lastCandle.getStartTime());
            return calendar.get(Calendar.HOUR);
        }
        else
            return -1;

    }

    public long getLastFinishTime() {
        if(this.minutesCandle.size() > 0)
            return minutesCandle.get(minutesCandle.size() - 1).getFinishTime();
        else if(hoursCandle.size() > 0)
            return hoursCandle.get(hoursCandle.size() - 1).getFinishTime();
        logger.warn("no candle found");
        return 0;
    }

    @Override
    public void Evaluate() {
        startTime = Long.MAX_VALUE;
        minutesCandle = DataCollectorFactory.getInstance().
                getMinutesCandleOfSymbol(this.rule.getSymbol());
        hoursCandle = DataCollectorFactory.getInstance().
                getHoursCandleOfSymbol(this.rule.getSymbol());
        double averageOfFirstWindow = getAverageOfWindow(this.rule.getFirstWindow());
        double averageOfSecondWindow = getAverageOfWindow(this.rule.getSecondWindow());
        Calendar calendar = Calendar.getInstance();
        Date finishTime = new Date(getLastFinishTime());

        //Date startTime = new Date(calendar.getTimeInMillis() - this.rule.getFirstWindow().toMillis());
        Date startTime = new Date(this.startTime);
        if(averageOfSecondWindow > averageOfFirstWindow)
        {
            EvaluatedRule evaluatedRule = new EvaluatedRule();
            evaluatedRule.setRule(this.rule);

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
