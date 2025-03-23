package DAO;

import Model.Account;
import Model.Message;
import java.util.List;
import java.util.ArrayList;

import java.sql.*;

import Util.ConnectionUtil;

public class SocialMediaDAO {

    public Account createAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        boolean usernameExists = false;

        // Check if username already in database
        try {

            String sql = "select * from account where username = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next() == true)
                usernameExists = true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            if (account.getUsername().length() != 0 && account.getPassword().length() > 3 && !usernameExists) {

                // insert account into database
                String sql = "insert into account (username, password) values (?, ?);";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, account.getUsername());
                preparedStatement.setString(2, account.getPassword());
                preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                // Retrieve the generated key
                if (rs.next()) {

                    int accountId = rs.getInt("account_id");
                    account.setAccount_id(accountId);
                }
                return account;

            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public Account login(Account account) {
        Connection connection = ConnectionUtil.getConnection();

        try {

            String sql = "select * from account where username = ? and password = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {

                int accountId = rs.getInt("account_id");
                account.setAccount_id(accountId);
                return account;
            } else
                return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            if (message.getMessage_text().length() != 0 && message.getMessage_text().length() < 256) {
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
            } else
                return null;

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
            Message oldmessage = retrieveMessageById(message.getMessage_id());
            if (message.getMessage_text().length() > 0 && message.getMessage_text().length() < 256
                    && oldmessage != null) {
                String sql = "UPDATE message SET message_text = ? WHERE message_id = ?;";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, message.getMessage_text());
                preparedStatement.setInt(2, message.getMessage_id());
                preparedStatement.executeUpdate();
                oldmessage.setMessage_text(message.message_text);
                return oldmessage;
            } else
                return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
