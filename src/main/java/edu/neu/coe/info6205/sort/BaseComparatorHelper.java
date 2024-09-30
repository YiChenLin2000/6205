package edu.neu.coe.info6205.sort;

import edu.neu.coe.info6205.util.Config;

import java.util.Comparator;
import java.util.Random;

public abstract class BaseComparatorHelper<X> extends BaseHelper<X> implements NonComparableHelper<X> {

    public Comparator<X> getComparator() {
        return comparator;
    }

    /**
     * Use compareTo on the X type to do a comparison.
     *
     * @param x1 the first X value.
     * @param x2 the second X value.
     * @return the result of x1.compareTo(x2).
     */
    public int pureComparison(X x1, X x2) {
        return comparator.compare(x1, x2);
    }

    public Helper<X> clone(String description, Comparator<X> comparator, int N) {
        throw new SortException("not implementable");
    }

    /**
     * Constructor for explicit random number generator.
     *
     * @param description  the description of this Helper (for humans).
     * @param comparator   the Comparator of X to be used.
     * @param n            the number of elements expected to be sorted. The field n is mutable so can be set after the constructor.
     * @param random       a random number generator.
     * @param instrumenter an instance of Instrument.
     * @param config       the configuration to be used.
     */
    public BaseComparatorHelper(String description, Comparator<X> comparator, int n, Random random, Instrument instrumenter, Config config) {
        super(description, random, instrumenter, config, n);
        this.comparator = comparator;
    }

    /**
     * Constructor for explicit seed.
     *
     * @param description  the description of this Helper (for humans).
     * @param comparator   the Comparator of X to be used.
     * @param n            the number of elements expected to be sorted. The field n is mutable so can be set after the constructor.
     * @param seed         the seed for the random number generator.
     * @param instrumenter an instance of Instrument.
     * @param config       the configuration to be used.
     */
    public BaseComparatorHelper(String description, Comparator<X> comparator, int n, long seed, Instrument instrumenter, Config config) {
        this(description, comparator, n, new Random(seed), instrumenter, config);
    }

    /**
     * Constructor to create a Helper with a random seed.
     *
     * @param description  the description of this Helper (for humans).
     * @param comparator   the Comparator of X to be used.
     * @param n            the number of elements expected to be sorted. The field n is mutable so can be set after the constructor.
     * @param instrumenter an instance of Instrument.
     */
    public BaseComparatorHelper(String description, Comparator<X> comparator, int n, Instrument instrumenter, Config config) {
        this(description, comparator, n, System.currentTimeMillis(), instrumenter, config);
    }

    /**
     * Constructor to create a Helper with a random seed and an n value of 0.
     *
     * @param description  the description of this Helper (for humans).
     * @param comparator   the Comparator of X to be used.
     * @param instrumenter an instance of Instrument.
     * @param config       the config.
     */
    public BaseComparatorHelper(String description, Comparator<X> comparator, Instrument instrumenter, Config config) {
        this(description, comparator, 0, instrumenter, config);
    }

    private final Comparator<X> comparator;
}