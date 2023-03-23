package com.api.automation.utils.async;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;

/** Async Util to wait until a condition is satisfied */
@Slf4j
public class AsyncUtils {

  /**
   * Waits until a given condition for the given max time in seconds.
   *
   * @param waitUntilSeconds max time to wait in seconds
   * @param conditionToWait condition to wait
   */
  public static void waitUntil(long waitUntilSeconds, Callable<Boolean> conditionToWait) {
    await().atMost(waitUntilSeconds, SECONDS).until(conditionToWait);
  }

  /**
   * Waits until a given condition for the given max time in seconds and poll interval in seconds.
   *
   * @param waitUntilSeconds max time to wait in seconds
   * @param pollIntervalSeconds polling time between each call
   * @param conditionToWait condition to wait
   */
  public static void waitUntil(
      long waitUntilSeconds, long pollIntervalSeconds, Callable<Boolean> conditionToWait) {
    await()
        .atMost(waitUntilSeconds, SECONDS)
        .with()
        .pollInterval(pollIntervalSeconds, SECONDS)
        .until(conditionToWait);
  }
}
