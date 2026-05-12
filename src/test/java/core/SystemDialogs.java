package core;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Helper for dealing with Android system error dialogs that occasionally appear during
 * pit-stop's heavy first-launch on x86_64 emulators (ANR / app-crash dialogs).
 *
 * Prefers the "Wait" action when possible — keeps the app alive and lets the test
 * continue once the main thread frees up. Only falls back to "Close app" if Wait isn't there.
 */
public final class SystemDialogs {

    private SystemDialogs() {}

    /**
     * Dismisses ANR dialogs in a loop — x86_64 emulator can pop them up several times
     * in a row right after dismissal. Returns the number of dialogs dismissed.
     */
    public static int dismissAllAnrs(AppiumDriver driver, int maxAttempts) {
        int dismissed = 0;
        for (int i = 0; i < maxAttempts; i++) {
            if (!dismissAnrIfPresent(driver)) break;
            dismissed++;
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return dismissed;
    }

    /**
     * Dismisses an ANR dialog if present. Returns true if a dialog was dismissed.
     */
    public static boolean dismissAnrIfPresent(AppiumDriver driver) {
        try {
            List<WebElement> waitBtn = driver.findElements(AppiumBy.id("android:id/aerr_wait"));
            if (!waitBtn.isEmpty()) {
                System.out.println("[anr] tapping 'Wait' to keep the app alive");
                waitBtn.get(0).click();
                return true;
            }
            List<WebElement> closeBtn = driver.findElements(AppiumBy.id("android:id/aerr_close"));
            if (!closeBtn.isEmpty()) {
                System.out.println("[anr] no 'Wait' button — tapping 'Close app'");
                closeBtn.get(0).click();
                return true;
            }
        } catch (Exception ignored) {
            // Accessibility tree might be unstable — fine.
        }
        return false;
    }
}
