package collector.crawler;

import collector.crawler.WebSocketClient;
import collector.kafka.MessageHandler;

import java.util.ArrayList;

public class ClientFactory {
    private ArrayList<Client> clients;
    public ClientFactory(){
        clients = new ArrayList<>();
    }


    public void newWebSocketClient(String symbol, MessageHandler messageHandler) {
        WebSocketClient newClient = new collector.crawler.WebSocketClient(symbol, messageHandler);
        clients.add(newClient);
        newClient.startClient();
    }

}
