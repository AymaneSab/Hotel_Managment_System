package Mysql;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.*;

public class Connexion{
    
     public  static Connection getConnection(){
         
        try {   
             
             Class.forName("com.mysql.cj.jdbc.Driver");
             System.out.println(" Driver Charged Succefully ");
             
             Connection cnx = null ;
             cnx = DriverManager.getConnection("jdbc:mysql://localhost/Hotel" ,"root" , "");    
              
             System.out.println(" Connected Succefully ");
             
             return  cnx; 
        }
        
        catch(ClassNotFoundException | SQLException e){
           System.out.println(e.getMessage());
            return null;
        }
    }   
}
 
