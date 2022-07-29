package api;

import api.service.SMAHandler;

import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        SMAHandler smaHandler = new SMAHandler();
        port(80);
        smaHandler.initializeHandlers();
    }
}
