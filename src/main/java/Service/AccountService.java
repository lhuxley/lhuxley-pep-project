package Service;

import Model.Account;
import DAO.AccountDAO;


public class AccountService {
    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account createAccount(Account account) {
        if (account.getUsername().isEmpty() || account.getPassword().length() <= 3 || accountDAO.usernameExists(account.getUsername())) {
            return null; 
        }

        return accountDAO.createAccount(account);
    }

    public Account login(Account account) {
        return accountDAO.login(account);
    }
    
}
