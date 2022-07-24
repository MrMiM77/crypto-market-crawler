package evaluator.database;
import data.Rule;

import java.sql.*;

public class Database {
    private Database instance;
    private Connection connection;
    //TODO read from config
    private String databaseURL;
    private String databaseUser;
    private String databasePassword;


    public Database() {
        databaseURL = "jdbc:mysql://localhost:3306/myDb";
        databaseUser = "root";
        databasePassword = "root1234";
        try {
            connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
            Statement createTableStatement = connection.createStatement();
            String createTableSql = "CREATE TABLE IF NOT EXISTS rules"
                    + "(rule_id int PRIMARY KEY AUTO_INCREMENT, name varchar(30), symbol_name varchar(30)," +
                    "start_time varchar(100), finish_time varchar(100), rule_type varchar(30)";
            createTableStatement.execute(createTableSql);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public Database getInstance() {
        if(instance == null)
            instance = new Database();
        return instance;
    }
    public synchronized void insertRule(Rule rule) {

    }

    public Connection getConnection() {
        return connection;
    }
}
