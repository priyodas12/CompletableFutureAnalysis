package tech.java;

import java.util.concurrent.CompletableFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompletableFutureException {

  public static void main (String[] args) {
    CompletableFuture<Integer> cf1 = new CompletableFuture<> ();
    CompletableFuture<Integer> cf2 = new CompletableFuture<> ();

    //cf1.complete (1);
    CompletableFuture.supplyAsync (() -> {
      try {
        Thread.sleep (4800);
      }
      catch (InterruptedException e) {
        throw new RuntimeException (e);
      }
      return cf1.complete (1);
    });

    cf2.completeExceptionally (new NullPointerException ());

    //will wait until both execution completes
    //CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf (cf1, cf2);

    CompletableFuture<Object> anyOf = CompletableFuture.anyOf (cf1, cf2);
    anyOf.whenComplete ((success, error) -> {
      if (error != null) {
        log.error ("One of the completable future has stuck in :: " + error.getCause ());
      }
    });
    //voidCompletableFuture.join ();
    anyOf.join ();
  }
}
