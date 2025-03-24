package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text().length() == 0 || message.getMessage_text().length() >= 256) {
            return null;
        }
        return messageDAO.createMessage(message);
    }

    public List<Message> retrieveAllMessages() {
        return messageDAO.retrieveAllMessages();
    }

    public Message retrieveMessageById(Integer message_id) {
        return messageDAO.retrieveMessageById(message_id);
    }

    public Message deleteMessageById(Integer message_id) {
        return messageDAO.deleteMessageById(message_id);
    }

    public Message updateMessage(Message message) {

        if (message == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }

        Message oldMessage = messageDAO.retrieveMessageById(message.getMessage_id());
        if (oldMessage == null) {
            return null;
        }

        message.setPosted_by(oldMessage.getPosted_by());
        message.setTime_posted_epoch(oldMessage.getTime_posted_epoch());

        return messageDAO.updateMessage(message);
    }

    public List<Message> retrieveMessagesByAccount(Integer account_id) {
        return messageDAO.retrieveMessagesByAccount(account_id);
    }
}
