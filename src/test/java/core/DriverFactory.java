package core;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.URL;
import java.nio.file.Paths;

public final class DriverFactory {

    private DriverFactory() {}

    public static AndroidDriver create() {
        String serverUrl = Config.get("appium.server.url", "http://127.0.0.1:4723");
        try {
            URL url = new URL(serverUrl);
            return new AndroidDriver(url, buildAndroid());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Android driver", e);
        }
    }

    private static UiAutomator2Options buildAndroid() {
        UiAutomator2Options opts = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setDeviceName(Config.get("device.name"))
                .setNoReset(Boolean.parseBoolean(Config.get("no.reset", "false")))
                .setFullReset(Boolean.parseBoolean(Config.get("full.reset", "true")));
        // Generous ADB / app-start timeouts — slow emulators time out at the default 20 s.
        opts.setCapability("appium:adbExecTimeout", 60000);
        opts.setCapability("appium:appWaitDuration", 30000);
        opts.setCapability("appium:uiautomator2ServerLaunchTimeout", 60000);
        opts.setCapability("appium:uiautomator2ServerInstallTimeout", 60000);

        String pv = Config.get("platform.version");
        if (pv != null && !pv.isBlank()) opts.setPlatformVersion(pv);

        String appPackage = Config.get("app.package");
        if (appPackage != null && !appPackage.isBlank()) opts.setAppPackage(appPackage);

        String appActivity = Config.get("app.activity");
        if (appActivity != null && !appActivity.isBlank()) opts.setAppActivity(appActivity);

        String appPath = Config.get("app.path");
        if (appPath != null && !appPath.isBlank()) {
            opts.setApp(Paths.get(appPath).toAbsolutePath().toString());
        }
        return opts;
    }
}
