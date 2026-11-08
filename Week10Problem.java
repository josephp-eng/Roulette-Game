public class Week10Problem{
    public static void main(String[] args) {
        //TODO: create at least 2 objects of the T-rex class
        //PUT YOUR CODE HERE

        Trex george = new Trex(2,"Green",4);

        Trex kevin = new Trex();

        //TODO: test your T-rex methods by calling them from main method, referencing any of your T-rex objects
        //PUT YOUR CODE HERE

        george.roar("Raymond",7);

        kevin.chomp("Seagull");

        george.walk();

    }

}

//A class named T-rex
class Trex {
    //instance variables
    //TODO: define at least 3 instance variables of your T-rex
    private int tHeight;
    private String tColor;
    private int tFerocity;


    // methods
    //TODO: create at least 3 methods for a T-rex.
    
    /**All methods should have a void return type
    The first method should contain no parameters
    The second method should have at least one parameter
    The third method should have at least two parameters
    Have print statement inside the methods, describing what your T-rex is doing.
    Your print statements should make use of the variables in your parameterized methods
    */

    /**
     * This method describes the action of a Trex walking, based on Ferocity
     */
    public void walk(){
        if (tFerocity>5) {
            System.out.println("Your "+ tColor +" Trex struts with confidence.");
        }
        else {
            System.out.println("Your "+ tColor +" Trex waddles along.");
        }
    }

    /**
     * The Trex will chomp a target, with its strength based on its ferocity
     * @param target The target to be bitten
     */
    public void chomp(String target){
        if (tFerocity>5) {
            System.out.println("Your "+ tColor +" Trex decimates the "+ target);
        }
        else {
            System.out.println("Your "+ tColor +" Trex nibble the "+ target);
        }
    }

    /**
     * This method describes the outcome of a Trex roaring at a target, with its target's resistance to intimidation being its nerve
     * @param target The being subjected to the roar
     * @param nerve The resistance of the target to being intimidated by the roar
     */
    public void roar(String target, int nerve){
        if (tHeight+tFerocity>nerve){
            System.out.println("Your " + tColor + " Trex scares away the " + target);
        }
        else {
            System.out.println("Your " + tColor + " Trex is scared away by the " + target);
        }
    }



//A class also contains constructors, accessors and mutators. We will build on these in the next lab.
    //constructor

    public Trex(){
        tHeight = 5;
        tColor = "red";
        tFerocity = 8;
        System.out.println("Your Trex is " + tColor + " and " + tHeight + "m tall with a ferocity of " + tFerocity);
    }
    public Trex(int height, String color, int ferocity){
        tHeight = height;
        tColor = color;
        tFerocity = ferocity;
        System.out.println("Your Trex is " + tColor + " and " + tHeight + "m tall with a ferocity of " + tFerocity);
    }

    //accessors


    //mutators

}