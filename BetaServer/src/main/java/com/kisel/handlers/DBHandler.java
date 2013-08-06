package com.kisel.handlers;

import com.kisel.gen.ProtoMessages.Alien;
import com.kisel.gen.ProtoMessages.AuthReq;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brainless
 */
public class DBHandler {

    private Connection connect;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final String dbName;
    private static final String PERSIST_ALIEN = "INSERT INTO Alien(name, password, lang, address) VALUES (?, ?, ?, ?)";
    private static final String AUTH_ALIEN = "SELECT id, name, password, lang, address FROM Alien WHERE name = ? AND password = ?";

    public DBHandler(String dbName) {
        this.dbName = dbName;
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + dbName,
                    "postgres", "rthsdybr");
        } catch (SQLException ex) {
            System.out.println(ex);
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
    }

    public int persist(Alien alien) {
        int res = 0;
        try {
            preparedStatement = connect.prepareStatement(PERSIST_ALIEN);
            preparedStatement.setObject(1, alien.getName());
            preparedStatement.setObject(2, alien.getPassword());
            preparedStatement.setObject(3, alien.getLang());
            preparedStatement.setObject(4, alien.getAddress());
            res = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return res;
    }

    public Alien auth(AuthReq authReq) {
        try {
            Alien.Builder alien;
            preparedStatement = connect.prepareStatement(AUTH_ALIEN);
            preparedStatement.setObject(1, authReq.getName());
            preparedStatement.setObject(2, authReq.getPassword());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                alien = Alien.newBuilder()
                        .setId(resultSet.getInt(1))
                        .setName(resultSet.getString(2))
                        .setPassword(resultSet.getString(3))
                        .setLang(resultSet.getString(4))
                        .setAddress(resultSet.getInt(5));
                return alien.build();       
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;

    }

    public void closeAll() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
