/**
 * Utility class for managing thread-local storage.
 * Provides thread-safe storage for user-specific data during request processing.
 * Implements methods for setting, getting, and removing thread-local values.
 */
package one.wcy.ebookloaningtool.utils;

/**
 * ThreadLocal utility class
 */
@SuppressWarnings("all")
public class ThreadLocalUtil {
    /**
     * ThreadLocal instance for storing thread-specific data
     */
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    /**
     * Retrieves the value stored in the current thread's ThreadLocal.
     *
     * @param <T> The type of the stored value
     * @return The value stored in ThreadLocal, or null if not set
     */
    public static <T> T get(){
        return (T) THREAD_LOCAL.get();
    }
	
    /**
     * Stores a value in the current thread's ThreadLocal.
     *
     * @param value The value to store in ThreadLocal
     */
    public static void set(Object value){
        THREAD_LOCAL.set(value);
    }

    /**
     * Removes the value stored in the current thread's ThreadLocal.
     * This method should be called after request processing to prevent memory leaks.
     */
    public static void remove(){
        THREAD_LOCAL.remove();
    }
}
