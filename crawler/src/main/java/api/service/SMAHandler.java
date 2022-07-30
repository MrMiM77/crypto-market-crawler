package api.service;

import api.Main;
import api.database.Database;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;


public class SMAHandler {

    private static final Logger logger = LogManager.getLogger(SMAHandler.class);
    public void initializeHandlers() {
        ObjectMapper om = new ObjectMapper();
        get("/sma_rules/:symbol_name", (request, response) -> {
            String symbol = request.params("symbol_name");
            Database database = Database.getInstance();
            HashMap queryResult = database.getSMARules(symbol);
            response.type("application/json");
            Gson gson = new Gson();
            if(queryResult == null)
            {
                response.status(404);
                logger.warn("find no result for: " + symbol);
                return gson.toJson("not found");
            }
            logger.info("get sma of symbol:" + symbol);
            logger.info("result is: " + queryResult);
            return gson.toJson(queryResult);
        });
    }


}
