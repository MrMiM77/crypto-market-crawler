package api.database;

import api.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {
    private static Database instance;
    private Connection connection;
    //TODO read from config
    private String databaseURL;
    private String databaseUser;
    private String databasePassword;
    private final static Logger LOGGER = LoggerFactory.getLogger(Database.class);


    private Database() {

        databaseURL = Config.getDatabaseHost();
        databaseUser = Config.getDatabaseUser();
        databasePassword = Config.getDatabasePassword();
        try {
            connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
            Statement createTableStatement = connection.createStatement();
            String createTableSql = "CREATE TABLE IF NOT EXISTS sma_rules"
                    + "(id int PRIMARY KEY AUTO_INCREMENT, name varchar(30), symbol varchar(30)," +
                    "first_window varchar(40), second_window varchar(40)," +
                    "start_time varchar(40), finish_time varchar(40))";
            createTableStatement.execute(createTableSql);
            LOGGER.debug("data base is created");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static Database getInstance() {
        if(instance == null)
            instance = new Database();
        return instance;
    }
    public HashMap<Integer, HashMap<String, Object>> getSMARules(String symbol) {
        try {
            Statement getStatement = connection.createStatement();
            String query = String.format("SELECT id, name, first_window, second_window, start_time, finish_time FROM sma_rules" +
                    " WHERE symbol = '%s';",symbol);
            ResultSet resultSet = getStatement.executeQuery(query);

            HashMap<Integer, HashMap<String, Object>> resultMap = new HashMap<>();
            while (resultSet.next()) {
                HashMap<String, Object> rowMap = new HashMap<>();
                int id = resultSet.getInt("id");

                rowMap.put("name", resultSet.getString("name"));


                rowMap.put("first_window", resultSet.getString("first_window"));

                rowMap.put("second_window", resultSet.getString("second_window"));

                rowMap.put("start_time", resultSet.getString("start_time"));

                rowMap.put("finish_time", resultSet.getString("finish_time"));
                resultMap.put(id, rowMap);
            }
            return resultMap;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
