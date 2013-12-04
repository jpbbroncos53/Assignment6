/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package worlddata;

/**
 *
 * @author JACOB
 */

import java.sql.*;
import java.io.*;
import java.text.NumberFormat;


public class DataAccess {
    private Connection conn;
    private BufferedWriter log;
    
    
    public DataAccess (Connection c, BufferedWriter w)throws IOException {
        log=w;
        conn=c;
    }
    public void RetrieveData(String trans)throws IOException {
         try
        {
            //create a Statement object
            Statement stmt = conn.createStatement();
            //Send the statement to the DBMS
            ResultSet rs = stmt.executeQuery(trans);
             ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
           
            while (rs.next())
            {
                for(int i=1;i<=numberOfColumns;i++){
                    log.write(rs.getString(i)+" ");
                }
                log.write("\n");
            }
            stmt.close();
        }
        catch (Exception ex)
        {
            log.write("\r\nERROR QUERY not done\r\n");
            log.write(ex.toString() + "\r\n");
            
        }
         
        
        
    }
    public void ChangeData(String trans, String type)throws IOException{
        try
        {
            Statement stmt = conn.createStatement();
            int result = stmt.executeUpdate(trans);
            if (result != 0)
            {
                log.write(type+" SUCCESSFUL\n");
            }
        }
        catch (Exception ex)
        {
            log.write("\r\nERROR "+type+" not done\r\n");
            log.write(ex.toString() + "\r\n");
            
        }
    }
}
