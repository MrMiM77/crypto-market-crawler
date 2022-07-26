package collector.kafka;

import api.Main;
import collector.config.Config;
import data.CandleStick;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class KafkaMessageProducer implements MessageHandler{
    private Producer<String, CandleStick> kafkaProducer;
    private String topicName;
    private int totalMessage;
    private static final Logger logger = LogManager.getLogger(KafkaMessageProducer.class);

    private KafkaMessageProducer instance;

    public KafkaMessageProducer getInstance() {
        if(instance == null)
            instance = new KafkaMessageProducer();
        return instance;
    }

    public KafkaMessageProducer() {
        this.topicName = Config.getKafkaTopicName();
        Properties props = new Properties();
        props.put("bootstrap.servers", Config.getKafkaServerAddress());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CandleStickSerializer.class.getName());
        this.kafkaProducer = new KafkaProducer<>(props);
        logger.info("kafka producer is initialize");

    }
    public void onMessage(CandleStick candleStick) {
        produce(candleStick);
    }
    public synchronized void produce(CandleStick candleStick) {
        this.kafkaProducer.send(new ProducerRecord<String, CandleStick>(topicName,
                Integer.toString(totalMessage++), candleStick));
        logger.info("new message is produced by kafka" + candleStick);
    }
}
