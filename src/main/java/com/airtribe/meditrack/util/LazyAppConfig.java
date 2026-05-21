package main.java.com.airtribe.meditrack.util;

/**
 * LazyAppConfig - Singleton Pattern (LAZY INITIALIZATION)
 *
 * Instance created on first use (not when class loads).
 *
 * Advantages:
 * - Memory-efficient (only created if used)
 * - Lazy loading benefit
 *
 * Disadvantages:
 * - Requires synchronization for thread-safety
 * - Small overhead on first call
 *
 * Compare with:
 * - IdGenerator: EAGER initialization (created at class load)
 * - AppConfig: EAGER initialization (created at class load)
 */
public class LazyAppConfig {
    // LAZY: Instance created on first use
    private static LazyAppConfig instance;

    private String appName;
    private String appVersion;
    private boolean debugMode;

    /**
     * Private constructor - prevents external instantiation
     */
    private LazyAppConfig() {
        this.appName = "MediTrack";
        this.appVersion = "1.0.0";
        this.debugMode = false;
    }

    /**
     * Gets singleton instance with lazy initialization
     * Synchronized to ensure thread-safety
     *
     * @return the singleton LazyAppConfig instance
     */
    public static synchronized LazyAppConfig getInstance() {
        if (instance == null) {
            instance = new LazyAppConfig();
        }
        return instance;
    }

    /**
     * Alternative: Double-checked locking pattern
     * More efficient for concurrent access
     */
    public static LazyAppConfig getInstanceDoubleChecked() {
        if (instance == null) {
            synchronized (LazyAppConfig.class) {
                if (instance == null) {
                    instance = new LazyAppConfig();
                }
            }
        }
        return instance;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public String toString() {
        return "LazyAppConfig{appName='" + appName + "', version='" + appVersion + "', debug=" + debugMode + '}';
    }
}

