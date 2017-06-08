package com.gamerking195.dev.thirst.util;

import com.gamerking195.dev.thirst.Main;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UtilSQL {
    private UtilSQL() {}
    private static UtilSQL instance = new UtilSQL();
    public static UtilSQL getInstance() {
        return instance;
    }

    private HikariDataSource dataSource;

    public void init() {
        if (dataSource == null) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://"+Main.getInstance().getYAMLConfig().hostName+"/"+Main.getInstance().getYAMLConfig().database);
            config.setUsername(Main.getInstance().getYAMLConfig().username);
            config.setPassword(Main.getInstance().getYAMLConfig().password);

            config.setMaximumPoolSize(5);

            dataSource = new HikariDataSource(config);
        }

        runStatementSync("CREATE TABLE IF NOT EXISTS "+Main.getInstance().getYAMLConfig().tablename+" (uuid varchar(36) NOT NULL, thirst INT, PRIMARY KEY(uuid))");
    }

    public void runStatement(String statement) {
        final String updatedStatement = statement.replace("TABLENAME", Main.getInstance().getYAMLConfig().tablename);

        Connection connection;

        if (dataSource == null)
            init();

        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updatedStatement);

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        preparedStatement.execute();

                        connection.close();
                    } catch (Exception ex) {
                        //cant do fancy error logging bc it does bukkit calls ;-;
                        ex.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(Main.getInstance());
        }
        catch(Exception ex) {
            Main.getInstance().printError(ex, "Error occurred while running MySQL statement.");
        }
    }

    public void runStatementSync(String statement) {
        final String updatedStatement = statement.replace("TABLENAME", Main.getInstance().getYAMLConfig().tablename);

        if (dataSource == null)
            init();

        Connection connection;

        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updatedStatement);

            try {
                preparedStatement.execute();

                connection.close();
            } catch (Exception ex) {
                //cant do fancy error logging bc it does bukkit calls ;-;
                ex.printStackTrace();
            }
        }
        catch(Exception ex) {
            Main.getInstance().printError(ex, "Error occurred while running MySQL statement.");
        }
    }

    //No real need to run queries async literally just getting a 36 char string & a 2-3 digit number.
    public ResultSet runQuery(String query) {
        final String updatedQuery = query.replace("TABLENAME", Main.getInstance().getYAMLConfig().tablename);

        Connection connection;

        try {
            connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updatedQuery);

            //Give whatever task that is using this 1 second to complete it, TODO find a better way to close the connection.
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        connection.close();
                    }
                    catch(Exception ex) {
                        Main.getInstance().printError(ex, "Error occurred while closing connection.");
                    }
                }
            }.runTaskLater(Main.getInstance(), 20L);

            return preparedStatement.executeQuery();
        }
        catch(Exception ex) {
            Main.getInstance().printError(ex, "Error occurred while running query '"+updatedQuery+"'.");
        }

        return null;
    }
}
