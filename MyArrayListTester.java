package SP24week14;

public class MyArrayListTester {
    private int[] theArray;

    /**
     * Constructor to make an array with nothing in it
     */
    public MyArrayListTester(){
        theArray = new int[0];
    }

    /**
     * How long is the instance variable array currently?
     *
     * @return the length of the instance variable array
     */
    public int size(){
        return this.theArray.length;
    }

    /**
     * Increase the size of the instance variable array by one
     * Copy all elements from the old array to the larger one and modify alias
     */
    private void increaseSize(){
        int[] biggerArray = new int[this.theArray.length + 1];
        for (int i = 0; i < this.theArray.length; i++){
            biggerArray[i] = this.theArray[i];
        }
        this.theArray = biggerArray;
    }

    /**
     * Add an element to the end of the array
     *
     * @param num the value to add to the array
     */
    public void add(int num){
        this.increaseSize();
        this.theArray[this.theArray.length-1] = num;
    }

    /**
     * Obtain an element at a specific index
     *
     * @param index the index position to obtain the element from
     * @return the element at that index
     */
    public int get(int index){
        return this.theArray[index];
    }

    /**
     * Modify an element at a specific index
     *
     * @param index the index position to change the element at
     * @param num the value to change that element to
     */
    public void set(int index, int num){
        this.theArray[index] = num;
    }

    /**
     * Make the array one element smaller
     * Copy all elements except the last element to the new array
     */
    private void decreaseSize(){
        int[] smallerArray = new int[this.theArray.length - 1];
        for (int i = 0; i < this.theArray.length - 1; i++){
            smallerArray[i] = this.theArray[i];
        }
        this.theArray = smallerArray;
    }

    /**
     * Take an element out of the array at a specific index
     * Move all elements after that one to the left
     *
     * @param index The element to remove from the array
     */
    public void remove(int index){

            //Start at the removal position and go to one in from the end
            for (int i = index; i < this.theArray.length - 1; i++) {
                //Change the element at this position to have the value of the element after it
                this.theArray[i] = this.theArray[i + 1];
            }
            //Make the array smaller to remove an element, which should now be a duplicated value at the end!
            this.decreaseSize();

    }

    /**
     * Add an element to the arry at a specific index
     * Move all elements after that one to the right
     *
     * @param index The element position to add at within the array
     * @param num The value to add at that element
     */
    public void add(int index, int num){
        //make it bigger to accomodate a new element first
        this.increaseSize();
        //Start one in from the end and go to the insert position
        for(int i = theArray.length - 2; i >= index; i--){
            //Change the element behind this one to have this one's value
            this.theArray[i+1] = this.theArray[i];
        }
        //We end with the value at index already moved over - so just change to added value!
        this.theArray[index] = num;
    }

    /**
     * @Override for toString method
     * Print the elements in [X, X, X] format
     *
     * @return A string containing the elements in the array, formatted comma-separated
     */
    public String toString(){
        String returnString = "[";
//        if (this.size() > 0){
//            returnString = returnString + this.get(0);
//        }
        for(int i = 0; i<this.size(); i++){
            returnString = returnString + ", " + this.get(i);
        }
        returnString = returnString + "]";
        return returnString;
    }

    public static void main(String[] args){
        MyArrayListTester newArrayList = new MyArrayListTester();
        newArrayList.add(5);
        System.out.println(newArrayList);


    }

}
