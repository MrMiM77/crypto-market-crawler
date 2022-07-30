package evaluator.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
    private static String kafkaTopicName;
    private static String kafkaServer;
    private static String databaseHost;
    private static String databaseUser;
    private static String databasePassword;

    public static void setConfigs() {
        try {
            Configuration config = new PropertiesConfiguration("./crawler/src/main/java/evaluator/config/config.properties");
            kafkaTopicName = config.getString("kafka.topicName");
            kafkaServer = config.getString("kafka.server");
            databaseHost = config.getString("database.host");
            databaseUser = config.getString("database.user");
            databasePassword = config.getString("database.password");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getKafkaTopicName() {
        return kafkaTopicName;
    }

    public static String getKafkaServer() {
        return kafkaServer;
    }

    public static String getDatabaseHost() {
        return databaseHost;
    }

    public static String getDatabaseUser() {
        return databaseUser;
    }

    public static String getDatabasePassword() {
        return databasePassword;
    }
}
