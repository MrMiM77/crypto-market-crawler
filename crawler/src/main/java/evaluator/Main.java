package evaluator;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import data.CandleStick;
import data.EvaluatedRule;
import data.MovingAverageRule;
import evaluator.collector.DataCollectorFactory;
import evaluator.config.Config;
import evaluator.database.RuleHandler;
import evaluator.evaluators.RuleEvaluatedHandler;
import evaluator.evaluators.RuleEvaluatorFactory;
import evaluator.kafka.KafkaMessageConsumer;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

public class Main {

    public static ArrayList<String> readSymbolsFromFile() {
        try {
            // create Gson instance
                // create Gson instance
                Gson gson = new Gson();
                // create a reader
                Reader reader = Files.newBufferedReader(Paths.get("./crawler/src/main/java/evaluator/symbols.json"));

                Map<?, ?> map = gson.fromJson(reader, Map.class);
                reader.close();
                return (ArrayList<String>) map.get("symbols");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
    }
    public static ArrayList<MovingAverageRule> readRulesFromFile() {
        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("./crawler/src/main/java/evaluator/rules.json"));
            // convert JSON file to map
            List<LinkedTreeMap> mapRules = gson.fromJson(reader, List.class);
            ArrayList<MovingAverageRule> rules = new ArrayList<>();
            for(LinkedTreeMap<? , ?> mapRule : mapRules) {
                MovingAverageRule rule = new MovingAverageRule();

                rule.setName((String) mapRule.get("name"));

                rule.setSymbol((String) mapRule.get("symbol"));

                rule.setRuleType((String) mapRule.get("rule_type"));

                rule.setFirstWindow(Duration.ofHours(Long.parseLong((String) mapRule.get("first_window"))));

                rule.setSecondWindow(Duration.ofHours(Long.parseLong((String) mapRule.get("second_window"))));
                rules.add(rule);
            }

            reader.close();
            return rules;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        //read config ind symbols and rules
        ArrayList<MovingAverageRule> rules = readRulesFromFile();
        ArrayList<String> symbols = readSymbolsFromFile();
        Config.setConfigs();

        // init collector factory
        DataCollectorFactory collectorFactory = DataCollectorFactory.getInstance();
        for(String symbol : symbols){
            collectorFactory.addCollector(symbol);
        }

        // start the kafka consumer
        KafkaMessageConsumer kafkaConsumer = new KafkaMessageConsumer(collectorFactory);
        Thread consumerThread = new Thread(kafkaConsumer);
        consumerThread.start();

        //start the rule handlers factory
        RuleHandler onEvaluationHandler = new RuleHandler();
        RuleEvaluatorFactory ruleEvaluatorFactory = new RuleEvaluatorFactory();
        for(MovingAverageRule rule : rules)
            ruleEvaluatorFactory.addRule(rule, onEvaluationHandler);
        ruleEvaluatorFactory.start();
    }
}
