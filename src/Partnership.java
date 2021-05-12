import java.io.*;
import java.util.*;
import java.sql.*;

// Class to run our program
public class Partnership {
    
    //current system user
    static String sessionUser;
    static Scanner sc = new Scanner(System.in);
 
     //main method to run program
    public static void main(String[] args) throws IOException {
        //THIS PART IMPORTANT SO WE DON'T DELETE AND REMAKE EMPTY TABLES EACH TIME PROGRAM IS RUN
        // fills the User table with each User's name password and username we extract from text file.
        if(Trans.findData("select count(username) from user", "count(username)").equals("0")){
            fillTable();
            authenticateUser();
          }
        else 
            authenticateUser();
        
    }

    //method to login and authenticate the identity of users of system by matching distinct userName 
    //to password, and going to the appropriate menu screen (Selected or unselected)
    public static void authenticateUser(){
        System.out.println("Welcome!!");
        System.out.print("Enter your username: ");
        String username = sc.nextLine();
        System.out.print("Enter your password: ");
        String pw = sc.nextLine();                                
        if (Trans.login(username, pw) > 0) {
				System.out.println("login successful");
            sessionUser = username;
            String status=Trans.findData("select selected from user where username='"+username+"'", "selected");
            if(status.equals("selected"))
                selectedView();
            else
                unselectedView();
         }
         
        else {
            System.out.println("wrong password");
            authenticateUser();
        }
    }          

         
    //for unselected users to choose if they want to send an invitation, view an invitation,
    // or log out. Uses their selected status and # of invites to control what options they are
    // allowed to choose (what they can do)     
    public static void unselectedView(){
        System.out.println("Please select what you would like to do (choose by number):");
        System.out.println("1. Send Invites\n2. View & Accept Invitations\n3. Log out");
        String input=sc.next();
        switch(input) {
            //Send Invitations
            case "1":
           //check to make sure users aren't allowed to send more than 5 invitations, we check invites
                if (Integer.parseInt(Trans.findData("select invites from user where username = '"+sessionUser+"' ", "Invites"))==0) {
                    System.out.println("You already sent out your 5 invitations. No more to send.");
                    unselectedView();
                }
//need to add condition so we don't go into invitation sending when no valid unselected users available
                String name = Trans.findData("select name from user where selected='unselected' and username not in (select invited from invites where inviter='"+sessionUser+"') and username!='"+sessionUser+"' ", "name");
                if(name==null) {
                    System.out.println("There are no other Unselected Users left to send invitations to.");
                    unselectedView();
                } 
                //if these conditions pass we send invitation
                else
                    sendInvite();
                break;     
            case "2":
                viewInvites();
                break;
            case "3":
                System.out.println("User successfully logged out.");
                System.exit(0);
                break;
            //in case wrong input entered by user output message and go back to menu
            default:
                System.out.println("Invalid option (please select (1) (2) or (3))");
                unselectedView();
                break;                
        }
    }
    
    //for partnered up (selected) users to be able to see their partner's name upon login
    public static void selectedView(){
        String name=Trans.findData("select name from user where username='"+sessionUser+"'", "name");
        System.out.println("Welcome "+ name);
        String partnerName = Trans.findData("select name from user where username=(select partner from user where username='"+sessionUser+"')", "name");
        System.out.println("Your partner is: "+partnerName);
        System.out.print("Enter 1 to log out: ");
        String choice=sc.next();
        if(choice.equals("1")) {
        	 System.out.println("User successfully logout from the system.");
            System.exit(0);
        }
        else {
            System.out.println("Invalid option");
            selectedView();
        }
    }
    
    public static void sendInvite(){
        System.out.println("Select a user to send an invitation to:");
        ArrayList<String> group=Trans.fetch("select name from user where selected='unselected' and username not in (select invited from invites where inviter='"+sessionUser+"') and username!='"+sessionUser+"' ", "name");
        System.out.println("-1.  Return to Main Menu");
        for(int i=0;i<group.size();i++){
            System.out.println(i+". "+group.get(i));
        }        
        System.out.print("Your choice: ");
        String choice=sc.next();
        while(verifyInput(choice)==false){
            System.out.println("Enter the correct input: ");
            choice=sc.next();
        }
        //in case users want to exit without sending invite/selected wrong menu
        if (Integer.parseInt(choice) == -1)
            unselectedView();
        if(Integer.parseInt(choice)>=0 && Integer.parseInt(choice)<=group.size()-1) {
            String name=group.get(Integer.parseInt(choice));
            String username=Trans.findData("select username from user where name='"+name+"'", "username");
            if(Trans.insert("insert into invites (inviter,invited) values ('"+sessionUser+"','"+username+"')")){
                Trans.Update("update user set invites = invites-1 where username ='"+sessionUser+"'");
                System.out.println("Invite sent to "+name);
                unselectedView();
            }
        }
        else {
            System.out.println("Enter the correct user");
            sendInvite();
        }
    }
    
    public static void viewInvites(){
        System.out.println("Select a user to accept invitation from (choose by number):");
        String count=Trans.findData("select count(invited) from invites where invited='"+sessionUser+"'", "count(invited)");
        if(count.equals("0")){
            System.out.println("You do not have any invites. Please select 1 or 3 : ");
            unselectedView();
        }
        else{
            ArrayList<String> inviters=Trans.fetch("select name from user where selected = 'unselected' and username in (select inviter from invites where invited='"+sessionUser+"')", "name");
            System.out.println("-1. Return to Main Menu");
            for(int i=0;i<inviters.size();i++){
                System.out.println(i+". "+inviters.get(i));
            }
            System.out.print("Your choice: ");
            String choice=sc.next();
            while(verifyInput(choice)==false){
                System.out.println("Enter the correct input: ");
                choice=sc.next();
            }
     //to give users an option to escape to main menu(and ultimately log off) without accepting 
     //invite in case they accidentally selected accept invite menu
            if (Integer.parseInt(choice) == -1)
                unselectedView();

            if(Integer.parseInt(choice)>=0 && Integer.parseInt(choice)<=inviters.size()-1){
                //check to verify user intended to enter input (choice) that they entered:
                System.out.println("Are you sure you intended to choose "  + inviters.get(Integer.parseInt(choice)) +"? If NO press 'N' , if Yes press 'Y'  : ");
                String conf = sc.next();
                if (conf.compareTo("N") == 0 || conf.compareTo("n") == 0) 
                    viewInvites();
                else {     
                    String inviterName=inviters.get(Integer.parseInt(choice));
                    String inviter=Trans.findData("select username from user where name='"+inviterName+"'", "username");
                   //remove any invitations from table once a user becomes selected               
                    Trans.Update("delete from invites where inviter = '"+inviter+"'");
                    Trans.Update("delete from invites where inviter = '"+sessionUser+"'");    
                    if(Trans.Update("update user set partner='"+inviter+"',selected='selected' where username='"+sessionUser+"'")==true && Trans.Update("update user set partner='"+sessionUser+"', selected='selected' where username='"+inviter+"'")==true)  {
                        System.out.println("Invite accepted from " + inviterName);
                        selectedView();
                    }
                    else {
                        System.out.println("Error encountered");
                        viewInvites();
                   }
               }
            }    
            else {
                System.out.println("Enter the correct user");
                viewInvites();
            }
       }
    }
    
    //used to make sure the user input is numeric for user input verification     
     public static boolean verifyInput(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
 // fills the User table with names, usernames(distinct), and passwords by calling Extraction.getData()
    //   which extracts that data from data.txt input file they are saved in.
    public static void fillTable() throws IOException{
          ArrayList<User> users =new ArrayList();
          ArrayList<ArrayList> info=new ArrayList<>();
          info=Extraction.getData();
          for(int i=0;i<info.size();i++){
              ArrayList<String> user=info.get(i);
              String name=user.get(0);
              String username=user.get(1);
              String password=user.get(2);
              User newUser=new User(username,name,password);
              users.add(newUser);
           }
          for(int i=0;i<users.size();i++){
              User user=users.get(i);
              int count=0;
              if(Trans.insert("insert into user (Name,Username,Password,Selected) values ('"+user.getUserName()+"','"+user.getName()+"','"+user.getPassword()+"','"+user.getSelected()+"')"))
                  count++;
          }

       }

}   
      
              




