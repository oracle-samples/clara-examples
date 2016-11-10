# _About_

This project contains example use [clara-rules](http://github.com/cerner/clara-rules), a Clojure-based rule engine. It is primarily for documentation and examples for those learning Clara.

# _Usage_

The Clojure examples can be run via the [Leiningen](http://leiningen.org) build system. Simply check out this project and run the following from its root directory on a system where Leiningen is installed:

```bash
lein run -m clara.examples
```

To run the [ClojureScript](http://clojurescript.org) examples, compile the code with ```lein compile``` or ```lein cljsbuild once```, and then open the ```resources/public/index.html``` file in a browser. This will run the [shopping example](https://github.com/cerner/clara-examples/blob/master/src/main/clojurescript/clara/examples/shopping.cljs) in ClojureScript and display the result.

# _Building_

Like clara-rules, this example project is built, tested, and deployed using [Leiningen](http://leiningen.org).  

# _Communication_

Questions can be posted to the [Clara Rules Google Group](https://groups.google.com/forum/?hl=en#!forum/clara-rules) or the [Slack channel](https://clojurians.slack.com/messages/clara/).  

# Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md)

# LICENSE

Copyright 2016 Cerner Innovation, Inc.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

&nbsp;&nbsp;&nbsp;&nbsp;http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.


