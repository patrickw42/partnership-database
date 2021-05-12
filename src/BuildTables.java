import java.sql.*;

// BE CAREFUL TO ONLY RUN ONCE AT BEGINING OTHERWISE WILL DELETE ALL TABLES AND REMAKE THEM EACH TIME CALLED
//Include condition that checks if the # of records in user table is 0 before running to make sure we don't already
// have table(s) made
// this will build our 2 tables: User(Username, Password, Name, Selected, Partner, Invites) where Username is PrimaryKey 
// and Invitations (Invited, Inviter) where Inviter and Invited both make up composite primary key of table
// and will reference/contain the distinct Usernames of the users who send and receive each invitation. 
public class BuildTables{
  
    public static void main(String[]args) { 
        String url = "jdbc:mysql://localhost:3306/partnership?useSSL=false";
	     String user = "root";
	     String password = "password";
        

 
    try {
      Class.forName( "com.mysql.cj.jdbc.Driver" );  
		Connection cx = DriverManager.getConnection( url, user, password );

      Statement st = cx.createStatement();
      
      //Creates user table setting the default values for Invites column to 5 and for Selected column to 'unselected' 
     // so any new users added to table will have their selected status as 'unselected' and # of invites they can send as 5
     // also sets the primary key of the table as userNames which we will ensure are distinct  
		String table1 = "user";
 
      String sql_drop = "DROP TABLE IF EXISTS " + table1;

      System.out.println( "\n=> execute: " + sql_drop );
      st.executeUpdate( sql_drop );

      String table_def =
	    "Username VARCHAR(40), Name VARCHAR(25), Password VARCHAR(20), Selected VARCHAR(12) DEFAULT 'unselected', Partner VARCHAR(50), Invites INT DEFAULT 5, PRIMARY KEY (Username)";

      String sql_create = "CREATE TABLE " + table1 + "(" + table_def + ")";

      System.out.println( "\n=> execute: " + sql_create );
      st.executeUpdate( sql_create );
      
      //Create Invites table
      // will set the primary key t be a composite of both columns (Invitee and Invited) which each reference the distinct
      // userId of the user sending and receiving the invitation 
      String table2 = "invites";
      
      String sql_drop2 = "DROP TABLE IF EXISTS " + table2;

      System.out.println( "\n=> execute: " + sql_drop2 );
      st.executeUpdate( sql_drop2 );

      String table_def2=
    		  "Inviter VARCHAR(40), Invited VARCHAR(40), PRIMARY KEY (Inviter, Invited)";
      String sql_create2 = "CREATE TABLE " + table2 + "(" + table_def2 + ")";

      System.out.println( "\n=> execute: " + sql_create2 );
      st.executeUpdate( sql_create2 );
            
      
    
      

     }
    catch( Exception x ) {
      System.err.println( x );
    }
  }
}
