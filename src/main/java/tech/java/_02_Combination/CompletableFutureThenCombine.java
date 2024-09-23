package tech.java._02_Combination;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureThenCombine {

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

    CompletableFuture<String> combined = future2.thenCombine (
        future1,
        (result1, result2) -> result1 +
                              " << combine "
                              + ">> " + result2
                                                             );

    combined.thenAccept (data -> log.info ("Final Data:: " + data));

    combined.join ();
  }
}
