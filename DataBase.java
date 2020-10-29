import java.io.IOException;
import java.io.*;
import java.util.Scanner;

public class DataBase {
    private DataBaseRec[] database;
    private Index firstNameIndex;
    private Index lastNameIndex;
    private Index IDIndex;
    private DeletedLocation deletedLocation;
    int numberOfRecords;

    public DataBase() {
        database = new DataBaseRec[200];
        firstNameIndex = new Index();
        lastNameIndex = new Index();
        IDIndex = new Index();
        deletedLocation = new DeletedLocation();
        numberOfRecords = 0;
        try{
            fillDataStructure();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public DataBase(int sizeOfDatabase) {
        database = new DataBaseRec[sizeOfDatabase];
        firstNameIndex = new Index(sizeOfDatabase);
        lastNameIndex = new Index(sizeOfDatabase);
        IDIndex = new Index(sizeOfDatabase);
        deletedLocation = new DeletedLocation(sizeOfDatabase);
        numberOfRecords = 0;
        try{
            fillDataStructure();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Index getFirstNameIndex() {
        return firstNameIndex;
    }

    public Index getLastNameIndex() {
        return lastNameIndex;
    }

    public Index getIDIndex() {
        return IDIndex;
    }

    public DeletedLocation getDeletedLocation() {
        return deletedLocation;
    }

    public DataBaseRec getDatabaseRec(int indexOfRecordToReturn) {
        return database[indexOfRecordToReturn];
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }
    // Finds and deletes a record with a matching ID
    // @return Index of deleted record or -1 if not found

    public int deleteRecord(String deleteID) {
        // Format ID 
        deleteID = deleteID.trim();
        deleteID = String.format("%05d", Integer.parseInt(deleteID));

        int index = this.find(deleteID);
        boolean found = (index >= 0);

        if (found) {
            // Add index to deleted Index stack
            deletedLocation.addIndex(index);

            // Remove from Ordered Indexes
            firstNameIndex.deleteRecord(index);
            lastNameIndex.deleteRecord(index);
            IDIndex.deleteRecord(index);

            numberOfRecords--;
        }

        return index;
    }

    /**
     * Dumps the entire database to the console with indices. For debugging
     * purposes only.
     */
    public void print() {
        int i = 0;
        while (database[i] != null) {
            System.out.println("[" + i + "] " + database[i].toString());
            i++;
        }
    }

    /**
     * Prints out the database in a specified order of a specified field
     * 
     *  for the parameter field:
     *  1, 2, or 3 corresponds to first name, last name, and ID
     *  respectively
     *  for the parameter order:
     *  1 or 2 corresponds to increasing and decreasing order
     *  respectively
     *  This consolidates a lot of repetitive code
     */
    public void listIt(int field, int order) {
        Index indexToPrint = null;

        // Determine which ordered Index to list
        switch (field) {
            case 1:
            indexToPrint = firstNameIndex;
            break;
            case 2:
            indexToPrint = lastNameIndex;
            break;
            case 3:
            indexToPrint = IDIndex;
            break;
            default:
            System.err.println("Invalid field input for listIt");
            break;
        }

        // Determine which order to list it in
        if (indexToPrint.getNumberOfRecords() == 0) {
            System.out.println("Database is empty.\n");
        } else if (order == 1) {
            // increasing order
            for (int i = 0; i < indexToPrint.getNumberOfRecords(); i++) {
                System.out.println(database[indexToPrint.getIndexRec(i)
                        .getDatabaseIndex()].toString());
            }
            System.out.println();
        } else if (order == 2) {
            // decreasing order
            for (int i = indexToPrint.getNumberOfRecords() - 1; i >= 0; i--) {
                System.out.println(database[indexToPrint.getIndexRec(i)
                        .getDatabaseIndex()].toString());
            }
            System.out.println();
        } else
            System.err.println("Invalid order input for listIt");
    }

    // Search for IDs using binary search

    public boolean search(String tempID) {
        // Format ID 
        tempID = tempID.trim();
        tempID = String.format("%05d", Integer.parseInt(tempID));

        // Organizes it to be used in binary search
        int lo = 0;
        int hi = IDIndex.getNumberOfRecords() - 1;
        int mid;
        boolean found;

        // Binary search
        while (lo <= hi) {
            mid = (hi + lo) / 2;
            found = (IDIndex.getIndexRec(mid).getData().compareTo(tempID) == 0);

            if (found)
                return true;
            else if (IDIndex.getIndexRec(mid).getData().compareTo(tempID) > 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return false; // Not found
    }

    // Locate record by ID
    // @return Index of the ID in the database or -1 if not found.

    public int find(String tempID) {
        // Format ID
        tempID = tempID.trim();
        tempID = String.format("%05d", Integer.parseInt(tempID));

        // Organized it to be used in Binary Search
        int lo = 0;
        int hi = IDIndex.getNumberOfRecords() - 1;
        int mid;
        boolean found;

        // Binary search
        while (lo <= hi) {
            mid = (hi + lo) / 2;
            found = (IDIndex.getIndexRec(mid).getData().compareTo(tempID) == 0);

            if (found)
                return IDIndex.getIndexRec(mid).getDatabaseIndex();
            else if (IDIndex.getIndexRec(mid).getData().compareTo(tempID) > 0) {
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return -1; // Not found
    }

    //Using parameters for data, add a record to database and check to see if ID is unique
    //if not unique, prompt user to enter a new ID

    public void insert(String firstName, String lastName, String tempID) {
        // Trim whitespace
        firstName = firstName.trim();
        lastName = lastName.trim();
        tempID = tempID.trim();

        tempID = String.format("%05d", Integer.parseInt(tempID));

        // Check if ID is in use
        while (search(tempID)) {
            @SuppressWarnings("resource")
            Scanner keyboard = new Scanner(System.in);
            System.out.print("The ID " + tempID
                + " is already in use.\nPlease enter a unique ID: ");
            tempID = keyboard.nextLine();
            System.out.println();

            // Format ID 
            tempID = tempID.trim();
            tempID = String.format("%06d", Integer.parseInt(tempID));

        }

        DataBaseRec record = new DataBaseRec(firstName,lastName, tempID);
        int indexToInsertAt;

        // Use index from deletedLocation for next record location if available
        if (deletedLocation.isEmpty())
            indexToInsertAt = numberOfRecords;
        else
            indexToInsertAt = deletedLocation.getIndex();

        // Add record to database and ordered indecies
        database[indexToInsertAt] = record;
        firstNameIndex.addRecord(new IndexRec(firstName, indexToInsertAt));
        lastNameIndex.addRecord(new IndexRec(lastName, indexToInsertAt));
        IDIndex.addRecord(new IndexRec(tempID, indexToInsertAt));

        numberOfRecords++;
    }

    public void addIt() {
        String name1, name2, tempID;
        boolean found;
        @SuppressWarnings("resource")
        Scanner keyboard = new Scanner(System.in);

        do {
            System.out.print("Enter a unique ID number to add: ");
            tempID = keyboard.nextLine();

            // is it unique ?
            found = search(tempID);
            if (found) {
                System.out.println("ID already in use");
                System.out.println("Please re-enter a unique ID.");
            }
        } while (found);

        // unique ID found. Ask for first and last name

        System.out.print("Enter first name: ");
        name1 = keyboard.nextLine();
        System.out.print("Enter last name: ");
        name2 = keyboard.nextLine();

        // add to our data structure
        insert(name1, name2, tempID);
    }

    //Delete student method. Asks for a ID, checks to see if the ID exists. 

    public void deleteIt() {
        String deleteID;
        @SuppressWarnings("resource")
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Please enter the ID to be deleted: ");
        deleteID = keyboard.next();

        if (deleteRecord(deleteID) == -1)
            System.out.println("The ID you entered does not exist.");
        else {
            System.out.println("Record has been deleted. Thank you.\n");
        }

    }

    //Find a studnet, asks for ID and checks to see if it exists
    public void findIt() {
        String findID;
        @SuppressWarnings("resource")
        Scanner keyboard = new Scanner(System.in);

        System.out.print("Please enter the ID to be found: ");
        findID = keyboard.next();

        int indexOfRecord = find(findID);

        if (indexOfRecord == -1)
            System.out.println("The ID you entered does not exist.");
        else
            System.out.println(getDatabaseRec(indexOfRecord).toString());

    }
    //Cases 4-9
    //This method consolidates code, and prevents repetitive coding. It does this
    // by assigning the field (name,last,orId) to a number and order (Ascending/Descending) to another number
    // By passing them as parameters. The methods below call the same method in the DataBase class
    // fieldNum=1 first name
    // fieldNum=2 last name
    // fieldNum=3 ID
    // orderNum=1 increasing
    // orderNum=2 decreasing

    public void ListByIDAscending() {
        listIt(3, 1);
    }

    public void ListByFirstAscending() {
        listIt(1, 1);
    }

    public void ListByLastAscending() {
        listIt(2, 1);
    }

    public void ListByIDDescending() {
        listIt(3, 2);
    }

    public void ListByFirstDescending() {
        listIt(1, 2);
    }

    public void ListByLastDescending() {
        listIt(2, 2);
    }

    //This method fills the DataBase with records from the file. ("database.txt"). What it does
    // is it reads them all in as strings and separates them by cutting the white space
    // then it fills it into an array with three indeces to hold first name, last and ID
    public void fillDataStructure()
    throws IOException {
        // Set up
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("database.txt")));

        String currentLine;

        // Iterate through file line by line
        while ((currentLine = reader.readLine()) != null) {
            // Trim extra whitespace off currentLine
            currentLine = currentLine.trim();

            // Split currentLine into multiple Strings at whitespace
            String[] parts = currentLine.split("\\s+");

            // Add record to dataBase
            insert(parts[1].trim(), parts[0].trim(),parts[2].trim());
        }

        reader.close();
    } 
}

