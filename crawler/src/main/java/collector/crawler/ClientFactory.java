package collector.crawler;

import collector.kafka.MessageHandler;

import java.util.ArrayList;

public class ClientFactory {
    private ArrayList<Client> clients;
    public ClientFactory(){
        clients = new ArrayList<>();
    }


    public void newWebSocketClient(String symbol, MessageHandler messageHandler) {
        WebSocketClient newClient = new WebSocketClient(symbol, messageHandler);
        clients.add(newClient);
        newClient.startClient();
    }

    public void newRestApiClient(String symbol, MessageHandler messageHandler) {
        RestApiClient client = new RestApiClient(messageHandler, symbol);
        clients.add(client);
        client.startClient();
    }

}
