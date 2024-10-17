package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

        // Methods for performing CRUD operations on the database
    public int createMessage(Message message) throws SQLException {
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare a query for inserting a message into the database
        String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) values (?,?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        // Set parameter values to message details. 
        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());

        // Add message to database
        int messagesCreated = preparedStatement.executeUpdate();

        return messagesCreated;
    }

    public List<Message> getAllMessages() throws SQLException {
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Create an array list for storing messages
        List<Message> messages = new ArrayList<>();

        // Prepare query for getting all messages
        String sql = "SELECT * FROM Message;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // Execute query and store messages in resultSet
        ResultSet rs = preparedStatement.executeQuery();

        // Iterate through result set and add each message to array list
        while(rs.next())
        {
            Message message = new Message(rs.getInt("message_id"), 
                                          rs.getInt("posted_by"), 
                                          rs.getString("message_text"),
                                          rs.getLong("time_posted_epoch"));

            messages.add(message);
        }

        // Return all messages
        return messages;
    }

    public Message getMessageById(int message_id) throws SQLException {
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare query for retrieving message
        String sql = "SELECT * FROM Message WHERE message_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message_id);

        // Execute query and store results in result set
        ResultSet rs = preparedStatement.executeQuery();

        // Iterate through result set, and initialize message with values retrieved
        while(rs.next())
        {
            Message message = new Message(rs.getInt("message_id"), 
                                          rs.getInt("posted_by"), 
                                          rs.getString("message_text"),
                                          rs.getLong("time_posted_epoch"));
            return message;
        }
        return null;
    }
    
    public int updateMessage(Message message) throws SQLException {
        // Connnect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare query for updating message
        String sql = "UPDATE Message SET message_text=? WHERE message_id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, message.getMessage_text());
        preparedStatement.setInt(2, message.getMessage_id());

        // Run SQL
        int numRowsAffected = preparedStatement.executeUpdate();

        return numRowsAffected;
    }

    public int deleteMessageById(int message_id) throws SQLException{
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare query for deleting message
        String sql = "DELETE FROM Message WHERE message_id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, message_id);
        
        // Delete message
        int numRowsAffected = preparedStatement.executeUpdate();
        return numRowsAffected;
    }

    public int deleteAllMessages() throws SQLException{
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare query for deleting all messages
        String sql = "DELETE * FROM Message;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        // Delete all messages
        int numRowsAffected = preparedStatement.executeUpdate();
        return numRowsAffected;
    }
    
}
