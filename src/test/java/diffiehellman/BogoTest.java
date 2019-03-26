package diffiehellman;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import io.vavr.collection.List;

public class BogoTest {

  @Test
  public void bogoTest() {
    long p = 23;
    long g = 5;
    long publicA = 4;
    long publicB = 10;

    List<Integer> test = List.ofAll(new Bogo(p, g).crack(publicA, publicB));
    assertFalse(test.isEmpty());
    test.grouped(2).forEach(pair -> {
      long secretA = pair.get(0);
      long secretB = pair.get(1);
      assertEquals(Utils.modPow(publicB, secretA, p), Utils.modPow(publicA, secretB, p));
    });
  }
}