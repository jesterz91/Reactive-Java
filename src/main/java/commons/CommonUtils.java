package commons;

public class CommonUtils {

    public static long startTime;

    public static void exampleStart(){
        startTime = System.currentTimeMillis();
    }

    public static void exampleStart(Object obj) {
        startTime = System.currentTimeMillis();
        Log.it(obj);
    }

    public static void exampleComplete() {
        System.out.println("-----------------------");
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
