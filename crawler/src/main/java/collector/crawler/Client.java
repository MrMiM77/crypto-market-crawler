package collector.crawler;

import collector.kafka.MessageHandler;

public abstract class Client {
    private MessageHandler messageHandler;

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public Client(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
