package collector.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
    private static String kafkaTopicName;
    private static String kafkaServerAddress;

    public static void setConfigs() {
        try {
            Configuration config = new PropertiesConfiguration("./crawler/src/main/java/collector/config/config.properties");
            kafkaTopicName = config.getString("kafka.topicName");
            kafkaServerAddress = config.getString("kafka.server");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getKafkaTopicName() {
        return kafkaTopicName;
    }

    public static String getKafkaServerAddress() {
        return kafkaServerAddress;
    }
}
