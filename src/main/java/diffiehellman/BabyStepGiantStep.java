package diffiehellman;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

/**
 * BabyStepGiantStep
 */
public class BabyStepGiantStep {

  private final BigInteger base;
  private final BigInteger modulo;

  public BabyStepGiantStep(long p, long g) {
    this.modulo = BigInteger.valueOf(p);
    this.base = BigInteger.valueOf(g);
  }

  /**
   * calculates g^exp mod p for this Naive instance
   * 
   * @param exp
   * @return g^exp mod p
   */
  private BigInteger modPow(long exp) {
    return base.modPow(BigInteger.valueOf(exp), modulo);
  }

  /**
   * finds alice's & bob's secrets given the public keys.
   * 
   * @param publicA
   * @param publicB
   * @return every possibile a,b couple
   */

  public List<Integer> crack(long publicA, long publicB) {
    List<Integer> res = new ArrayList<Integer>();

    BabyStepGiantStepSolver solver = new BabyStepGiantStepSolver();

    List<Integer> aliceCandidates = solver.solve(publicA);
    List<Integer> bobCandidates = solver.solve(publicB);

    System.out.println(aliceCandidates.size());
    System.out.println(bobCandidates.size());

    // calculates the cartesian product of the two lists
    aliceCandidates.forEach(a -> bobCandidates.forEach(b -> {
      res.add(a);
      res.add(b);
    }));

    return res;
  }

  /**
   * <p>
   * given y, solves for x the DLP: y = g^x mod p.
   * </p>
   * 
   * <p>
   * let m = ceil(sqrt(p)) and i,j in [0,m[
   * </p>
   * <p>
   * find all i,j satisfying g^j = y*(g^-m)^i, and return x = i*m + j for each i,j
   * </p>
   */
  private class BabyStepGiantStepSolver {

    /**
     * sqrt(p)
     */
    private final long m;
    /**
     * a^-m mod p
     */
    private final BigInteger factor;

    /**
     * baby steps container
     */
    private final Map<BigInteger, Long> map;

    public BabyStepGiantStepSolver() {

      m = (long) Math.ceil(Math.sqrt(modulo.longValueExact()));
      factor = modPow(-m);

      // Stream<BigInteger> stream = Stream.iterate(BigInteger.ZERO, n ->
      // n.add(BigInteger.ONE)).limit(m).parallel();

      map = LongStream.rangeClosed(0, m).parallel().boxed().collect(toMap(x -> modPow(x), identity()));
    }

    /**
     * calculates the discrete logarithm using the baby-step giant-step algorithm
     *
     * @param arg the argument for which the DLP is to be solved
     * @return a value x such that g^x mod p = arg
     */
    public List<Integer> solve(long arg) {

      List<Integer> res = new ArrayList<Integer>();
      BigInteger gamma = BigInteger.valueOf(arg);

      // TODO better filter (parallel?)
      for (long i = 0; i < m; i++) {
        if (map.containsKey(gamma)) {
          long x = i * m + map.get(gamma);
          res.add((int) x);
        }
        gamma = gamma.multiply(factor).mod(modulo);
      }

      return res;
    }
  }

  public static void main(String[] args) {
    System.out.println("starting counter");
    long start = System.currentTimeMillis();
    long p = 128504093;
    long g = 10009;
    long publicA = 69148740;
    long publicB = 67540095;

    List<Integer> test = new BabyStepGiantStep(p, g).crack(publicA, publicB);

    System.out.println(test);

    long time = (System.currentTimeMillis() - start);
    System.out.printf("this execution took %s:%s.%s\n", (time / 1000) / 60, (time / 1000) % 60, time % 1000);

  }
}
