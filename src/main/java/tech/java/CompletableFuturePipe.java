package tech.java;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFuturePipe {

  public static void main (String[] args) throws ExecutionException, InterruptedException {
    Supplier<String> task1 = () -> {
      String result = null;
      try {
        log.info ("Task1 Started:: {}", Thread.currentThread ().getName ());
        Thread.sleep (4000);
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
        Thread.sleep (4000);
        result = LocalDateTime.now ().toString ();
        log.info ("Task2 completed:: {}", Thread.currentThread ().getName ());
      }
      catch (InterruptedException e) {
        log.error ("Task2 Exception:: {}", e.getMessage ());
      }
      return result;
    };

    CompletableFuture<String> combinedFuture = CompletableFuture.supplyAsync (task1)
        .thenCombine (
            CompletableFuture.supplyAsync (task2),
            (result1, result2) -> String.format ("%s: %s", result1, result2)
                     )
        .thenApply (data -> "combined data:" + data + ", " + Thread.currentThread ().getName ());
    log.info ("Final Result:: {}", combinedFuture.get ());

    //asking main thread to wait
    combinedFuture.join ();
  }
}
