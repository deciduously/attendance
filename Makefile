.PHONY: help bundle clean deps install release test

SHELL       := /bin/bash
export PATH := ./bin:$(PATH)

project     = attendance
verfile     = version.properties
version     = $(shell grep ^version $(verfile) | sed 's/.*=//')
atom        = "$(project)-$(version)"
release     = release/
target      = target/
readme      = README.md
license     = LICENSE

help:
	@echo "version =" $(version)
	@echo "Usage: make {bundle|clean|deps|help|install|release|test}" 1>&2 && false

clean:
	(rm -Rfv $(project) $(release) $(target))
	(rm -fv .installed .tested .released .bundled)

bin/boot:
	(mkdir -p bin/                                                                             && \
	curl -fsSLo bin/boot https://github.com/boot-clj/boot-bin/releases/download/latest/boot.sh && \
	chmod 755 bin/boot)

deps: bin/boot

.installed:
	boot build        && \
	date > .installed

install: .installed

.bundled: .installed
	$(shell cp -r $(target) "$(atom)/" && \
	cp $(license) $(atom)              && \
	cp $(readme) $(atom)               && \
	cp $(verfile) $(atom))
	date > .bundled

bundle: .bundled

trim:
	rm -Rfv "$(atom)/js/main.out/"    && \
	rm -fv  "$(atom)/js/main.cljs.edn"

.released: .bundled trim
	$(shell mkdir $(release)                                     && \
	zip -r "$(release)$(atom).zip" $(atom)                       && \
	tar -cf - $(atom) | xz -9e -c - > "$(release)$(atom).tar.xz")
	date > .released

release: .released

.tested: clean .released
	@echo "Not yet!"

test: .tested
