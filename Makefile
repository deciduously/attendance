.PHONY: help bundle clean deps release run test

SHELL       := /bin/bash
export PATH := ./bin:$(PATH)

project     = attendance
verfile     = version.properties
version     = $(shell grep ^version $(verfile) | sed 's/.*=//')
atom        = "$(project)-$(version)"
target      = target/
artifacts   = static/js/main.out/
frontend    = static/js/main.js
readme      = README.md
server      = "$(target)$(atom).jar"
license     = LICENSE

help:
	@echo "version =" $(version)
	@echo "Usage: make {bundle|clean|deps|help|release|run|test}" 1>&2 && false

clean:
	(rm -Rfv $(atom) $(artifacts) out/)
	(rm -fv .installed .tested .released .bundled $(frontend) "$(atom).zip" "$(atom).tar.xz")

bin/boot:
	(mkdir -p bin/                                                                             && \
	curl -fsSLo bin/boot https://github.com/boot-clj/boot-bin/releases/download/latest/boot.sh && \
	chmod 755 bin/boot)

deps: bin/boot

$(frontend):
	boot build

.bundled: $(frontend)
	cp -r $(target) "$(atom)/"         && \
	cp $(license) $(atom)              && \
	cp $(readme) $(atom)               && \
	cp $(verfile) $(atom)              && \
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
	@echo "Not yet!" && \
	date > .tested

test: .tested
