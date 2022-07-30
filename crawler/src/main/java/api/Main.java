package api;

import api.config.Config;
import api.service.SMAHandler;

import static spark.Spark.port;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;


public class Main {



    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {


        Config.setConfigs();
        logger.info("salam");
        System.out.println(Config.getDatabaseHost());
        SMAHandler smaHandler = new SMAHandler();
        System.out.println("fdaf af a");
        logger.error("server is up");
        logger.info("saalam");
        port(80);

        smaHandler.initializeHandlers();
    }
}
