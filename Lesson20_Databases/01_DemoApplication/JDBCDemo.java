/*
Copyright Ann Barcomb and Emily Marasco, 2021-2023
Licensed under GPL v3
See LICENSE.txt for more information.
*/

package edu.ucalgary.oop;

import java.sql.*;

public class JDBCDemo{

    private Connection dbConnect;
    private ResultSet results;
            
    public JDBCDemo(){
    }
    
    public void createConnection(){
                
        try{
            dbConnect = DriverManager.getConnection("jdbc:mysql://localhost/pets", "Marasco", "ensf");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String selectCats(){

        StringBuffer catsAndOwners = new StringBuffer();
        
        try {                    
            Statement myStmt = dbConnect.createStatement();
            results = myStmt.executeQuery("SELECT * FROM cats");
            
            while (results.next()){
                System.out.println("Print results: " + results.getString("name") + ", " + results.getString("owner"));
                
                catsAndOwners.append(results.getString("name") + ", " + results.getString("owner"));
                catsAndOwners.append('\n');
            }
            
            myStmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return catsAndOwners.toString();
    }    
    
    public void insertNewCat(String name, String owner, String birthdate){

        try {
            
            String query = "INSERT INTO cats (name, owner, birth) VALUES (?,?,?)";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);
            
            myStmt.setString(1, name);
            myStmt.setString(2, owner);
            myStmt.setString(3, birthdate);
            
            int rowCount = myStmt.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            
            myStmt.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }    

    public void deleteCat(String name){
                    
        try {
            String query = "DELETE FROM cats WHERE name = ?";
            PreparedStatement myStmt = dbConnect.prepareStatement(query);

            myStmt.setString(1, name);
                        
            int rowCount = myStmt.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            
            myStmt.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }    

    public void close() {
        try {
            results.close();
            dbConnect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {

        JDBCDemo myJDBC = new JDBCDemo();
 
        myJDBC.createConnection();
        
        String allCats = myJDBC.selectCats();
        System.out.println("Here is a list of cats and owners: ");
        System.out.println(allCats);
        
        myJDBC.insertNewCat("Cookie","Casey","2013-11-13");   
        myJDBC.selectCats();        
        myJDBC.deleteCat("Cookie");
        myJDBC.selectCats();
        
        myJDBC.close();        
    }
}
