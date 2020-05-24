package hiworks.com.dynamicrouting.multiTenancy;

public class ThreadLocalStorage {
    private static ThreadLocal<String> subDB = new ThreadLocal<>();

    public static void setSubDBName(String subDBName) {
        subDB.set(subDBName);
    }

    public static String getSubDBName() {
        return subDB.get();
    }

    public static void clear() {
        subDB.set(null);
    }
}
