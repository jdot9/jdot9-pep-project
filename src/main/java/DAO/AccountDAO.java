package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    
    // Methods for performing CRUD operations on the database
    public int createAccount(Account account) throws SQLException {
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare a query for inserting an account into the database
        String sql = "INSERT INTO Account(username, password) values (?,?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

        // Set parameter values to account details. 
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());

        // Add account to database
        int accountsCreated = preparedStatement.executeUpdate();

        return accountsCreated;
    }

    public List<Account> getAllAccounts() throws SQLException {
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Create an array list for storing accounts
        List<Account> accounts = new ArrayList<>();

        // Prepare query for getting all accounts
        String sql = "SELECT * FROM Account;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        // Execute query and store accounts in resultSet
        ResultSet rs = preparedStatement.executeQuery();

        // Iterate through result set and add each account to array list
        while(rs.next())
        {
            Account account = new Account(rs.getInt("account_id"), 
                                          rs.getString("username"), 
                                          rs.getString("password"));

            accounts.add(account);
        }

        // Return all accounts
        return accounts;
    }

    public Account getAccountById(int account_id) throws SQLException {
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare query for retrieving account
        String sql = "SELECT * FROM Account WHERE account_id = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, account_id);

        // Execute query and store results in result set
        ResultSet rs = preparedStatement.executeQuery();

        // Iterate through result set, and initialize account with values retrieved
        while(rs.next())
        {
            Account account = new Account(rs.getInt("account_id"), 
                                          rs.getString("username"), 
                                          rs.getString("password"));
            return account;
        }
        return null;
    }
    
    public int updateAccount(Account account) throws SQLException {
        // Connnect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare query for updating account
        String sql = "UPDATE Account SET username=?, password=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());

        // Run SQL
        int numRowsAffected = preparedStatement.executeUpdate();

        return numRowsAffected;
    }

    public int deleteAccountById(int account_id) throws SQLException{
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare query for deleting account
        String sql = "DELETE FROM Account WHERE account_id=?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, account_id);
        
        // Delete account
        int numRowsAffected = preparedStatement.executeUpdate();
        return numRowsAffected;
    }

    public int deleteAllAccounts() throws SQLException{
        // Connect to database
        Connection connection = ConnectionUtil.getConnection();

        // Prepare query for deleting all accounts
        String sql = "DELETE * FROM Account;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        
        // Delete all accounts
        int numRowsAffected = preparedStatement.executeUpdate();
        return numRowsAffected;
    }
}
