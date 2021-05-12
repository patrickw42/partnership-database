import java.sql.*;;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class Trans {
    static String url = "jdbc:mysql://localhost:3306/partnership?useSSL=false";
    static String dbUsername = "root";
    static String pw = "password";
 
 
 
 
    public static int login(String user, String pass) {
        String databaseUsername = "";
        Connection con = null;
        Statement stmt = null;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(url, dbUsername, pw); 
            stmt = con.createStatement();
            String SQL = "SELECT * FROM user WHERE username='" + user + "' && password ='" + pass+ "'";
            rs = stmt.executeQuery(SQL);
            while (rs.next()) {
                databaseUsername = rs.getString("username");
            }   
            if (databaseUsername != "") 
                return 1;
            else 
                return 0; 
        }          
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return 0;
  }



    public static boolean insert(String sql){
        Connection con = null;
        Statement s = null;
        try {
            con = DriverManager.getConnection(url, dbUsername, pw);
            s = con.prepareStatement(sql);
            s.execute(sql);
            return true;
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return false;
        }
        finally {
            try {
                if(s!=null)
                    s.close();
                if(con!=null)
                    con.close();
             }
            catch(SQLException e) {
                JOptionPane.showMessageDialog(null, e); 
            }
      } 
  }    


    public static boolean Update(String sql){
        Connection con = null;
        Statement s = null;
        try {
            con = DriverManager.getConnection(url, dbUsername, pw);
            s = con.prepareStatement(sql);
            s.execute(sql);
            return true;
         }
         catch(Exception e) {
             System.out.println(e);
             return false;
         }
         finally {
             try {
                 if(s!=null)
                     s.close();
                 if(con!=null)
                     con.close();
              }
             catch(Exception e) {
                 JOptionPane.showMessageDialog(null, e); 
              }
          }
    }  
 
  
    public static String findData(String sql,String columName){
        Connection con = null;
        Statement s = null;
        try{
            con = DriverManager.getConnection(url, dbUsername, pw); 
            s = con.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);
            String col=null;
            while(rs.next()){
               col = rs.getString(columName);
            }
        return col;
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
        finally {
            try {
                if(s!=null)
                    s.close();
                if(con!=null)
                    con.close();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, e); 
            }
        }
    }
       
       
       
   
   //returns the result of sql query based on sql string and columnName passed in. returns multiple pieces of data as 
   // members of an ArrayList. returns list of unselected users to send invites to 
    public static ArrayList fetch(String sql,String columName){
        Connection con = null;
        Statement s = null;
        ArrayList<String> data=new ArrayList<>();
        try {
            con = DriverManager.getConnection(url, dbUsername, pw); 
            s = con.prepareStatement(sql);
            ResultSet rs = s.executeQuery(sql);
            String col=null;
            while(rs.next()){
                col = rs.getString(columName);
                data.add(col);
            }
            return data;
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    finally {
        try {
            if(s!=null)
                s.close();
            if(con!=null)
                con.close();
         }
         catch(Exception e) {
             JOptionPane.showMessageDialog(null, e); 
         }
      }
   } 
}
