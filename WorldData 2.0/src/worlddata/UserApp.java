/* PROJECT: UserApp(Java)         PROGRAM: WorldData 2.0
 * AUTHOR:Jacob Potter
 * OOP CLASSES USED:  SataAccess
 *      
 * FILES ACCESSED: 
 *      INPUT:   WorldTrans.txt           
 *      OUTPUT:  Log.txt                
 *      
 * DESCRIPTION:  The program itself is just the CONTROLLER which UTILIZES
 *      the SERVICES (public methods) in DataAccess which works with data 
 *      previously set up in a MYSQL server.
 *     
 *     
 ******************************************************************************/
package worlddata;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

/**
 *
 * @author JACOB
 */
public class UserApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        // TODO code application logic here
        
        String url = "jdbc:mysql://localhost:3306/world";
        String user = "Jacob";
        String password = "jlp1992";
        
        BufferedWriter log=new BufferedWriter(
                    new FileWriter("Log.txt",false));
        
        log.write("Connecting to MySQL...\r\n");

        try
        {
            //Create a connection to the database
            Connection conn = DriverManager.getConnection(url, user, password);
            log.write("OK, the DB Connection is OPENED\r\n");
            
            File filein=new File("WorldTrans.txt");
            Scanner scanner=new Scanner(filein);
            
            
            String aline;
            
            DataAccess data=new DataAccess(conn,log);
            while(scanner.hasNext()){
                aline=scanner.nextLine();
                
                switch(aline.substring(0, 1)){
                    case "S":
                        log.write(aline+"\n");
                        RetrieveData(aline,data);
                        break;
                    case "U":
                        log.write(aline+"\n");
                        UpdateData(aline,data);
                        break;
                    case "I":
                        log.write(aline+"\n");
                        InsertData(aline,data);
                        break;
                    case "D":
                        log.write(aline+"\n");
                        DeleteData(aline,data);
                        break;
                }
                
                log.write("\n");
                
            }

            conn.close();
            System.out.println("See Log.txt in project folder");
        }
        catch (Exception ex)
        {
            log.write("\r\nERROR, DB Connection didn't work - no trans done\r\n");
            log.write(ex.toString());
            System.out.println("ERROR, DB Connection didn't work - no trans done");
        }
        
        log.write("\r\nEXITING PROGRAM");
        log.close();
    }
    
    private static void RetrieveData (String line, DataAccess data)
            throws IOException{
        data.RetrieveData(line);
    }
    
    private static void UpdateData (String line, DataAccess data)
            throws IOException{
        data.ChangeData(line,"UPDATE");
    }

    private static void InsertData(String aline, DataAccess data) 
            throws IOException {
       
            String tranCode = "INSERT";
            String tableName;
            String colNames;
            String dataValues;

            String[] field;
            field = aline.split("\\|");

           tableName = field[0].substring(2, field[0].length() );

            if (field.length == 2)
            {
                colNames = "";
                dataValues = field[1]; 
            }
            else
            {
                colNames = String.format("(%s)", field[1]);
                dataValues = field[2];
            }
            aline=String.format("INSERT INTO %s%s VALUES "
                    + "(%s)", tableName,colNames,dataValues );
            data.ChangeData(aline, tranCode);
                       
    }

    

    private static void DeleteData(String aline, DataAccess data)
            throws IOException {
       
            String tranCode = "DELETE";
            

            String[] field;
            field = aline.split("\\|");
            
            String tableName=field[0].substring(2, field[0].length() );
            String colNames=field[1];
            String dataValues=field[2];
            
            aline=String.format("DELETE FROM %s WHERE %s = %s",
                    tableName, colNames,dataValues);
            data.ChangeData(aline, tranCode);
            
    }
}
    

