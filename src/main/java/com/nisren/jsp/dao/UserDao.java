package com.nisren.jsp.dao;

import com.nisren.jsp.bean.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private String jdbcUrl = "jdbc:mysql://localhost:3306/userdb?useSSL=false";
    private String jdbcUserName = "root";
    private String jdbcPassword = "2023";
    private String jdbcDriver = "com.mysql.jdbc.Driver";

    private static final String INSERT_USER_SQL = "INSERT INTO users "+" (name, email, country) VALUES "+" (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where  id =?;";
    private static final String UPDATE_USER_SQL = "update users set name=?,email=?,country=? where id =?;";

    public UserDao(){

    }

    protected Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(jdbcDriver);
            connection = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
        } catch (SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return connection;

    }

    //Insert User
    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USER_SQL);
        try(Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);
            //it will return nothing so executeUpdate() is used
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }

    //Select User by Id
    public User selectUserById(int id){
        User user = null;
        //Setting Connection
        try (Connection connection = getConnection();
        //Ceate Statement Using connaction object
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);    ){
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            //Execute query using executeQuery() and as we will get user object back hold it in ResultSet object
            ResultSet resultSet = preparedStatement.executeQuery();
            //process serultset object
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(id, name, email, country);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    //Select all Users
    public List<User> selectAllUsers(){
        List<User> users = new ArrayList<User>();
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);){
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    //Update Users
    public boolean updateUser( User user) throws SQLException{
        boolean rowUpdated;
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL); ){
            System.out.println(preparedStatement);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.setInt(4, user.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        }
        return  rowUpdated;
    }


    //Delete Users
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);){
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

}
