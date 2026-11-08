import java.util.Scanner;
import java.lang.Math;

public class Week5EProblem2 {

    public static void main(String[] args){

        // get sides
        Scanner sideGet = new Scanner(System.in);
        System.out.print("How many sides should this die have?: ");
        int sideN = sideGet.nextInt();

        // roll die of n-1 number of sides...
        // [1,n] = (int) (math.random() * n-1) + 1
        int dieRoll = (int) (Math.random() * (sideN - 1 ) + 1);
        System.out.print("The die roll is: "+dieRoll+"!");

    }
}
