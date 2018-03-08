# Attendance
Attendance solver in Reagent.  No network connection required.
### Requirements
To run, a web browser.  To develop:
* `java`
* `git`
* [`boot`](http://boot-clj.com)
### Usage
Download the [latest release](https://github.com/deciduously/attendance/releases/tag/v0.1.6) and decompress it.  Execute the jar by invoking `java -jar attendance-v0.2.0/attendance-v0.2.0.jar` and point your browser to `localhost:3000`.

Example roster.csv:
```
A,7,Name One,Name Two,Name Three,,,,
B,9,Name Four,Name Five,Name Six,,,,
CE,4,Name Two,,,,,,
```
Trailing commas result from our use of MS Excel to export the roster, and are ignored.

Example extra.csv:
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

`boot run` will start a hot-reloading development environment.  Invoke `boot repl -c` in a new terminal and run `(start-repl)` to use a CLJS repl.

To connect to CIDER, run `boot repl -s run` and use `M-x cider-connect` to `localhost` and the given `nREPL` port (not `9009`, this is the frontend REPL).

`make test` will run an optimized production build at `target/`, build a release bundle at `attendance-v0.2.0/`, and create a `.zip` and a `.tar.xz` of the bundle at the project root.  It will then eventually run the test suite.

Use `make install` for just the build, `make bundle` to put together the release bundle, and/or `make release` to produce the compressed archives.
### Contributing
Sure!  Open a PR.
### Errata
Thanks due to [mrmcc3](https://github.com/mrmcc3), whose [blog post](https://mrmcc3.github.io/post/csv-with-clojurescript) was a great push in the right direction for the file upload.
