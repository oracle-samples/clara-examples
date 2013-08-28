package clara.examples.java;

/**
 * Java bean used in example rules.
 */
public class Order {

    /**
     * The order's total.
     */
    private final int total;

    /**
     * Creates an order with the given total.
     * @param total
     */
    public Order(int total) {
        this.total = total;
    }

    /**
     * Returns the order's total.
     */
    public int getTotal() {
        return total;
    }

    public boolean equals(Object that) {

        if (!(that instanceof  Order)) {
            return false;
        }

        Order _that = (Order) that;

        return this.total == _that.total;
    }

    public int hashCode() {
        return 17 * total;
    }
}
