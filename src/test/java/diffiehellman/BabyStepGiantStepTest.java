package diffiehellman;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.vavr.collection.List;

/**
 * BabyStepGiantStepTest
 */
public class BabyStepGiantStepTest {

  @Test
  @Tag("Slow")
  public void BsgsSlowTest() {
    long p = 128504093;
    long g = 10009;
    long publicA = 69148740;
    long publicB = 67540095;
    List<Integer> test = List.ofAll(new BabyStepGiantStep(p, g).crack(publicA, publicB));
    assertFalse(test.isEmpty());
    test.grouped(2).forEach(pair -> {
      long secretA = pair.get(0);
      long secretB = pair.get(1);
      assertEquals(Utils.modPow(publicB, secretA, p), Utils.modPow(publicA, secretB, p));
    });
  }

  @Test
  public void BsgsTest() {
    long p = 128504093;
    long g = 10009;
    long publicA = 69148740;
    long publicB = 67540095;

    java.util.List<Integer> test = new BabyStepGiantStep(p, g).crack(publicA, publicB);

    java.util.List<Integer> result = Arrays.asList(45664, 55428, 45664, 32181451, 45664, 64307474, 45664, 96433497,
        32171687, 55428, 32171687, 32181451, 32171687, 64307474, 32171687, 96433497, 64297710, 55428, 64297710,
        32181451, 64297710, 64307474, 64297710, 96433497, 96423733, 55428, 96423733, 32181451, 96423733, 64307474,
        96423733, 96433497);

    assertEquals(test, result);
    assertNotSame(test, result);
  }

  @Test
  public void sharedSecretsTest() {

    long p = 128504093;
    long g = 10009;
    long publicA = 69148740;
    long publicB = 67540095;
    List<Integer> test = List.ofAll(new Naive(p, g).crack(publicA, publicB));

    Set<Long> sharedSecrets = new HashSet<>();

    test.grouped(2).forEach(pair -> {
      long secA = pair.get(0);
      long secB = pair.get(1);
      long pubA = Utils.modPow(g, secA, p);
      long pubB = Utils.modPow(g, secB, p);
      long sharedSecretA = Utils.modPow(pubB, secA, p);
      long sharedSecretB = Utils.modPow(pubA, secB, p);
      assertEquals(sharedSecretA, sharedSecretB);
      sharedSecrets.add(sharedSecretA);
    });

    assertEquals(1, sharedSecrets.size());
  }
}