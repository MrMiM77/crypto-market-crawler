package evaluator.kafka;

import api.Main;
import collector.kafka.KafkaMessageProducer;
import data.CandleStick;
import evaluator.collector.ReceiveDataHandler;
import evaluator.config.Config;
import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class KafkaMessageConsumer implements Runnable{
    private Consumer<String, CandleStick> kafkaConsumer;
    private ReceiveDataHandler handler;
    private static final Logger logger = LogManager.getLogger(KafkaMessageProducer.class);

    public KafkaMessageConsumer(ReceiveDataHandler handler) {
        String topicName = Config.getKafkaTopicName();
        Properties props = new Properties();

        props.put("bootstrap.servers", Config.getKafkaServer());
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CandleStickDeserializer.class.getName());
        kafkaConsumer = new KafkaConsumer<>(props);

        //Kafka Consumer subscribes list of topics here.
        kafkaConsumer.subscribe(List.of(topicName));
        //print the topic name
        logger.info("kafka consumer is initialized: " + kafkaConsumer);
        this.handler = handler;
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, CandleStick> records = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, CandleStick> record : records) {
                logger.info("new message is received: " + record.value());
                // print the offset,key and value for the consumer records.

                handler.onReceive(record.value());
            }
        }
    }
}
