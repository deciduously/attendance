# Attendance
Full-stack attendance-taking app in Rust and ClojureScript.
### Requirements
This app requires Linux and an HTML5-enabled web browser.  Cross-compiling hsn't been attempted yet, but it should also work with Windows.  It was written to be used with Chrome, and developed between Chrome and Firefox - I haven't tested it with anything else yet.

If you want to interact with the code you will also need `git`, `make`, [`rust`](https://rust-lang.org), a Java 8+ JRE, and [`boot`](http://boot-clj.com), which can be obtained by running `make deps`.  The fastest way to obtain a Rust installation is via [`rustup`](https://www.rustup.rs).
### Usage
Download the [latest release](https://github.com/deciduously/attendance/releases/tag/v0.3.1-r1) and decompress it.  Run the executable `attendance-server` (e.g. `cd attendance-v0.3.1/ && ./attendance-server`) and point your browser to `localhost:3000` (or click the URL shown if your terminal emulator is fancy).  `Rocket.toml` can be used to set the target port and other [config options](https://rocket.rs/guide/configuration/#rockettoml).

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
Larger mock rosters that more closely approximate real world use are available at [static/data/mock_roster.csv](https://github.com/deciduously/attendance/blob/master/static/data/mock_roster.csv) and [mock_extra.csv](https://github.com/deciduously/attendance/blob/master/static/data/mock_extra.csv).  These can be loaded via the Load Mock Roster button to play around with.
### Caveats
The app currently cannot handle name collisions, and it's not a high priority as we currently have none in real world use.

The extra hours are only used for the generated report - any name with a time block that goes over 4 (e.g. 4-6, not 1-4) will be added to the corresponding Extended Day class.  Core hour kids are currently discarded.

It's also pretty specific to one setup for now.  More config options on the way (hopefully).
### Development
Clone or download this repository.  Use `make run` to build the frontend and compile and launch the server.  I broke hot reloading when I switched out the Clojure backend for Rust, stay tuned.

`make test` will build a release bundle at `attendance-v0.3.1/`, and create a `.zip` and a `.tar.xz` of the bundle at the project root.  It will then eventually run the test suite.

Use `make bundle` to put together the release bundle, and/or `make release` to produce the compressed archives from that bundle.  `make clean` is also available to clear up build artifacts and old archives, and is called first by `make test`.
### Libraries
Backend:
* [Rocket](https://rocket.rs)
* [Diesel](http://diesel.rs/)
* [r2d2](https://github.com/sfackler/r2d2)/[r2d2-diesel](https://github.com/diesel-rs/r2d2-diesel)
* [dotenv](https://github.com/purpliminal/rust-dotenv)

Frontend:
* [Reagent](https://reagent-project.github.io)
* [cljs-ajax](https://github.com/JulianBirch/cljs-ajax)

Utility:
* [specter](https://github.com/nathanmarz/specter)
### Contributing
Sure!  Open a PR.
### Acknowledgements
Thanks due to [mrmcc3](https://github.com/mrmcc3), whose [blog post](https://mrmcc3.github.io/post/csv-with-clojurescript) was a great push in the right direction for the file upload, and [StefanoOrdine](https://github.com/StefanoOrdine) for their [Rocket/React](https://github.com/StefanoOrdine/rust-reactjs) example.
