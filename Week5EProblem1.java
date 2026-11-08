import java.util.Scanner;

public class Week5EProblem1 {

    public static void main(String[] args){

        // get numbers
        Scanner numberGet = new Scanner(System.in);
        System.out.print("Enter Number 1: ");
        int number1 = numberGet.nextInt();
        System.out.print("Enter Number 2: ");
        int number2 = numberGet.nextInt();
        System.out.print("Enter Number 3: ");
        int number3 = numberGet.nextInt();
        System.out.print("Enter Number 4: ");
        int number4 = numberGet.nextInt();

        // print average
        int avg = (number1 + number2 + number3 + number4)/4;
        System.out.print("Your Average is: "+avg);

    }
}
