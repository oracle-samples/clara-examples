# clara-examples

Example usage of Clara.

## Usage

See the code for examples using Clara.

### In-memory
Simply typing ```lein run``` in the project's root directory will run the [simple, in-memory examples](https://github.com/rbrush/clara-examples/tree/master/src/main/clojure/clara/examples).

### ClojureScript
Compile the ClojureScript code with ```lein compile``` or ```lein cljsbuild once```, and then open the ```resources/public/index.html``` file in a browser will run the [ClojureScript examples](https://github.com/rbrush/clara-examples/blob/master/src/main/clojurescript/clara/examples/shopping.cljs) and display the result.

### Tools
Functionality from [clara-tools](https://github.com/rbrush/clara-tools), including browser-based views of the working memory and rule structures, can be run with the following command:

```lein run -m clara.examples.tools```

## License

Copyright © 2013 Ryan Brush

Distributed under the Eclipse Public License, the same as Clojure.
