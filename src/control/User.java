package control;

public class User {

    private String userName;
    private String userPassword;
    private String userRol;
    private String userFirstName;
    private String userFamilyName;
    private String userID;

    public User() {
        this.userName = "";
        this.userPassword = "";
        this.userRol = "";
        this.userFirstName = "";
        this.userFamilyName = "";
        this.userID = "";
    }
    
    public User(String userName, String userPassword, String userRol, String userFirstName, String userFamilyName, String userID) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.userRol = userRol;
        this.userFirstName = userFirstName;
        this.userFamilyName = userFamilyName;
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserRol() {
        return userRol;
    }

    public void setUserRol(String userRol) {
        this.userRol = userRol;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserFamilyName() {
        return userFamilyName;
    }

    public void setUserFamilyName(String userFamilyName) {
        this.userFamilyName = userFamilyName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return userName + "&&" + userPassword + "&&" + userRol + "&&" + userFirstName + "&&" + userFamilyName + "&&" + userID;
    }

}
