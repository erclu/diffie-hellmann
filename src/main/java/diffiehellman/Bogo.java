package diffiehellman;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * omaggio al bogosort
 */
public class Bogo {

  /**
   * Limite massimo dei valori segreti da cercare
   */
  private static final int LIMIT = 1000;
  /** questo è il modulo */
  private final long p;
  /** questa è la base */
  private final long g;

  public Bogo(long p, long g) {

    if (p > LIMIT) {
      throw new IllegalArgumentException("this will take sooooooooooooooooooo long");
    }

    this.p = p;
    this.g = g;
  }

  /**
   * esplora casualmente tutte le coppie (a,b) tc a<LIMIT e b<LIMIT
   * 
   * @param publicA valore di A
   * @param publicB valore di B
   * @return tutte le coppie di possibili segreti a,b
   */
  public List<Integer> crack(long publicA, long publicB) {
    List<Integer> res = new ArrayList<Integer>();

    Random random = new Random(new Date().getTime());

    Set<List<Integer>> checkedSecrets = new HashSet<>();

    while (checkedSecrets.size() < p * (p - 1)) {

      PairOfSecrets pair = new PairOfSecrets(random.nextInt((int) p), random.nextInt((int) p));

      if (!checkedSecrets.add(pair.toList())) { // couple was already checked
        continue;
      }

      if (pair.hasSamePublicKeys(publicA, publicB))
        res.addAll(pair.toList());

    }

    return res;
  }

  private class PairOfSecrets {

    private final int secretA;
    private final int secretB;

    public PairOfSecrets(int secretA, int secretB) {
      this.secretA = secretA;
      this.secretB = secretB;
    }

    public boolean hasSamePublicKeys(long publicA, long publicB) {
      return publicA == Utils.modPow(g, secretA, p) && publicB == Utils.modPow(g, secretB, p);
    }

    public List<Integer> toList() {
      List<Integer> list = new ArrayList<>();
      list.add(secretA);
      list.add(secretB);
      return list;
    }
  }

  public static void main(String[] args) {
    long p = 23;
    long g = 5;
    long publicA = 4;
    long publicB = 10;
    List<Integer> test = new Bogo(p, g).crack(publicA, publicB);

    System.out.println(test);
  }
}
