package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public String createAccount(Account account) throws SQLException {
        // Check if account exists 
        int numAccountsCreated = accountDAO.createAccount(account);
        return numAccountsCreated + " account successfully created.";
    }

    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.getAllAccounts();
    }

    public Account getAccountById(int account_id) throws SQLException {
        return accountDAO.getAccountById(account_id);
    }

    public String updateAccount(Account account) throws SQLException {
        int numAccountsUpdated = accountDAO.updateAccount(account);
        return numAccountsUpdated + " account succesfully updated.";
    }

    public String deleteAccountById(int account_id) throws SQLException{
        int numAccountsDeleted = accountDAO.deleteAccountById(account_id);
        return numAccountsDeleted + " account succesfully deleted.";
    }

    public String deleteAllAccounts() throws SQLException{
        int numAccountsDeleted = accountDAO.deleteAllAccounts();
        return numAccountsDeleted + " accounts succesfully deleted.";
    }
}
