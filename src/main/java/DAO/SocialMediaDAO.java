package DAO;

import Model.Account;
//import Model.Message;



import java.sql.*;


import Util.ConnectionUtil;

public class SocialMediaDAO {


     public Account createAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();


        boolean usernameExists = false;
        
        try { 
            // Check if username already in database
            String sql = "select * from Account where username = ?;";
            
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next() == true)
                usernameExists = true;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        

        try { 
            if (account.getUsername().length() != 0 && account.getPassword().length() > 3 && !usernameExists){
           
                //insert account into database
                String sql = "insert into account (username, password) values (?, ?);";
                
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, account.getUsername());
                preparedStatement.setString(2, account.getPassword());
                preparedStatement.executeUpdate();
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    // Retrieve the generated key
                    int generatedId = rs.getInt(1); 
                    account.setAccount_id(generatedId);
                }
                return account;

            }
            else{
                return null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;



    }
}
