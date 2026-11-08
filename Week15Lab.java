import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Set;

public class Week15Lab {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.print("Please input a file name: ");
        String fileName = in.next()+".txt";
        System.out.println(fileName);
        Scanner fileRead = null;
        PrintWriter textWrite = null;
        ArrayList <String> fileOut = new ArrayList<>();
        int count = 0;
        int characterCount = 0;
        System.out.print("Input a single character: ");
        String singleCharacter = in.next().substring(0,1);
        ArrayList <String> searchedText = new ArrayList<>();
        try {
            File userFile = new File(fileName);
            File newFile = new File(fileName.substring(0,fileName.length()-4)+"-searchresults.txt");
            textWrite = new PrintWriter(newFile);
            fileRead = new Scanner(userFile);
            while (fileRead.hasNext()){
                fileOut.add(count, fileRead.next());
                if (fileOut.get(count).substring(0,1).equals(singleCharacter)){
                    characterCount++;
                    searchedText.add(fileOut.get(count));
                }
                count++;
            }
            textWrite.print(singleCharacter+" "+characterCount+ "\n");
            for (int i = 0; i<characterCount; i++){
                textWrite.print(searchedText.get(i)+ "\n");
            }

        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            fileRead.close();
            textWrite.close();
        }
        System.out.println(fileOut.toString());
        System.out.println(characterCount);
    }
}
