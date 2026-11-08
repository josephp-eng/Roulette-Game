import java.util.ArrayList;
import java.util.Scanner;

public class Lab14Problem1 {
    public static void main(String [] args){
        ArrayList <String> test = new ArrayList <String>();
        Scanner in = new Scanner(System.in);
        int condition = 1;
        String newWord;
        int counter = 0;
        boolean escape = false;
        while (condition == 1){
            System.out.print("Please input a word: ");
            newWord = in.nextLine();
            System.out.println();
            counter = 0;
            for (int i = 0; i<newWord.length(); i++){
                if (newWord.charAt(i)==' '){
                    if (!newWord.substring(counter,i+1).toUpperCase().contains("GOAL")){
                        test.add(newWord.substring(counter,i));
                        counter = i+1;
                    }
                    else{
                        condition = 0;
                        break;
                    }
                }
                else if (i == newWord.length()-1){
                    if (!newWord.substring(counter,i+1).toUpperCase().contains("GOAL")){
                        test.add(newWord.substring(counter,i+1));
                        counter = i+1;
                    }
                    else{
                        condition = 0;
                        break;
                    }
                }
            }
        }


        for (int i = 0; i<test.size(); i++){
            test.set(i, test.get(i).toUpperCase() + "*");
        }
        for (int i = 0; i<test.size(); i++){
            System.out.printf("Index: %d = %10s\n",i, test.get(i));
        }
    }
}
