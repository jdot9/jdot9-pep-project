package Service;

import java.sql.SQLException;
import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public String createMessage(Message message) throws SQLException {
        int numMessagesCreated = messageDAO.createMessage(message);
        return numMessagesCreated + " message successfully saved.";
    }

    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int message_id) throws SQLException {
        return messageDAO.getMessageById(message_id);
    }

    public String updateMessage(Message message) throws SQLException {
        int numMessagesUpdated = messageDAO.updateMessage(message);
        return numMessagesUpdated + " message succesfully updated.";
    }

    public String deleteMessageById(int message_id) throws SQLException{
        int numMessagesDeleted = messageDAO.deleteMessageById(message_id);
        return numMessagesDeleted + " messaged succesfully deleted.";
    }

    public String deleteAllMessages() throws SQLException{
        int numMessagesDeleted = messageDAO.deleteAllMessages();
        return numMessagesDeleted + " messages succesfully deleted.";
    }
}
