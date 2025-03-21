package DAO;

import Model.Account;
import Model.Message;

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
            if (message.getMessage_text().length() != 0 && message.getMessage_text().length()<256){
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
            } 
            else
                return null;
            

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;

    }

}
