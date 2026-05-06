package core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    private static final Properties PROPS = load();

    private Config() {}

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("android.properties")) {
            if (is == null) throw new RuntimeException("android.properties not found on classpath");
            p.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read android.properties", e);
        }
        return p;
    }

    public static String get(String key) {
        return PROPS.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        return PROPS.getProperty(key, defaultValue);
    }
}
