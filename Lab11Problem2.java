import java.util.Scanner;
public class Lab11Problem2 {
    public static void main(String[] arg){
        int[] numbers = new int[4];
        Scanner in = new Scanner(System.in);
        int count = 0;
        double sum = 0;
        System.out.print("Please input an integer :) ");
        numbers[count]=in.nextInt();
        sum = numbers[count]+sum;
        count++;
        System.out.print("Please input an integer :) ");
        numbers[count]=in.nextInt();
        sum = numbers[count]+sum;
        count++;
        System.out.print("Please input an integer :) ");
        numbers[count]=in.nextInt();
        sum = numbers[count]+sum;
        count++;
        System.out.print("Please input an integer :) ");
        numbers[count]=in.nextInt();
        sum = numbers[count]+sum;
        count++;
        double avg = sum/count;
        System.out.println("Array Values: "+ numbers[0]+ ", "+ numbers[1]+ ", "+ numbers[2]+ ", "+ numbers[3]);
        System.out.println("Total: "+sum);
        System.out.println("Average: " + avg);

    }
}
