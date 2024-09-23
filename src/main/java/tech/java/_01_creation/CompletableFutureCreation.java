package tech.java._01_creation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureCreation {

  public static void main (String[] args) {

    //**********************************************************************************

    //with Supplier;
    Supplier<String> someLongRunningTask = () -> {
      String result = null;
      try {
        log.info ("someLongRunningTask Started:: {}", Thread.currentThread ().getName ());
        Thread.sleep (4000);
        result = LocalDateTime.now ().toString ();
        log.info ("someLongRunningTask completed:: {}", Thread.currentThread ().getName ());
      }
      catch (InterruptedException e) {
        log.error ("someLongRunningTask Exception:: {}", e.getMessage ());
      }
      return result;
    };

    CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync (
            someLongRunningTask)
        .thenApply (data -> data + " >> " + UUID.randomUUID ())
        .thenAccept (data -> log.info ("Completed with:: " + data));

    voidCompletableFuture.join ();

    //**********************************************************************************

    //with Runnable
    Runnable nothingWillBeReturned = () -> {
      try {
        log.info ("nothingWillBeReturned Started:: {}", Thread.currentThread ().getName ());
        Thread.sleep (4000);
        log.info (LocalDateTime.now ().toString ());
        log.info ("nothingWillBeReturned completed:: {}", Thread.currentThread ().getName ());
      }
      catch (InterruptedException e) {
        throw new RuntimeException (e);
      }
    };

    CompletableFuture<Void> runnableCompletableFuture =
        CompletableFuture.runAsync (nothingWillBeReturned);

    runnableCompletableFuture.join ();

    //**********************************************************************************

    //with completedFuture
    CompletableFuture<Instant> instantCompletableFuture =
        CompletableFuture.completedFuture (Instant.now ());

    instantCompletableFuture.thenAccept (data -> log.info (data + " >>  instantCompletableFuture "
                                                           + "completed with:: "
                                                           + Thread.currentThread ().getName ()));

    instantCompletableFuture.join ();
  }
}
/**
 * [ForkJoinPool.commonPool-worker-1] INFO tech.java._01_creation.CompletableFutureCreation -
 * someLongRunningTask Started:: ForkJoinPool.commonPool-worker-1 [ForkJoinPool.commonPool-worker-1]
 * INFO tech.java._01_creation.CompletableFutureCreation - someLongRunningTask completed::
 * ForkJoinPool.commonPool-worker-1 [ForkJoinPool.commonPool-worker-1] INFO
 * tech.java._01_creation.CompletableFutureCreation - Completed with:: 2024-09-23T18:30:40.938006600
 * >> 7b359288-ff51-400b-8d0d-9da1aa2efdd7 [ForkJoinPool.commonPool-worker-1] INFO
 * tech.java._01_creation.CompletableFutureCreation - nothingWillBeReturned Started::
 * ForkJoinPool.commonPool-worker-1 [ForkJoinPool.commonPool-worker-1] INFO
 * tech.java._01_creation.CompletableFutureCreation - 2024-09-23T18:30:44.992440500
 * [ForkJoinPool.commonPool-worker-1] INFO tech.java._01_creation.CompletableFutureCreation -
 * nothingWillBeReturned completed:: ForkJoinPool.commonPool-worker-1 [main] INFO
 * tech.java._01_creation.CompletableFutureCreation - 2024-09-23T13:00:44.992440500Z >>
 * instantCompletableFuture completed with:: main
 */