import java.util.Scanner;
public class Week6Problem3 {
    public static void main(String[] args) {
        Scanner numGet = new Scanner(System.in);
        System.out.print("Restaurant Bill Calculator:");
        System.out.println();
        System.out.print("Enter total bill: ");
        double total = numGet.nextDouble();
        System.out.print("Enter number of people: ");
        int people = numGet.nextInt();
        System.out.print("The bill of $"+total+" will be $"+(double)((int)((total/people)*100)/100)+" each when split among "+ people+ " people");

    }
}