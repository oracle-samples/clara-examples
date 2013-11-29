# clara-examples

Example usage of Clara.

## Usage

See the code for examples using Clara. 

### In-memory
Simply typing ```lein run``` in the project's root directory will run the [simple, in-memory examples](https://github.com/rbrush/clara-examples/tree/master/src/main/clojure/clara/examples).

### ClojureScript
Compile the ClojureScript code with ```lein compile``` or ```lein cljsbuild once```, and then open the ```resources/public/index.html``` file in a browser will run the [ClojureScript examples](https://github.com/rbrush/clara-examples/tree/master/rc/main/clojurescript/clara/examples) and display the result.

### Storm
Typing ```lein run -m clara.examples.storm``` will start an in-process Storm topology and run the storm example.

## License

Copyright © 2013 Ryan Brush

Distributed under the Eclipse Public License, the same as Clojure.
