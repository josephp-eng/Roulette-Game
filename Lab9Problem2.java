public class Lab9Problem2 {

    public void Boolean(Boolean robot, Boolean code, Boolean time, Boolean mechanism){
        if (robot && code){
            System.out.println("We have a working machine");
        }
        else if (code || (robot && time)){
            System.out.println("We have a plan");
        }
        else if (robot &&! time){
            System.out.println("We have a piece of metal");
        }
        else if (mechanism || code){
            System.out.println("We have a start");
        }
    }

    public static void main(String[]args) {
        Lab9Problem2 test = new Lab9Problem2();
        System.out.println("Test #1");
        test.Boolean(true, false, false, false);
        System.out.println("Test #2");
        test.Boolean(true, false, true, false);
        System.out.println("Test #3");
        test.Boolean(true, true, false, false);
        System.out.println("Test #4");
        test.Boolean(false, false, false, true);
    }
}
