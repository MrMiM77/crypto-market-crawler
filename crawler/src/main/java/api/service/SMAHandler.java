package api.service;

import api.database.Database;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.delete;


public class SMAHandler {
    public void initializeHandlers() {
        ObjectMapper om = new ObjectMapper();
        get("/sma_rules/:symbol_name", (request, response) -> {
            String symbol = request.params("symbol_name");
            Database database = Database.getInstance();
            HashMap queryResult = database.getSMARules(symbol);
            System.out.println(queryResult);
            response.type("application/json");
            Gson gson = new Gson();
            if(queryResult == null)
            {
                response.status(404);
                return gson.toJson("not found");
            }
            return gson.toJson(queryResult);
        });
    }


}
