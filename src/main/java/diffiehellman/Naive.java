package diffiehellman;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Classe da completare per l'esercizio 2.
 */
public class Naive {

  /**
   * Limite massimo dei valori segreti da cercare
   */
  private static final int LIMIT = 65536;

  /**
   * modulo
   */
  private final long p;
  /**
   * base
   */
  private final long g;

  public Naive(long p, long g) {
    this.p = p;
    this.g = g;
  }

  /**
   * Metodo da completare
   * 
   * @param publicA valore di A
   * @param publicB valore di B
   * @return tutte le coppie di possibili segreti a,b
   */
  public List<Integer> crack(long publicA, long publicB) {
    List<Integer> res = new ArrayList<Integer>();

    // strutture per memorizzare i match per possibili segreti di alice e bob
    List<BigInteger> aliceCandidates = new ArrayList<>();
    List<BigInteger> bobCandidates = new ArrayList<>();

    // converte g, p, publicA, publicB a BigInteger e espone metodi utili
    ProblemInstance pb = new ProblemInstance(publicA, publicB);

    // stream parallelo di BigInteger da 0 a LIMIT inclusi gli estremi
    Stream<BigInteger> stream = Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE)).limit(LIMIT + 1).parallel();

    stream.forEach(x -> {
      // calcola pow = g^x mod p
      BigInteger pow = pb.modPow(x);

      // se pow è uguale a publicA, vuol dire che x è un possibile segreto di alice
      if (pb.publicAEquals(pow))
        aliceCandidates.add(x);

      // se pow è uguale a publicB, vuol dire che x è un possibile segreto di bob
      if (pb.publicBEquals(pow))
        bobCandidates.add(x);
    });

    // costruisce la lista dei risultati calcolando il prodotto cartesiano delle due
    // liste di segreti, riconvertendole ad Integer.
    // lancia ArithmeticException se la conversione fallisce
    aliceCandidates.forEach(a -> bobCandidates.forEach(b -> {
      res.add(a.intValueExact());
      res.add(b.intValueExact());
    }));

    return res;
  }

  private class ProblemInstance {

    private final BigInteger base;
    private final BigInteger mod;

    private final BigInteger publicA;
    private final BigInteger publicB;

    public ProblemInstance(long publicA, long publicB) {
      // valori che dipendono dall'istanza di Naive
      this.base = BigInteger.valueOf(g);
      this.mod = BigInteger.valueOf(p);

      // valori che dipendono dalla chiamata a crack(..)
      this.publicA = BigInteger.valueOf(publicA);
      this.publicB = BigInteger.valueOf(publicB);
    }

    public BigInteger modPow(BigInteger exp) {
      return base.modPow(exp, mod);
    }

    public boolean publicAEquals(BigInteger value) {
      return publicA.equals(value);
    }

    public boolean publicBEquals(BigInteger value) {
      return publicB.equals(value);
    }
  }

  public static void main(String[] args) {
    System.out.println("starting counter");
    long start = System.currentTimeMillis();
    long p = 128504093;
    long g = 10009;
    long publicA = 69148740;
    long publicB = 67540095;

    List<Integer> test = new Naive(p, g).crack(publicA, publicB);

    long time = (System.currentTimeMillis() - start);
    System.out.printf("this execution took %s ms, the solution is\n", time % 1000);
    System.out.println(test);
  }
}
