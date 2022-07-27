package evaluator.database;
import data.EvaluatedRule;
import data.MovingAverageRule;
import data.Rule;

import java.sql.*;
import java.util.Calendar;

public class Database {
    private static Database instance;
    private Connection connection;
    //TODO read from config
    private String databaseURL;
    private String databaseUser;
    private String databasePassword;


    private Database() {
        databaseURL = "jdbc:mysql://localhost:3306/myDb";
        databaseUser = "root";
        databasePassword = "root1234";
        try {
            connection = DriverManager.getConnection(databaseURL, databaseUser, databasePassword);
            Statement createTableStatement = connection.createStatement();
            String createTableSql = "CREATE TABLE IF NOT EXISTS sma_rules"
                    + "(id int PRIMARY KEY AUTO_INCREMENT, name varchar(30), symbol_name varchar(30)," +
                    "first_window DATETIME, second_window DATETIME," +
                    "start_time DATETIME, finish_time DATETIME, primary key(id))";
            createTableStatement.execute(createTableSql);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static Database getInstance() {
        if(instance == null)
            instance = new Database();
        return instance;
    }
    public synchronized void insertRule(EvaluatedRule evaluatedRule) {
        String query = " insert into sma_rules (name, symbol, first_window, second_window, start_time, finish_time)"
                + " values (?, ?, ?, ?, ?, ?)";
        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(evaluatedRule.getStart());
        long startTimeInMillis = calendar.getTimeInMillis();
        calendar.setTime(evaluatedRule.getFinish());
        long finishTimeInMillis = calendar.getTimeInMillis();
        try {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString (1, evaluatedRule.getRule().getName());
            preparedStmt.setString (2, evaluatedRule.getRule().getSymbol());



            preparedStmt.setDate   (3, new Date(evaluatedRule.getRule().getFirstWindow().toMillis()));

            preparedStmt.setDate   (4, new Date(evaluatedRule.getRule().getSecondWindow().toMillis()));

            preparedStmt.setDate   (5, new Date(startTimeInMillis));

            preparedStmt.setDate   (6, new Date(finishTimeInMillis));
            preparedStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
