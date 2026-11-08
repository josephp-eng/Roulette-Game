// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.

import java.util.Scanner;

public class Week6Problem1 {
    public static void main(String[] args) {
        Scanner nameGet = new Scanner(System.in);
        System.out.print("Enter your Name: ");
        String name = nameGet.nextLine();
        System.out.println("Hello, " + name);
    }
}