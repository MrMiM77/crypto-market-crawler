package collector;

import collector.crawler.ClientFactory;
import collector.kafka.KafkaMessageProducer;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> symbols = new ArrayList<>(List.of(new String[]{"NEOETH", "ETHBTC"}));
        KafkaMessageProducer producer = new KafkaMessageProducer();
        ClientFactory clientFactory = new ClientFactory();
        for(String symbol : symbols) {
            clientFactory.newWebSocketClient(symbol, producer);
            clientFactory.newRestApiClient(symbol, producer);
        }
    }
}
