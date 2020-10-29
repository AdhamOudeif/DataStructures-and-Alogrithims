
//DataBase Record gives a method to fill array of strings like this - |"first"|"last"|"index"| 
public class DataBaseRec {
    private String firstName;
    private String lastName;
    private String ID;

    public DataBaseRec() {
        firstName = "";
        lastName = "";
        ID = "";
    }

    public DataBaseRec(String firstName, String lastName, String ID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + ID;
    }
}