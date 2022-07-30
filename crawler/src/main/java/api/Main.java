package api;

import api.config.Config;
import api.service.SMAHandler;

import static spark.Spark.port;

public class Main {
    public static void main(String[] args) {
        Config.setConfigs();
        System.out.println(Config.getDatabaseHost());
        SMAHandler smaHandler = new SMAHandler();
        port(80);
        smaHandler.initializeHandlers();
    }
}
