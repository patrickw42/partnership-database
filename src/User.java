//don't need setters because only need to populate the table the first time it is used. Extracting User info from text file
// and getting it into the table.  From there we just re-set the User's invites, partner & selected in the table as needed
// constructor sets name, username and password (from text file) and invites and selected are already initialized.
public class User {
    private String name,userName,password,selected = "unselected";
    private int invites = 5;
    
    public User(String name,String userName,String pw){
        this.name=name;
        this.userName=userName;
        this.password=pw;
    }
    
    public String getName(){
        return this.name;
    }
   
    public String getUserName() {
        return this.userName;
    }
   
    public String getPassword() {
        return this.password;
    }
     
    public String getSelected() {
        return this.selected;
    }
  
    public int getInvites() {
        return this.invites;
    }
  
  }

