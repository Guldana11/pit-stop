package core;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries failed tests up to twice. Compensates for flaky setUp on a slow x86_64 Android
 * emulator: observed failure modes are "Failed to create Android driver" (UiAutomator2
 * server dies mid-suite) and "Language selection screen did not appear within 60s" (cold
 * cache after fullReset). One retry wasn't enough — driver creation can fail twice in a
 * row when the emulator is stressed. Two retries lets us ride through these without
 * masking real bugs, since a real bug would fail consistently across all attempts.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRY_COUNT = 2;
    private int currentRetry = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (currentRetry < MAX_RETRY_COUNT) {
            currentRetry++;
            System.out.println("[retry] " + result.getName()
                    + " — attempt " + (currentRetry + 1) + " of " + (MAX_RETRY_COUNT + 1));
            return true;
        }
        return false;
    }
}
