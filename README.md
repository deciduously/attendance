# Attendance
Attendance solver in Reagent.  No network connection required.
### Requirements
* `java` 8+
* `git`

If you want to interact with the code you will also need [`boot`](http://boot-clj.com), which can be obtained by running `make deps`.
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
These are available at [resource/data/mock_roster.csv](https://github.com/deciduously/attendance/blob/master/resource/data/mock_roster.csv) and [mock_extra.csv](https://github.com/deciduously/attendance/blob/master/resource/data/mock_extra.csv).
### Caveats
The app currently cannot handle name collisions, and it's not a high priority as we currently have none in real world use.  The extra hours are only used for the generated report - any name with a time block that goes over 4 (e.g. 4-6, not 1-4) will be added to the corresponding Extended Day class.

It's also pretty specific to one setup for now.  More config options on the way (hopefully).
### Development
Clone or download this repository.

`boot run` leans on `cljs-repl-env` to facilitate use with Emacs/CIDER.  Use `M-x cider-jack-in-clojurescript`, and call the task with a future: `(def p (future (boot (run))))`.  Once this loads you can invoke `(start-repl)` in the boot REPL buffer and connect your browser's devtools at `localhost:3000`.  Make sure you're hooked up with something like `(js/alert "Hello from Emacs!")`.

`make test` will run an optimized production build at `target/`, build a release bundle at `attendance-v0.2.0/`, and create a `.zip` and a `.tar.xz` of the bundle at the project root.  It will then eventually run the test suite.

Use `make install` for just the build, `make bundle` to put together the release bundle, and/or `make release` to produce the compressed archives.
### Contributing
Sure!  Open a PR.
### Errata
Thanks due to [mrmcc3](https://github.com/mrmcc3), whose [blog post](https://mrmcc3.github.io/post/csv-with-clojurescript) was a great push in the right direction for the file upload.
