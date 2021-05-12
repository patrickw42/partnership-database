import java.sql.*;
import java.io.*;
import java.util.*;

//used to extract the user's info from the text file to load into the system in order to populate our tables with the User's 
// name, username(distinct key), and password.
public class Extraction {
    static ArrayList<ArrayList> data =new ArrayList<ArrayList>();
    
    private static String [] extractFile(String filename) throws IOException {
        BufferedReader b=null;
        try {
           b=new BufferedReader(new FileReader(filename)); 
           StringBuilder s=new StringBuilder();
           String row=b.readLine();
           while(row!=null) {
               s.append(row);
               s.append("/n");
               row=b.readLine();
               }
           
           String input=s.toString();
           String [] records=input.split("/n");
           return records;  
           }
        
        
        catch (Exception e){
           e.printStackTrace();
           return null;  
           }
        
        finally {
            b.close();  
            }
        
    }
    
    //returns Arralist where each list contains 1 user's record/data
    public static ArrayList getData()throws IOException {
    
        String file[]=extractFile("C:\\Users\\Pat\\Documents\\CSC 545 - Database Systems\\FinalProject\\data.txt");
        for (int i=0;i<file.length;i++){
            String[] records=file[i].split(",");
             ArrayList<String> entity=new ArrayList<>();
             for (int j=0;j<records.length;j++){
                 entity.add(records[j]);
                }
             data.add(entity);
             }
        return data;       
     }

//to verify if our data was extracted correctly from the file. outputs the arraylist's values that we return from getData 
    public static void main(String[] args) throws IOException{
        getData();
        System.out.println("Data extracted: ");
        for (int i=0;i<data.size();i++){
            System.out.println(data.get(i).toString());
        }
   }     

}     
    
        
    