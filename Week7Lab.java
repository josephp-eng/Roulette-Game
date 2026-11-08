import java.lang.Math;

public class Week7Lab {

    public String makeItAwesome(String input){
        String out= input +" is awesome!!";
        return out;
    }

    public int rollNSidedDie(int sideN){
        int dieRoll = (int) (Math.random() * (sideN ) +1 );
        return dieRoll;
    }

    public static void main(String[] args) {

        // problem 1

        Week7Lab tester = new Week7Lab();
        //Test One
        String text = "Coding";
        System.out.println(tester.makeItAwesome(text));
        //Test Two
        System.out.println(tester.makeItAwesome("Michigan Tech"));
        //Test Three
        String awesome = tester.makeItAwesome("Everything");
        System.out.println(awesome);

        // problem 2

        System.out.println(tester.rollNSidedDie(6));
        System.out.println(tester.rollNSidedDie(4));
        System.out.println(tester.rollNSidedDie(2));

    }
}
