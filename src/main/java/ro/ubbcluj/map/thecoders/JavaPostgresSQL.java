package ro.ubbcluj.map.thecoders;

import ro.ubbcluj.map.thecoders.domain.User;

import java.sql.*;

public class JavaPostgresSQL {

    public static boolean findIntoDatabase(String userName, String userPassword){
        String username = "postgres";
        String password = "1234";
        String url = "jdbc:postgresql://localhost:5432/academic";

        String name = userName;
        String pass = userPassword;

        String sql = "SELECT * FROM users WHERE user_name = " + name + " AND password = " + pass;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                System.out.println("User: " +name + " password: " + pass );
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void writeToDatabase(String firstname, String lastname, String userName, String userpassword){
        String username = "postgres";
        String password = "1234";
        String url = "jdbc:postgresql://localhost:5432/academic";

        String sql = "INSERT INTO users(first_name,last_name,user_name,password) VALUES (?,?,?,?)";
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setString(1,firstname);
            ps.setString(2,lastname);
            ps.setString(3,userName);
            ps.setString(4,userpassword);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
