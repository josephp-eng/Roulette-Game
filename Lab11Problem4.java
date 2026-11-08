import java.util.Scanner;
public class Lab11Problem4 {
    public static void main(String[] args){
        //Initialize a scanner and gain an input from user
        Scanner in = new Scanner(System.in);
        System.out.print("Please enter a line of text: ");
        String input = in.nextLine();
        //creates an array of char out of input string
        char [] inputArray;
        inputArray = input.toCharArray();
        //checks for lowercase first letter and capitalizes it
        if (inputArray[0]>96 && inputArray[0]<123){
            inputArray[0] = (char)(inputArray[0]-32);
        }
        //replaces last char with .
        inputArray[inputArray.length-1]=46;
        //transition back to string and print
        new String(inputArray);
        System.out.print(inputArray);
    }
}
