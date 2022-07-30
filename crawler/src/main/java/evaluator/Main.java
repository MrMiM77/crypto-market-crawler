package evaluator;

import data.CandleStick;
import data.EvaluatedRule;
import data.MovingAverageRule;
import evaluator.collector.DataCollectorFactory;
import evaluator.config.Config;
import evaluator.database.RuleHandler;
import evaluator.evaluators.RuleEvaluatorFactory;
import evaluator.kafka.KafkaMessageConsumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Config.setConfigs();
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

        RuleHandler ETHBTCHandler = new RuleHandler();
        MovingAverageRule ETHBTCRule = new MovingAverageRule();
        movingAverageRule.setSymbol("ETHBTC");


        ruleEvaluatorFactory.start();
        //String url = "jdbc:mysql://localhost:3306/crawler_db?useSSL=false";
//        String username = "root";
//        String password = "root1234";
//
//        System.out.println("Connecting database...");
//        try (Connection connection = DriverManager.getConnection(url, username, password)) {
//            System.out.println("Database connected!");
//        } catch (SQLException e) {
//            throw new IllegalStateException("Cannot connect the database!", e);
//        }

//        RuleHandler ruleHandler = new RuleHandler();
//        EvaluatedRule evaluatedRule = new EvaluatedRule();
//        MovingAverageRule rule = new MovingAverageRule();
//        rule.setRuleType("SMA");
//        rule.setSymbol("NEOETH");
//        rule.setFirstWindow(Duration.ofHours(4));
//        rule.setSecondWindow(Duration.ofHours(3));
//
//        evaluatedRule.setRule(rule);
//        evaluatedRule.setFinish(new Date());
//        evaluatedRule.setStart(new Date());
//        ruleHandler.onEvaluateRule(evaluatedRule);
    }
}
