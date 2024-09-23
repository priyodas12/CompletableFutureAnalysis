package tech.java;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureAllOf {

  public static void main (String[] args) {
    Supplier<String> task1 = () -> {
      String result = null;
      try {
        log.info ("Task1 Started:: {}", Thread.currentThread ().getName ());
        Thread.sleep (3000);
        result = UUID.randomUUID ().toString ();
        log.info ("Task1 completed:: {}", Thread.currentThread ().getName ());
      }
      catch (InterruptedException e) {
        log.error ("Task1 Exception:: {}", e.getMessage ());
      }
      return result;
    };

    Supplier<String> task2 = () -> {
      String result = null;
      try {
        log.info ("Task2 Started:: {}", Thread.currentThread ().getName ());
        Thread.sleep (7000);
        result = LocalDateTime.now ().toString ();
        log.info ("Task2 completed:: {}", Thread.currentThread ().getName ());
      }
      catch (InterruptedException e) {
        log.error ("Task2 Exception:: {}", e.getMessage ());
      }
      return result;
    };

    // Create CompletableFuture instances for each task
    CompletableFuture<String> future1 = CompletableFuture.supplyAsync (task1);
    CompletableFuture<String> future2 = CompletableFuture.supplyAsync (task2);

    // Combine the results when both tasks complete
    CompletableFuture<Void> allOfFuture = CompletableFuture.allOf (future1, future2)
        .thenRun (() -> {
          try {
            String result1 = future1.get ();
            String result2 = future2.get ();
            log.info ("Combined data: {}: {}", result1, result2);
          }
          catch (Exception e) {
            log.error ("Error getting results", e);
          }
        });

    // Wait for all futures to complete
    allOfFuture.join ();
  }
}
