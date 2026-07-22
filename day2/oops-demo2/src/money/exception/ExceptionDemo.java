package money.exception;

public class ExceptionDemo {

    public static void m2() throws CustomException {
        System.out.println("Inside m2()");
        throw new CustomException("Custom exception thrown from m2()");
    }

    public static void m1() {
        System.out.println("Inside m1()");
        try {
            m2();
        } catch (CustomException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        m1();
    }
}
