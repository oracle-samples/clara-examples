package clara.examples.java;

/**
 * Java bean used in example rules.
 */
public class Customer {

    /**
     * Customer name;
     */
    private final String name;

    /**
     * VIP status.
     */
    private final boolean vip;

    /**
     * Creates a customer with the given name and status.
     */
    public Customer (String name, boolean vip) {
        this.name = name;
        this.vip = vip;
    }

    /**
     * Returns true if the customer is a VIP.
     */
    public boolean isVIP() {
        return vip;
    }

    /**
     * Returns the name of the customer.
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return "VIP: " + isVIP();
    }

    public boolean equals(Object that) {

        if (!(that instanceof  Customer)) {
            return false;
        }

        Customer _that = (Customer) that;

        return this.name.equals(_that.name) &&
               this.vip == _that.vip;
    }

    public int hashCode() {
        return 17 * name.hashCode() * (vip ? 1 : 2);
    }
}
