---
name: agl-layer-ci-caller
description: How CI works for this AGL layer repo - the thin caller .github/workflows/ci.yml, the shared engine in dl9pf-agl/agl-ci, ci/build-matrix.yaml (images, machines, tiers, needs), what a pull request builds, what a build costs, how to add an image or bump the pin. Consult BEFORE editing .github/workflows/ci.yml or ci/build-matrix.yaml, adding an image or machine, or when a CI run is red, empty or slow.
---

# CI of this layer repo (caller + matrix)

This skill is a copy shared by meta-agl, meta-agl-demo and meta-agl-devel. The source of truth is the agl-ci manual (`docs/manual/13-layer-ci-engine.md`, chapter 15 for mirrors and drills) and the agl-ci skills.

## Parts
- `.github/workflows/ci.yml`: only triggers and one `uses: dl9pf-agl/agl-ci/.github/workflows/layer-ci.yml@<40-char SHA>`. All logic is in agl-ci; a fix or a new limit arrives only when the pin is bumped, by hand through a pull request (these repos have no Dependabot). The repo requires full-SHA pins. meta-agl also has the nightly schedule (`cron: '17 1 * * *'`, 01:17 UTC) and a `fetch` job that fills the download cache; demo and devel have no schedule: the nightly of meta-agl builds their images through `needs`.
- `ci/build-matrix.yaml`: this repo's images. Fields: `needs: [<repo>]` (demo needs meta-agl, devel needs demo), `machine_eula`, and `images:` entries `{target, features, sdk, machines, tiers}`. `machines:` is a list or a shared name from agl-ci (`ci` = qemux86-64, qemuarm64, raspberrypi5, sparrow-hawk); `tiers:` is a list of `push-pr|nightly|release|dispatch` or a preset (`gate`, `nightly`, `nightly-x86`, `dispatch`, defined in agl-ci `ci/matrix-defaults.yaml`). `check_layers:` lists layers for yocto-check-layer; `default: []` keeps it off (M19).
- `.github/CODEOWNERS`: the owner reviews `/.github/`, `/.claude/` and `/ci/`.

## What runs when (selection of 2026-10-29)
CI builds **qemux86-64 only**. The pull-request gate is a single build: `agl-image-minimal` of meta-agl. A pull request of demo or devel selects **no build**: its `matrix` job warns about an empty matrix and `ci / result` is green. The nightly builds `agl-image-minimal`, `agl-ivi-demo-flutter`, `agl-kvm-demo` and `agl-xen-image-test`; every other image runs by dispatch (`workflow_dispatch`, tier `dispatch`). A pull request always runs tier `push-pr` and mode `build`; the pool comes from the event (same-repo: trusted `agl-pr`; fork: untrusted `agl-fork`).

## Cost
Two trusted slots. `agl-image-minimal` takes about 26 minutes warm and 4.5 hours cold; the Qt and Flutter demo images take 20 hours or more cold (job limit 24 h). A push to a pull request cancels its run in progress. The required check is `ci / result`; merge by rebase only.

## Add or change an image
1. Add the entry. The features must exist as `kas/feature/<f>.yml` in the repo that owns them and as a pointer in agl-ucb. 2. Check it: `AGL_TREE=<tree with the three repos> python3 <agl-ci>/ci/scripts/_matrix.py` prints the expanded entries (needs PyYAML). 3. Names are plain (`[A-Za-z0-9._-]`), at most 128 entries, and no EULA machine in `push-pr`; the matrix of a pull request is untrusted input and is validated. 4. A change of the matrix of a repo with a running cold build cancels that run when pushed to its pull request.

## Bump the pin
Take the SHA of the agl-ci `main` commit you want, replace it in every `uses:` line, keep the `# main (agl-ci PR #n)` comment, run `actionlint`. Nothing else changes for the caller.
