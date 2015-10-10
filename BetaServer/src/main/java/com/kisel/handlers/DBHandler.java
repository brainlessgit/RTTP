package com.kisel.handlers;

import com.kisel.gen.ProtoMessages.Alien;
import com.kisel.gen.ProtoMessages.AuthReq;
import com.kisel.gen.ProtoMessages.SearchReq;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brainless
 */
public class DBHandler {

    private static final Logger logger = Logger.getLogger(DBHandler.class.getName());

    private Connection connect;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final String dbName;
    private static final String PERSIST_ALIEN = "INSERT INTO Alien(name, password, lang, address) VALUES (?, ?, ?, ?)";
    private static final String AUTH_ALIEN    = "SELECT id, name, password, lang, address FROM Alien WHERE name = ? AND password = ?";
    private static final String SEARCH_ALIEN  = "SELECT id, name, password, lang, address FROM Alien WHERE name LIKE ?";

    public DBHandler(String dbName) {
        this.dbName = dbName;
    }

    public void connect() throws Exception {
        try {
//            Class.forName("akka.Actor");
            Class.forName("org.postgresql.Driver");
//            DriverManager.registerDriver(new org.postgresql.Driver());
            connect = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/" + dbName,
                    "rttp", "password");
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Failed to connect to db", ex);
            throw ex;
        } catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "Failed to connect to db ", ex);
            throw ex;
        }
    }

    public int persist(Alien alien) {
        logger.log(Level.INFO, "Persisting alien" + alien);
        int alienId = 0;
        try {
            preparedStatement = connect.prepareStatement(PERSIST_ALIEN);
            preparedStatement.setObject(1, alien.getName());
            preparedStatement.setObject(2, alien.getPassword());
            preparedStatement.setObject(3, alien.getLang());
            preparedStatement.setObject(4, alien.getAddress());
            // check this shi
            alienId = preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                alienId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to persist alien, preparedStatement = " + preparedStatement, e);
        }
        return alienId;
    }

    public Alien auth(AuthReq authReq) {
        logger.log(Level.INFO, "Authenticating " + authReq);
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
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to auth alien", e);
        }
        return null;
    }

    public List<Alien> search(SearchReq searchReq) {
        List<Alien> result = new ArrayList<Alien>();
        try {
            preparedStatement = connect.prepareStatement(SEARCH_ALIEN);
            preparedStatement.setObject(1, "%" + searchReq.getName() + "%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Alien alien = Alien.newBuilder()
                        .setId(resultSet.getInt(1))
                        .setName(resultSet.getString(2))
                        .setPassword(resultSet.getString(3))
                        .setLang(resultSet.getString(4))
                        .setAddress(resultSet.getInt(5))
                        .build();
                result.add(alien);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Failed to execute search", e);
        }
        return result;
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
            logger.log(Level.WARNING, "Failed to close connections", e);
        }
    }
}
