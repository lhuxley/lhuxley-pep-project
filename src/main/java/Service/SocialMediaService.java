package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;
import java.util.List;


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

    public List<Message> retrieveAllMessages() {
        return socialMediaDAO.retrieveAllMessages();
    }  

    public Message retrieveMessageById(Integer message_id) {
        return socialMediaDAO.retrieveMessageById(message_id);
    }

    public Message deleteMessageById(Integer message_id) {
        return socialMediaDAO.deleteMessageById(message_id);
    }


}
