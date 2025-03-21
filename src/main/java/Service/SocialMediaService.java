package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {
    SocialMediaDAO socialMediaDAO;

    public SocialMediaService() {
        socialMediaDAO = new SocialMediaDAO();
    }

    public Account createAccount(Account account) {
        return socialMediaDAO.createAccount(account);
    }

    public Account login(Account account) {
        return socialMediaDAO.login(account);
    }

    public Message createMessage(Message message) {
        return socialMediaDAO.createMessage(message);
    }

}
