package DAO;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import Model.Message;

import Util.ConnectionUtil;

public class MessageDAO {
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "insert into message (posted_by, message_text, time_posted_epoch) values (?,?,?);";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                int messageId = rs.getInt("message_id");
                message.setMessage_id(messageId);
            }
            return message;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public List<Message> retrieveAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {

            String sql = "select * from message;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            return messages;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public Message retrieveMessageById(Integer message_id) {
        Connection connection = ConnectionUtil.getConnection();

        Message message = new Message();

        try {

            String sql = "select * from message where message_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public Message deleteMessageById(Integer message_id) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            Message message = retrieveMessageById(message_id);
            if (message == null) {
                return null;
            } else {
                String sql = "delete from message where message_id = ?;";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, message_id);
                preparedStatement.executeUpdate();

                return message;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public Message updateMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message.getMessage_id());
            int updatedRows = preparedStatement.executeUpdate();

            return updatedRows > 0 ? message : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> retrieveMessagesByAccount(Integer account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {

            String sql = "select * from message m left join account a on a.account_id = m.posted_by WHERE m.posted_by = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

            return messages;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }
}
