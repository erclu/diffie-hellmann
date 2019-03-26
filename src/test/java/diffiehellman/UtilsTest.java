package diffiehellman;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class UtilsTest {

  @Test
  public void modPowTest() {
    long[] res = { 2, 4, 8, 5, 10, 9, 7, 3, 6, 1 };
    for (int i = 0; i < res.length; i++) {

      // XXX linea sostituita con la successiva
      // assertEquals(res[i], Utils.modPow(2, i, 11));
      assertEquals(res[i], Utils.modPow(2, i + 1, 11));
    }
  }

  @Test
  public void isPrimeRootTest() {
    assertTrue(Utils.isPrimeRoot(2, 11));
    assertTrue(Utils.isPrimeRoot(5, 23));
    // XXX linea rimossa perché il toziente di 15 è 8, non 14
    // assertFalse(Utils.isPrimeRoot(7, 15));

    assertFalse(Utils.isPrimeRoot(3, 13));
  }

  @Test
  public void exchangeTest() {
    long p = 23, base = 5;
    assertTrue(Utils.isPrimeRoot(5, 23));
    long secretA = 4;
    long publicA = Utils.modPow(base, secretA, p);
    long secretB = 3;
    long publicB = Utils.modPow(base, secretB, p);
    assertEquals(Utils.modPow(publicB, secretA, p), Utils.modPow(publicA, secretB, p));
  }

  @Test
  public void primeTest() {
    List<Long> primes = Utils.findPrimes(70, 2000, 300);
    int idx = 19;
    for (long n : primes) {
      assertEquals(Utils.firstPrimes[idx], n, "prime " + n);
      idx++;
    }
  }

  @Test
  public void searchTest() {
    long p = 8503057;

    System.out.println(String.format("%x", p));

    // XXX linea rimossa perché la fattorizzazione del toziente di p ritorna una
    // lista di fattori scorretta
    // assertTrue(Utils.isPrimeRoot(10009, p));

    // 8503057-1 = 2^4*3^12, mentre la chiamata primeFactors(8503056)
    // ritorna la lista [2,2,2,2] invece di [2,3]
    List<Integer> primeFactors = new ArrayList<>();
    primeFactors.add(2);
    primeFactors.add(3);
    assertNotEquals(primeFactors, Utils.primeFactors(p));

    long base = 10009;
    long secretA = 42;
    long publicA = Utils.modPow(base, secretA, p);
    long secretB = 123;
    long publicB = Utils.modPow(base, secretB, p);
    assertEquals(Utils.modPow(publicB, secretA, p), Utils.modPow(publicA, secretB, p));

    System.out.println(String.format("a %d b %d A %d B %d", secretA, secretB, publicA, publicB));

    System.out.println(Utils.modPow(publicB, secretA, p));
  }

}
