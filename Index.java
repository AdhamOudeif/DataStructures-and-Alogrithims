//Index Record to create ordered arrays
public class Index {
    private int numberOfRecords;
    private IndexRec[] orderedArrayIndex;

    public Index() {
        numberOfRecords = 0;
        orderedArrayIndex = new IndexRec[200];
    }

    public Index(int sizeOfIndex) {
        numberOfRecords = 0;
        orderedArrayIndex = new IndexRec[sizeOfIndex];
    }

    public IndexRec getIndexRec(int indexOfRecordToReturn) {
        return orderedArrayIndex[indexOfRecordToReturn];
    }

    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    public void print() {
        for (int i = 0; i < numberOfRecords; i++)
            System.out.println(orderedArrayIndex[i].toString());
    }

    // Add an Index record to the ordered Index
    public void addRecord(IndexRec recordToAdd) {
        // Add record to end of ordered Index
        orderedArrayIndex[numberOfRecords] = recordToAdd;

        IndexRec temp; // For swapping
        boolean recordInCorrectLocation;

        // Sort recordToAdd into appropriate place in OrderedIndex using
        // Insertion algorithm
        for (int currentIndex = numberOfRecords - 1; currentIndex >= 0; currentIndex--) {
            recordInCorrectLocation = (recordToAdd.getData().compareTo(
                    orderedArrayIndex[currentIndex].getData()) > 0);

            if (recordInCorrectLocation)
                break;
            else {
                // Swap recordToAdd up a spot in the OrderedIndex
                temp = orderedArrayIndex[currentIndex];
                orderedArrayIndex[currentIndex] = orderedArrayIndex[currentIndex + 1];
                orderedArrayIndex[currentIndex + 1] = temp;
            }
        }

        numberOfRecords++;
    }

    // Search for and delete record in an OrderedIndex

    public void deleteRecord(int databaseIndexToDelete) {
        int iterator;

        // Linear search to find the proper record to delete
        for (iterator = 0; iterator < numberOfRecords; iterator++) {
            if (this.getIndexRec(iterator).getDatabaseIndex() == databaseIndexToDelete)
                break;
        }

        // Shift all the records after the record to delete up one space,
        // overwriting the deleted record.
        while (iterator < numberOfRecords) {
            this.orderedArrayIndex[iterator] = this.getIndexRec(iterator + 1);
            iterator++;
        }

        numberOfRecords--;
    }
}