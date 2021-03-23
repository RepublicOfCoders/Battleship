import java.util.Scanner;
import java.util.Locale;

public class Main {

    public static void method(String[] args) {
        // write your code here
        System.out.println("Exception");
        throw new RuntimeException();
    }

    /* Do not change code below */
    public static void main(String[] args) {
        try {
            method(args);
        } catch (RuntimeException e) {
            System.out.println("RuntimeException");
        } catch (Exception e) {
            System.out.println("Exception");
        }
    }
}