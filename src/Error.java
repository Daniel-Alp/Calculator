public class Error {
    public static boolean hadError;

    public static void report(String error, int where) {
        System.out.printf("Error: %s at character %d\n", error, where);
        hadError = true;
    }
}
