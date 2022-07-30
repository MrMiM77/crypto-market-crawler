package evaluator.database;
import data.EvaluatedRule;
import data.MovingAverageRule;
import data.Rule;
import evaluator.config.Config;

import java.sql.*;
import java.util.Calendar;

public class Database {
    private static Database instance;
    private Connection connection;
    private String databaseURL;
    private String databaseUser;
    private String databasePassword;


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
            System.out.println("create the db");
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

        System.out.println(calendar.getTime());
        long startTimeInMillis = calendar.getTimeInMillis();
        calendar.setTime(evaluatedRule.getFinish());
        long finishTimeInMillis = calendar.getTimeInMillis();
        System.out.println(calendar.getTime());
        System.out.println(new Date(startTimeInMillis).getYear());
        try {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString (1, evaluatedRule.getRule().getName());
            preparedStmt.setString (2, evaluatedRule.getRule().getSymbol());




            preparedStmt.setString(3, String.valueOf(evaluatedRule.getRule().getFirstWindow().toHours()));

            preparedStmt.setString(4, String.valueOf(evaluatedRule.getRule().getSecondWindow().toHours()));


            preparedStmt.setString   (5,evaluatedRule.getStart().toString());

            preparedStmt.setString   (6, evaluatedRule.getFinish().toString());
            System.out.println("add to db");
            preparedStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
