import java.util.LinkedList;
import java.util.ArrayList;

public class HashTable {

    HashNode [] hashTableArr;
    double loadFactor = 0.7;
    int growFactor = 2;
    int size = 1;
    int collisions = 0;
    String collisionHandling ;

    LinkedList<HashNode> chainList = new LinkedList<HashNode>();

    public HashTable(){
        hashTableArr = new HashNode[10];        // default size is 10
    }

    public HashTable(int arrSize){
        hashTableArr = new HashNode[arrSize];           // initialize hashTable with size
        collisionHandling = "LP";
    }

    public HashTable(String collisionHandling){
        hashTableArr = new HashNode[100];           // initialize hashTable with size and type of collision handling

        // LP = Linear Probing; QP = Quadratic Probing; DH = Double Hashing
        if(collisionHandling.equals("LP") || collisionHandling.equals("QP") || collisionHandling.equals("DH"))
            this.collisionHandling = collisionHandling;
        else {
            System.out.println("Incorrect input for probing, set Linear Probing by default.");
            collisionHandling = "LP";
        }

    }
    public HashTable(int arrSize, String collisionHandling){
        hashTableArr = new HashNode[arrSize];           // initialize hashTable with size and type of collision handling

        // LP = Linear Probing; QP = Quadratic Probing; DH = Double Hashing
        if(collisionHandling.equals("LP") || collisionHandling.equals("QP") || collisionHandling.equals("DH"))
            this.collisionHandling = collisionHandling;
        else {
            System.out.println("Incorrect input for probing, set Linear Probing by default.");
            collisionHandling = "LP";
        }

    }

    public void put(int key, int value){        // inserts a key and a value to the hashtable.


        //System.out.println("size:" + size + " max: " + loadFactor*hashTableArr.length);

        if(size>loadFactor*hashTableArr.length){        // checks if 70% of the hashtable is taken
            growArray();                                // grows the array if needed.
        }

        int HashInd = hashFunction(key, hashTableArr.length);       // hashes the key and gets the index required.


        if(HashInd != -1){
            hashTableArr[HashInd] =  new HashNode(key, value);          // inserts new hashNode(with key and value) at the hashed key index (HashInd)
            size++;
        }   // keeps track of how many items have been inserted
        else
            System.out.println("Could not add (" + key +", " +  value + ") due to infinite cycle during double hashing.");

    }

    public void putReHash(HashNode node, HashNode[] newArr){    // Used to insert when the array is grown


        int HashInd = reHashFunction(node.key, newArr.length, newArr);

        if(HashInd != -1)
            newArr[HashInd] = node;
        else
            System.out.println("Could not add (" + node.key +", " +  node.value + ") due to infinite cycle during double hashing with the rehash.");

    }

    private void growArray() {          // increases the hashTable size by the growFactor

        System.out.println("Growing Array...");
        HashNode[] newArr = new HashNode[hashTableArr.length*growFactor];   // new array to copy old array into.

        for(int i = 0; i< hashTableArr.length; i++){            // Copies all elements in the current array into the new one
            if(hashTableArr[i]!= null){
                putReHash(hashTableArr[i], newArr);             // uses a different "put" method that works like the original "put"
                                                                // method but doesnt update size or check the need to grow again
            }
        }

        hashTableArr = newArr;

    }

    public int hashFunction(int key, int arrLength){            // This the method that turns the key into a hash Index

        int HashInd = key % arrLength;          // Hash function is basically key mod table size.

        if(hashTableArr[HashInd]==null)         // if the spot is open, this is the index used
            return HashInd;
        else                                    // otherwise there is a collision
        {
            collisions++;
            int doubleHashInd;
           // System.out.println("COLLISION AT " + HashInd + " KEY : "+ key);

            if (collisionHandling.equals("LP"))
                return linearProbing(HashInd, arrLength, hashTableArr);       // method to handle collisions by linear probing
            else if (collisionHandling.equals("QP"))
                return quadraticProbing(key, HashInd, arrLength, hashTableArr);
            else if (collisionHandling.equals("DH")){

                doubleHashInd = doubleHashing(HashInd,key, arrLength, hashTableArr);
                if(doubleHashInd != -1)
                {
                    return doubleHashInd;
                }
            }
        }

        return -1;
    }

    public int reHashFunction(int key, int arrLength, HashNode[] newArr){       // Used as a helper when array is copied into a new one

        int HashInd = key % arrLength;                  // Hash function is basically key mod table size.
        int doubleHashInd;
        if(newArr[HashInd]==null)                       // if the spot is open, this is the index used
            return HashInd;
        else                                            // otherwise there is a collision
        {
            collisions++;

            if(collisionHandling.equals("LP"))
                return linearProbing(HashInd, arrLength, newArr);
            else if (collisionHandling.equals("QP"))
                return quadraticProbing(key, HashInd, arrLength, newArr);
            else if (collisionHandling.equals("DH")){

                 doubleHashInd = doubleHashing(key, HashInd, arrLength, newArr);
                 if(doubleHashInd != -1)
                 {
                     return doubleHashInd;
                 }
            }

        }

        return -1;
    }

    public int linearProbing(int HashInd, int arrLength, HashNode[] newArr){        // Linear Probing collision handling

        for(int i = HashInd+1; i != HashInd; i++){                  // Starts at the current index + 1 and looks for empty spot
            if (i >= arrLength)                                     // If it reaches the end, it starts back at the first spot.
                i = 0;

            if(newArr[i]==null){                                    // if the spot is empty, it returns the index it should be at.
                return i;
            }

            collisions++;                                           // Each time it finds a spot wth an element, it counts as a collision

        }

        return -1;
    }

    public int quadraticProbing(int key, int oldInd, int arrLength, HashNode[] newArr){

        int counter = 1;
        int HashInd = oldInd;

        while(newArr[HashInd]!= null){                                    // While HashInd is pointing to an element, try another HashInd
            HashInd = (key + counter * counter) % arrLength;
            counter++;
        }

        return HashInd;
    }

    public int doubleHashing(int oldInd, int key, int arrLength, HashNode[] newArr){
        int HashIndex = oldInd;

        for(int i=1; newArr[HashIndex]==null; i++)
        {
            HashIndex = secondHash(oldInd,  key, arrLength, i);
            collisions++;

        }


        return HashIndex;

    }

    private int secondHash(int inputInd, int key, int arrLength, int i){

        return (inputInd + i * (97- key % 97) ) % arrLength;
    }

    public void printHashTable() {                  // Method that just prints out the whole table.

        System.out.print("\n[");
        for(int i = 0; i<hashTableArr.length; i++){

            if( i%10 == 0 && i != 0)
                System.out.println();

            if(hashTableArr[i]!=null)
                System.out.printf( "%-20s%-5s", i + ": (" + hashTableArr[i].key + ", " + hashTableArr[i].value + ")", " | ");       // Format of output is "HashIndex : (key, value) | "
                //System.out.print( i + ": (" + hashTableArr[i].key + ", " + hashTableArr[i].value + ") | ");       // Format of output is "HashIndex : (key, value) | "
            else
                System.out.printf( "%-20s%-5s", i + ": (null)" , " | ");
                //System.out.print( i + ": (null) | ");
        }

        System.out.println("]");
        System.out.println("\nType of Collision Handling: " + collisionHandling + " | Collisions: " + collisions);  // Reports the total count of collisions in the program
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }


    public int getKey(int key){

        int hashInd = hashFunction(key, hashTableArr.length);

        if(hashTableArr[hashInd].key == key)
            return hashTableArr[hashInd].key;

        return 0;
    }

    public int getValue(int key){

        int hashInd = hashFunction(key, hashTableArr.length);

        if(hashTableArr[hashInd].key == key)
            return hashTableArr[hashInd].value;

        return 0;
    }


}
