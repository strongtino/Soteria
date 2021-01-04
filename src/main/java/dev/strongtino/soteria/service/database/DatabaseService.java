package dev.strongtino.soteria.service.database;

import dev.strongtino.soteria.service.license.License;
import dev.strongtino.soteria.service.license.product.Product;
import dev.strongtino.soteria.util.Base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by StrongTino on 28.12.2020.
 */

public class DatabaseService implements Base {

    private Connection connection;

    public DatabaseService() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connect();
        setup();
    }

    public void insertLicense(License license) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("INSERT INTO " + TABLE_LICENSES + " VALUES(?, ?, ?)");
            statement.setString(1, license.getKey());
            statement.setString(2, license.getUser());
            statement.setString(3, license.getProduct().name());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadLicenses() {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + TABLE_LICENSES);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String license = result.getString("key");
                String user = result.getString("user");
                Product product = Product.getByName(result.getString("product"));

                SOTERIA.getLicenseService().addLicenseToMap(new License(license, user, product));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean rowExistInTable(String table, String key, Object value) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM " + table + " WHERE `" + key + "`=?");

            if (value instanceof Integer) {
                statement.setInt(1, (int) value);
            } else if (value instanceof Float) {
                statement.setFloat(1, (float) value);
            } else if (value instanceof Double) {
                statement.setDouble(1, (double) value);
            } else if (value instanceof Long) {
                statement.setLong(1, (long) value);
            } else if (value instanceof String) {
                statement.setString(1, (String) value);
            } else if (value instanceof Boolean) {
                statement.setBoolean(1, (boolean) value);
            }
            boolean output = statement.executeQuery().next();
            statement.close();

            return output;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/soteria",
                    "strongtino",
                    "bluesky23"
            );
        } catch (SQLException e) {
            warning("Failed to establish a connection with MySQL.");
            System.exit(1);
        }
    }

    private void setup() {
        AtomicReference<PreparedStatement> statement = new AtomicReference<>();

        getStatements().forEach(sql -> {
            try {
                statement.set(connection.prepareStatement(sql));
                statement.get().executeUpdate();
                statement.get().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private Set<String> getStatements() {
        Set<String> toReturn = new HashSet<>();

        toReturn.add("CREATE TABLE IF NOT EXISTS " + TABLE_LICENSES + "("
                + "`key` VARCHAR(32), "
                + "user VARCHAR(64), "
                + "product VARCHAR(32))");

        return toReturn;
    }
}