/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.module03_basicgui_db_interface.db;


//added

import com.example.module03_basicgui_db_interface.Person;
import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 *
 * @author MoaathAlrajab
 */
public class ConnDbOps {
    final String MYSQL_SERVER_URL = "jdbc:mysql://csc311papazisserver.mysql.database.azure.com/";
    final String DB_URL = "jdbc:mysql://csc311papazisserver.mysql.database.azure.com/DBname";
    final String USERNAME = "papazisAdmin";
    final String PASSWORD = "farmingdale24*";
    
    public  boolean connectToDatabase() {
        boolean hasRegistredUsers = false;


        //Class.forName("com.mysql.jdbc.Driver");
        try {
            //First, connect to MYSQL server and create the database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS DBname");
            statement.close();
            conn.close();

            //Second, connect to the database and create the table "users2" if cot created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users2 ("
                    + "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "firstName VARCHAR(200) NOT NULL,"
                    + "lastName VARCHAR(200) NOT NULL UNIQUE,"
                    + "department VARCHAR(200),"
                    + "major VARCHAR(200),"
                    + "course VARCHAR(200) NOT NULL"
                    + ")";
            statement.executeUpdate(sql);

            //check if we have users in the table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users2");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegistredUsers = true;
                }
            }

            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasRegistredUsers;
    }

    public  void queryUserById(int id) {


        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM users2 WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            /*while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phone + ", Address: " + address);
            }*/

            while (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String department = resultSet.getString("department");
                String major = resultSet.getString("major");
                String course = resultSet.getString("course");
                System.out.println("ID:" + id + "FirstName: " + firstName + ", LastName: " + lastName + ", Department: " + department + ", Major: " + major + ", Course: " + course);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //fills observable list called data with the data from users2 table
    public  void listAllUsers() {

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM users2 ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String department = resultSet.getString("department");
                String major = resultSet.getString("major");
                String course = resultSet.getString("course");
                //System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phone + ", Address: " + address);

            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //returns a list of data that contains the data from the database
    public List<Person> getData() {
        List<Person> dataList = new ArrayList<Person>(); //stores the database data
        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM users2 ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            //iterate through database and add rows to dataList
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String department = resultSet.getString("department");
                String major = resultSet.getString("major");
                String course = resultSet.getString("course");
                dataList.add(new Person(id, firstName, lastName, department, major, course));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dataList; //return the list that has the database rows

    }

    //adds new row to database
    public void insertUser(int id, String firstName, String lastName, String department, String major, String course) {

        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO users2 (id, firstName, lastName, department, major, course) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, department);
            preparedStatement.setString(5, major);
            preparedStatement.setString(6, course);

            int row = preparedStatement.executeUpdate();

            if (row > 0) {
                System.out.println("A new user was inserted successfully.");
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "DELETE FROM users2 WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            int row = preparedStatement.executeUpdate();

            //check if the row got deleted successfully
            if(row > 0) {
                System.out.println("User deleted successfully");

                sql = "UPDATE users2 SET id = id - 1 WHERE id > ?";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
                System.out.println("IDs decremented successfully");
            }


        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void updateUser(int id, String firstName, String lastName, String department, String major, String course) {
        try{
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "UPDATE users2 SET firstName = ?, lastName = ?, department = ?, major = ?, course = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, department);
            preparedStatement.setString(4, major);
            preparedStatement.setString(5, course);
            preparedStatement.setInt(6, id);

            int row = preparedStatement.executeUpdate();

            if(row > 0){
                System.out.println("User updated successfully");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }




} //end of class
