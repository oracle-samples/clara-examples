package clara.examples.java;

import clara.rules.QueryResult;
import clara.rules.RuleLoader;
import clara.rules.WorkingMemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Main class for our example.
 */
public class ExampleMain {

    /**
     * Since the working memory is immutable, we can load it once and reuse it for every
     * new working memory we need. This doesn't really matter in this example, but would
     * be a good pattern for code that frequently creates sessions to evaluate a set of
     * objects.
     */
    static final WorkingMemory emptyMemory = RuleLoader.loadRules("clara.examples.java.shopping");

    /**
     * Query used in the example.
     */
    static final String QUERY = "clara.examples.java.shopping/get-promotions";

    /**
     * Main function for the example.
     */
    public static void main(String [] args) {

        // Create some facts to add to the working memory.
        List facts = Arrays.asList(new Customer("Tim", true), new Order(250));

        // Insert some facts and fire the rules.
        WorkingMemory memory = emptyMemory.insert(facts).fireRules();

        // Run the promotion query and print the results.
        for (QueryResult result: memory.query(QUERY)) {
            System.out.println("Query result: " + result.getResult("?promotion"));
        }
    }
}
