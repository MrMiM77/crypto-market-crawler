package api.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
    private static String databaseHost;
    private static String databaseUser;
    private static String databasePassword;

    public static String getDatabaseHost() {
        return databaseHost;
    }

    public static String getDatabaseUser() {
        return databaseUser;
    }

    public static String getDatabasePassword() {
        return databasePassword;
    }

    public static void setConfigs() {
        try {
            Configuration config = new PropertiesConfiguration("./crawler/src/main/java/api/config/config.properties");
            databaseHost = config.getString("database.host");
            databaseUser = config.getString("database.user");
            databasePassword = config.getString("database.password");
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
