package evaluator;

import data.CandleStick;
import data.MovingAverageRule;
import evaluator.collector.DataCollectorFactory;
import evaluator.database.RuleHandler;
import evaluator.evaluators.RuleEvaluatorFactory;
import evaluator.kafka.KafkaMessageConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        DataCollectorFactory collectorFactory = DataCollectorFactory.getInstance();
        collectorFactory.addCollector("NEOETH");
        collectorFactory.addCollector("ETHBTC");

        KafkaMessageConsumer kafkaConsumer = new KafkaMessageConsumer(collectorFactory);

        Thread consumerThread = new Thread(kafkaConsumer);
        consumerThread.start();

        RuleHandler onEvaluationHandler = new RuleHandler();
        MovingAverageRule movingAverageRule = new MovingAverageRule();
        movingAverageRule.setSymbol("NEOETH");
        movingAverageRule.setRuleType("SMA_HOURLY");
        movingAverageRule.setFirstWindow(Duration.ofHours(3));
        movingAverageRule.setSecondWindow(Duration.ofHours(4));
        RuleEvaluatorFactory ruleEvaluatorFactory = new RuleEvaluatorFactory();
        ruleEvaluatorFactory.addRule(movingAverageRule, onEvaluationHandler);
        ruleEvaluatorFactory.start();
    }
}
