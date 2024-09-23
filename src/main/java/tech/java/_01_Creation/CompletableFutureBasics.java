package tech.java._01_Creation;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureBasics {

  public static void main (String[] args) throws ExecutionException, InterruptedException {

    // 1. Run a task asynchronously
    CompletableFuture<Void> future = CompletableFuture.runAsync (() -> {
      // Simulate a long-running task
      try {
        log.info ("Task started..." + Thread.currentThread ().getName ());
        Thread.sleep (4000);
        log.info ("Task completed." + Thread.currentThread ().getName ());
      }
      catch (InterruptedException e) {
        log.info ("Exception 1:: {}", e.getMessage ());
      }
    });

    // Wait for the task to complete
    future.get ();

    // 2. Supply a result asynchronously (non-blocking)
    CompletableFuture<String> resultFuture = CompletableFuture.supplyAsync (() -> {
      // Simulate another task
      try {
        log.info ("Fetching data started:: {}", Thread.currentThread ().getName ());
        Thread.sleep (4000);
        log.info ("Fetching data completed:: {}", Thread.currentThread ().getName ());
      }
      catch (InterruptedException e) {
        log.error ("Exception 2:: {}", e.getMessage ());
      }
      return "Hello, CompletableFuture!";
    });

    // Get the result when done
    String result = resultFuture.get ();
    log.info ("Result: " + result);

    // 3. Chain tasks together
    CompletableFuture<String> chainedFuture = resultFuture
        .thenApply (data -> data + " Let's chain.")
        .thenApply (data -> data + " And keep chaining.");

    log.info ("Chained result: " + chainedFuture.get ());

    // 4. Handle exceptions
    CompletableFuture<String> exceptionalFuture = CompletableFuture.supplyAsync (() -> {
      // Simulate an exception
      if (true) {
        log.error ("Exception 3::");
      }
      return "Success!";
    }).exceptionally (ex -> {
      log.info ("Error: " + ex.getMessage ());
      return "Recovered from error!";
    });

    // Get the result, either the original one or the recovered one
    log.info ("Exceptional handling result: " + exceptionalFuture.get ());
  }
}

