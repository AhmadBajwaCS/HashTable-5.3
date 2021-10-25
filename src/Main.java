import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args){
        HashTable hashTableQP = new HashTable("QP");
        HashTable hashTableLP = new HashTable("LP");
        HashTable hashTableDH = new HashTable("DH");

        File inputFile = createInputFile();         // creates an input file with sample data.

        try {                                                                           // Reads in the file and computes on it.
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String st = reader.readLine();
            String[] strArr;
            while (st!=null){



                strArr = st.split(" ", 15);

                //System.out.println("This is the string: " + st + " and length:  " + strArr.length);

                if(strArr.length<10)
                    break;

                //System.out.println(st);

                for(int j = 0; j<9; j+= 2)
                {
                    hashTableLP.put(Integer.parseInt(strArr[j]),Integer.parseInt(strArr[j+1]) );
                    hashTableQP.put(Integer.parseInt(strArr[j]),Integer.parseInt(strArr[j+1]) );
                    hashTableDH.put(Integer.parseInt(strArr[j]),Integer.parseInt(strArr[j+1]) );
                }
                st = reader.readLine();
            }


        } catch(IOException e){
            System.out.println("Error Occurred");
        }


        /*hashTable.put(123, 10);
        hashTableLP.put(5624, 30);
        hashTableLP.put(5625, 30);
        hashTableLP.put(8691, 40);
        hashTableLP.put(276, 60);
        hashTableLP.put(346, 65);
        hashTableLP.put(20, 70);
        hashTableLP.put(724, 74);
        hashTableLP.put(726, 74);
        hashTableLP.put(729, 74);*/

        System.out.println("\n\nHash Table with Linear Probing: ");
        hashTableLP.printHashTable();

        System.out.println("\n\nHash Table with Quadratic Probing: ");
        hashTableQP.printHashTable();

        System.out.println("\n\nHash Table with Double Hashing: ");
        hashTableDH.printHashTable();
        //test.set(5, 52);


    }

    private static File createInputFile() {

        File inputFile = new File("input.txt");
        try {

            BufferedWriter output = new BufferedWriter(new FileWriter(inputFile));

            System.out.println("Creating input file...");
            int randInt;

            for(int i = 0; i < 10000; i++ )
            {
                randInt = (int) (Math.random() * 100000)+1;
                output.write(randInt + " " + i + " ");

                if(i != 0 && i%5 == 0)
                    output.newLine();
            }

            System.out.println("Finished creating input file.\n");

        } catch(IOException e){
            System.out.println("Error Occurred");
        }

        return inputFile;
    }
}
