package diffiehellman;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import io.vavr.collection.List;

/**
 * Per la soluzione dell'esercizio 2, questo test deve risultare soddisfatto.
 * 
 * Può essere lanciato da linea di comando con `./gradlew exe2`
 */
public class NaiveTest {

  @Test
  public void exer2() {
    long p = 128504093;
    long g = 10009;
    long publicA = 69148740;
    long publicB = 67540095;
    List<Integer> test = List.ofAll(new Naive(p, g).crack(publicA, publicB));
    assertFalse(test.isEmpty());
    test.grouped(2).forEach(pair -> {
      long secretA = pair.get(0);
      long secretB = pair.get(1);
      assertEquals(Utils.modPow(publicB, secretA, p), Utils.modPow(publicA, secretB, p));
    });
  }

}
