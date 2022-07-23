package evaluator.kafka;

import data.CandleStick;
import org.apache.kafka.clients.consumer.*;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;


public class KafkaMessageConsumer implements Runnable{
    private Consumer<String, CandleStick> kafkaConsumer;

    public KafkaMessageConsumer() {
        String topicName = "test-kafka";
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        // TODO add data deserializer
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "DataDeserializer");
        kafkaConsumer = new KafkaConsumer<>(props);

        //Kafka Consumer subscribes list of topics here.
        kafkaConsumer.subscribe(List.of(topicName));
        //print the topic name
        System.out.println("Subscribed to topic " + topicName);
    }

    @Override
    public void run() {
        while (true) {
            // TODO add exception
            ConsumerRecords<String, CandleStick> records = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, CandleStick> record : records) {

                // print the offset,key and value for the consumer records.
                System.out.printf("offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), record.value());
                System.out.println(record.value().getClose());
            }
        }
    }
}
