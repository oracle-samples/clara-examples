package clara.examples.java;

/**
 * Java bean used in example rules.
 */
public class Promotion {

    /**
     * Description of the promotion.
     */
    private final String description;

    /**
     * Creates a promotion with the given description.
     */
    public Promotion(String description) {
        this.description = description;
    }

    public boolean equals(Object that) {
        if (!(that instanceof Promotion)) {
            return false;
        }

        Promotion _that = (Promotion) that;

        return this.description.equals(_that.description);
    }

    public int hashCode() {
        return 17 * description.hashCode();
    }

    public String toString() {
        return "DESCRIPTION: " + description;
    }

   public String getDescription() {
        return description;	
   }
}
