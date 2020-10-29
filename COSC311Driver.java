/* this program is a DataBase where many operations can be performed. It reads in a list of First and Last names, as well as an ID number and adds it to the record. Then 
the user is able to manipulate it with the following commands below */
import java.util.*;
import java.io.IOException;

public class COSC311Driver
{

    public static void main(String[] args)
    {

        DataBase d=new DataBase(); 
        int response;
        Scanner keyboard=new Scanner(System.in);

        do
        {
            System.out.println(" 1 Add a new student");
            System.out.println(" 2 Delete a student");
            System.out.println(" 3 Find a student by ID");
            System.out.println(" 4 List students by ID increasing");
            System.out.println(" 5 List students by first name increasing");
            System.out.println(" 6 List students by last name increasing");
            System.out.println(" 7 List students by ID decreasing");
            System.out.println(" 8 List students by first name decreasing");
            System.out.println(" 9 List students by last name decreasing");
            System.out.println(" ");
            System.out.println(" 0 End");

            response=keyboard.nextInt();

            switch (response)
            {
                case 1: d.addIt();  //Note: if the user enters an ID already in use, issue a warning and return to the menu
                break;
                case 2: d.deleteIt();   //Note: output either "Deleted" or "ID not Found" and return to menu
                break;
                case 3: d.findIt(); //Note: output the entire record or the message "ID not Found" and return to menu
                break;
                case 4: d.ListByIDAscending();      
                break;
                case 5: d.ListByFirstAscending();   
                break;
                case 6: d.ListByLastAscending();
                break;
                case 7: d.ListByIDDescending();
                break;
                case 8: d.ListByFirstDescending();
                break;
                case 9: d.ListByLastDescending();
                break; 
                default:                
            }

        } while (response!=0);

    }
}
 
