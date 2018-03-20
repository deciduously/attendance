.PHONY: help bundle clean deps release run test

SHELL       := /bin/bash
export PATH := ./bin:$(PATH)

project     = attendance
verfile     = version.properties
version     = $(shell grep ^version $(verfile) | sed 's/.*=//')
atom        = "$(project)-$(version)"
frontend    = frontend/js/main.js
server      = target/release/attendance-server
readme      = README.md
license     = LICENSE

help:
	@echo "version =" $(version)
	@echo "Usage: make {bundle|clean|deps|help|release|run|test}" 1>&2 && false

clean:
	(rm -Rfv $(atom) frontend/ $(server))
	(rm -fv .tested .released .bundled target/debug/attendance "$(atom).zip" "$(atom).tar.xz")

bin/boot:
	(mkdir -p bin/                                                                             && \
	curl -fsSLo bin/boot https://github.com/boot-clj/boot-bin/releases/download/latest/boot.sh && \
	chmod 755 bin/boot)

deps: bin/boot

$(frontend):
	boot build

$(server):
	cargo build --release

.bundled: $(frontend) $(server)
	cp -r frontend/ $(atom)       && \
	rm -r "$(atom)/js/main.out/"  && \
	rm "$(atom)/js/main.cljs.edn" && \
	cp $(server) $(atom)          && \
	cp Rocket.toml $(atom)        && \
	cp $(license) $(atom)         && \
	cp $(readme) $(atom)          && \
	cp $(verfile) $(atom)         && \
	date > .bundled

bundle: .bundled

.released: .bundled
	zip -r "$(atom).zip" $(atom)                        && \
	tar -cf - $(atom) | xz -9e -c - > "$(atom).tar.xz"  && \
	date > .released

release: .released

run: $(frontend)
	cargo run

.tested: clean .released
	echo "Testing Backend"  && \
	cargo test              && \
	echo "Testing Frontend" && \
	echo "Not Yet"
	date > .tested

test: .tested
