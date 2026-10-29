---
name: agl-layer-local-repro
description: How to reproduce a CI build or failure of this AGL layer repo on a desk - agl-ucb and scripts/aglsetup.sh, dev mode for local layer edits, the CI builder image, rebuilding the exact commits of a CI run from its resolved pins, shared caches, long builds. Consult BEFORE trying to reproduce a red CI run, test a layer change by hand or build a target locally.
---

# Reproduce a CI build locally

This skill is a copy shared by meta-agl, meta-agl-demo and meta-agl-devel. Details: the agl-ucb skills (`agl-ucb-getting-started`, `-kas-fragments`, `-pins-and-releases`) and the agl-ci manual chapter 13.

## The normal way
```
git clone https://github.com/dl9pf-agl/agl-ucb && cd agl-ucb
scripts/aglsetup.sh --list-targets                # target -> features -> owning repo
scripts/aglsetup.sh -m qemux86-64 <features>      # kas-container shell; then: bitbake <target>
```
kas clones the three AGL layers into the agl-ucb checkout. To edit this repo: run once without `--dev` (clones), then always `scripts/aglsetup.sh --dev ...`; without it kas hard-resets a clean clone to the branch tip and drops unpushed commits.

## Same builder as CI
CI uses the signed builder image, pinned by digest in agl-ci `ci/builder.digest`: `export KAS_CONTAINER_IMAGE=ghcr.io/dl9pf-agl/agl-ci/builder@sha256:<digest>`. The default upstream kas image lacks the lint tools. A permission error from Docker although you are in group `docker` is a stale group in the shell.

## The exact commits of a CI run
Floating layers mean a run is not described by `kas/pins.yml` alone. The `matrix` job of every run prints "Resolved pins of this run" in its summary: a kas fragment with the commit of every repo. Save it as `resolved-pins.yml` in the agl-ucb checkout and build with it in place of `kas/pins.yml`: `kas-container build kas/base.yml:kas/machine/<m>.yml:kas/feature/<f>.yml:resolved-pins.yml` (set `KAS_TARGET=<target>`). `aglsetup.sh` does not take this file. The commits must be on the declared branch (`master`), otherwise kas fetches.

## Caches and long builds
Share caches across builds with `AGL_SSTATE_DIR` and `AGL_DL_DIR` (aglsetup.sh mounts them). A cold build takes hours: run it detached with a log file (`setsid bash -c '... > build.log 2>&1' < /dev/null > /dev/null 2>&1 & disown`), never as a foreground command that can be killed; a kill mid-compile corrupts artifacts. `Sstate summary: ... (0% match ...)` in the log means a full rebuild is ahead.

## Run the CI engine itself
`AGL_TREE=$PWD CI=true <agl-ci>/ci/scripts/validate.sh --machine qemux86-64 --target agl-image-minimal`, then `build.sh` with the same arguments (agl-ci manual chapter 13, "Running on a desk"). `CI=true` adds the CI-only fragment.
