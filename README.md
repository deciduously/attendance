# Attendance
Full-stack attendance-taking app in Clojure/ClojureScript.  **EXPERIMENTAL** Rust backend. For now, app can be built and served by invoking `boot build && cargo run`.  The Makefile will follow, and v0.2.0 will stay up for posterity.

It turns out I will not have access to a server capable of running a JRE - this will be an attempt to remove runtime dependencies.  Rust seemed like a good choice.

I'm very much using this project to learn how these tools work.  I've intentionally eschewed curated frameworks and templates, preferring to build the project up myself.  As a result I'm probably doing some really nasty stuff in here that will need to be changed, and anything that does work is purely by accident.  Bring hazmat goggles.

That said, a lot of it *does* work!  v0.2.0 has been in daily use in the target work environment since release.  Any and all credit for that goes to Clojure itself and the talented library designers.  If you haven't yet, get you some!  Functional programming is supposed to be fun.
### Requirements
This app requires a Java 8+ JRE present and an HTML5-enabled web browser.  It was written to be used with Chrome, and developed between Chrome and Firefox - I haven't tested it with anything else yet.

If you want to interact with the code you will also need `git`, `make`, and [`boot`](http://boot-clj.com), which can be obtained by running `make deps`.
### Usage
Download the [latest release](https://github.com/deciduously/attendance/releases/tag/v0.2.0) and decompress it.  Execute the jar by invoking `java -jar attendance-v0.2.0/attendance-v0.2.0.jar` and point your browser to `localhost:3000`.  It uses `version.properties` for the version number and reads the following environment variables, given with their defaults:
```shell
PORT=3000
```
Example roster.csv input:
```
A,7,Name One,Name Two,Name Three,,,,
B,9,Name Four,Name Five,Name Six,,,,
CE,4,Name Two,,,,,,
```
Trailing commas result from our use of MS Excel to export the roster, and are ignored.

Example extra.csv input:
```
Name One,1-4
Name Two,4-6
```
Larger mock rosters that more closely approximate real world use are available at [resources/public/data/mock_roster.csv](https://github.com/deciduously/attendance/blob/master/resources/public/data/mock_roster.csv) and [mock_extra.csv](https://github.com/deciduously/attendance/blob/master/resources/public/data/mock_extra.csv).  These can be loaded via the Load Mock Roster button to play around with.
### Caveats
The app currently cannot handle name collisions, and it's not a high priority as we currently have none in real world use.

The extra hours are only used for the generated report - any name with a time block that goes over 4 (e.g. 4-6, not 1-4) will be added to the corresponding Extended Day class.  Core hour kids are currently discarded.

It's also pretty specific to one setup for now.  More config options on the way (hopefully).
### Development
Clone or download this repository.

`boot run` starts an nREPL server.  Once this loads you can invoke `Mx cider-connect` and provide the nREPL port given.  From there you can run `(start-repl)` and connect your browser's devtools at `localhost:3000`.  Make sure you're hooked up with something like `(js/alert "Hello from Emacs!")`.

`make test` will run an optimized production build at `target/`, build a release bundle at `attendance-v0.2.0/`, and create a `.zip` and a `.tar.xz` of the bundle at the project root.  It will then eventually run the test suite.

Use `make bundle` to put together the release bundle, and/or `make release` to produce the compressed archives from that bundle.  `make clean` is also available to clear up build artifacts and old archives, and is called first by `make test`.
### Libraries
Backend:
* [Ring](https://github.com/ring-clojure/ring)
* [H2](http://h2database.com/) via [HugSQL](https://www.hugsql.org/)
* [bidi](https://github.com/juxt/bidi) for routing

Frontend:
* [Reagent](https://reagent-project.github.io)
* [cljs-ajax](https://github.com/JulianBirch/cljs-ajax)

Utility:
* [specter](https://github.com/nathanmarz/specter)
* [docjure](https://github.com/mjul/docjure)
### Contributing
Sure!  Open a PR.
### Acknowledgements
Thanks due to [mrmcc3](https://github.com/mrmcc3), whose [blog post](https://mrmcc3.github.io/post/csv-with-clojurescript) was a great push in the right direction for the file upload.
