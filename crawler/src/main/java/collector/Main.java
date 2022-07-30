package collector;

import collector.config.Config;
import collector.crawler.ClientFactory;
import collector.kafka.KafkaMessageProducer;
import com.google.gson.Gson;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static ArrayList<String> readListOfSymbols() {
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get("./crawler/src/main/java/collector/symbols.json"));

            // convert JSON file to map
            Map<?, ?> map = gson.fromJson(reader, Map.class);
            reader.close();
            return (ArrayList<String>) map.get("symbols");
            // print map entries
            // close reader

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        readListOfSymbols();
        Config.setConfigs();
        ArrayList<String> symbols =  readListOfSymbols();
        KafkaMessageProducer producer = new KafkaMessageProducer();
        ClientFactory clientFactory = new ClientFactory();

        for(String symbol : symbols) {
            System.out.println(symbol);
            clientFactory.newWebSocketClient(symbol, producer);
            clientFactory.newRestApiClient(symbol, producer);
        }
    }
}
