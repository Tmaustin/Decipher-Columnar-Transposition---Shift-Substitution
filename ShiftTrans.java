/*
 * COMP 424 Computer System Security
 * Assignment 1
 * October 17, 2016
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public class ShiftTrans {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String ciphertextMain ="DRPWPWXHDRDKDUBKIHQVQRIKPGWOVOESWPKPVOBBDVVVDXSURWRLUEBKOLVHIHBKHLHBLNDQRFLOQ";
        //((10!)+(9!)+(8!)+(7!)+(6!)+(5!)+(4!)+(3!)+(2!)+(1!))*26 = 104985738
        //((9!)+(8!)+(7!)+(6!)+(5!)+(4!)+(3!)+(2!)+(1!))*26 = 10636938
        //((8!)+(7!)+(6!)+(5!)+(4!)+(3!)+(2!)+(1!))*26 = 1202058
        //((7!)+(6!)+(5!)+(4!)+(3!)+(2!)+(1!))*26 = 153738
        //((6!)+(5!)+(4!)+(3!)+(2!)+(1!))*26 = 22698
        //((5!)+(4!)+(3!)+(2!)+(1!))*26 = 3978
        //((4!)+(3!)+(2!)+(1!))*26 = 858
        //((3!)+(2!)+(1!))*26 = 234
        //((2!)+(1!))*26 = 78
        //((1!))*26 = 26
        
        
        //In order for the program to run you need to set the total amount of
        //strings after you do the Shift Cipher and the Transposition Cipher
        //Look above for the values from 1-10
        
        //*********************PLEASE CHANGE BEFORE YOU RUN*********************
        int totalStrings = 3978;
        //**********************************************************************
        
        //Storage of all the Deciphered Strings
        String [][] allStrings = new String [totalStrings][4]; 
        //Matches Decyphered String Key
        int allStringsCount = 0; 
        //Goes through the entire alphabet to shift 1-26
        for(int shiftNum = 0 ; shiftNum < 26 ; shiftNum++){
            //Sends the ciphertext to be shifted by the shift number its currently on
            String ciphertext = cipher (ciphertextMain , shiftNum);
            
            //Get the permutations file
            //http://textmechanic.com/text-tools/combination-permutation-tools/permutation-generator/
            //Got all permuations from 1-10 and directly saved them to a text file
            //After I got each permutation I compined them into one text file
            BufferedReader keyLength = new BufferedReader(new FileReader("permutations.txt"));
            try {
                //reads the first line of the txt docuent
                String key = keyLength.readLine();
                
                //length of the key
                int columnLength = key.length();
                //gets the row length
                int rowLength = ciphertext.length() / columnLength;
                //padding of the last row
                int padding = (rowLength * columnLength) -ciphertext.length() ;
                
                if (padding != 0 ){
                    //If the padding is not perfect then add a row
                    rowLength++;
                }
                    
                while (key != null) {
                    //Redue the original integers when it reads the next key
                    columnLength = key.length();
                    rowLength = ciphertext.length() / columnLength;
                    padding = ciphertext.length() - (rowLength * columnLength);

                    if (padding != 0 ){
                        rowLength++;
                    }
                    if( key != null){
                        //Testing
                        //System.out.println(ciphertext + "      " + columnLength + "  " + padding + "  " + rowLength + "  " + key);
                        
                        //Check if the column has padding on it
                        //This passding will occur at the end of the column
                        boolean [] hasPadding = new boolean [columnLength];
                        for(int i = 0; i < key.length(); i++) {
                            if(padding == 0){
                                hasPadding [Character.getNumericValue(key.charAt(i))-1] = true;
                            }
                            else if (i < padding ){
                                hasPadding [Character.getNumericValue(key.charAt(i))-1] = true;
                            }
                            else{
                                hasPadding [Character.getNumericValue(key.charAt(i))-1] = false;
                            }
                        }
                        
                        //This table will have the list of the characters in there correct columns
                        char [][] keyTable = new char [rowLength][columnLength];
                        //Counts the number of substrings length
                        int substringCount = 0;
                        //seperates the ciphertext string into its substrings 
                        //and if there is padding then the length is rowlength-1
                        //put there is no padding then its equal to the rowlength
                        for (int column = 0 ; column < columnLength ; column++){
                            String tempString = "";
                            if (hasPadding[column]){
                                tempString = ciphertext.substring(substringCount, substringCount + rowLength);
                                substringCount = substringCount + rowLength;
                            }
                            else{
                                tempString = ciphertext.substring(substringCount, substringCount + (rowLength - 1));
                                substringCount = substringCount + (rowLength - 1);
                            }
                            //places the substring in the respecive columns by each row
                            for (int row = 0 ; row < rowLength ; row++)
                                if( row < tempString.length()){
                                    keyTable [row][column] = tempString.charAt(row);                                    
                                }
                            //System.out.println((column+1) + "   " + tempString + "    " + tempString.length());
                        }
                        
                        //Print Decoded Message
                        //It reads the deciphered code in order of the key to get the 
                        //deciphered string
                        String  decypheredString ="";
                        for(int printRow=0 ; printRow < rowLength ; printRow++) {
                            for(int printColumn = 0; printColumn < columnLength; printColumn++){
                                int columnFixed = Character.getNumericValue(key.charAt(printColumn))-1;
                                if(Character.isAlphabetic(keyTable [printRow][columnFixed])){
                                    decypheredString = decypheredString + "" + keyTable [printRow][columnFixed];
                                }
                                else{
                                }
                            }
                        }
                        //Shift
                        allStrings [allStringsCount][3] = Integer.toString(shiftNum);
                        //Key
                        allStrings [allStringsCount][2] = key;
                        //Decyphered String
                        allStrings [allStringsCount][1] = decypheredString;
                        //Matches
                        allStrings [allStringsCount][0] = Integer.toString(countMatch(decypheredString));

                        //Increase the number of strings by 1
                        allStringsCount++;
                    }
                    //Next Line
                    key = keyLength.readLine();
                }
            } finally {
                keyLength.close();
            }
        }
        //This will sort the array by the number of matches in decreasing order
        Arrays.sort(allStrings, new Comparator<String[]>() {
            @Override
            public int compare(final String[] left, final String[] right) {
                final int leftInt = Integer.parseInt(left[0]);
                final int rightInt = Integer.parseInt(right[0]);
                return Integer.compare(rightInt, leftInt);
            }
        });
        PrintWriter writer = new PrintWriter("Top200.txt", "UTF-8");
        //This prints out the number of matches in decreasing order up to 100
        for(int x = 0 ; x < 200; x++){
            if( x < allStrings.length){
                String tempString ="#" + (x+1) + "  " +
                    "Matches : " + allStrings [x][0] + " -  " + 
                    "Shift Number : " + allStrings [x][3] + " -  " +
                    "Key : " + allStrings [x][2] + "\n"+
                    "String : " + allStrings [x][1];
                writer.println(tempString);
                System.out.println( tempString );
            }
            else{
                break;
            }
        }
        writer.close();
    }
    //This will shift the string around by there letter and the number of shifts 
    //it wants to do
    //http://stackoverflow.com/questions/19108737/java-how-to-implement-a-shift-cipher-caesar-cipher
    //User: Eric Leschinski
    //Date: Feb 17 2014
    //Personal Site: http://stackoverflow.com/users/445131/eric-leschinski
    public static String cipher(String msg, int shift){
        String s = "";
        int len = msg.length();
        for(int x = 0; x < len; x++){
            char c = (char)(msg.toUpperCase().charAt(x) + shift);
            if (c > 'Z')
                s += (char)(msg.toUpperCase().charAt(x) - (26 - shift));
            else
                s += (char)(msg.toUpperCase().charAt(x) + shift);
        }
        return s;
    }
    //This compares the deciphered strings to a dictionary and it counts all of the
    //matches.
    public static int countMatch(String text) throws FileNotFoundException, IOException{
        //https://gist.github.com/deekayen/4148741#file-1-1000-txt
        BufferedReader dictionary = new BufferedReader(new FileReader("1-1000.txt"));
        int matches = 0; 
        try {
            String line = dictionary.readLine();
            while (line != null) {
                if (text.contains(line.toUpperCase()))
                    matches++;
                line = dictionary.readLine();
            }
        } finally {
            dictionary.close();
        }
        return matches;
    }
}
