public class Error {
    static boolean hadError;

    public static void report(String error, int where) {
        System.err.printf("Error: %s at character %d\n", error, where);
        hadError = true;
    }
}
