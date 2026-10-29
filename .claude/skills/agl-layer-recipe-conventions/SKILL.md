---
name: agl-layer-recipe-conventions
description: Where things live in the AGL layer repos meta-agl, meta-agl-demo and meta-agl-devel - sublayers, kas feature fragments, what yocto-check-layer covers, the upstream Gerrit workflow and the AI-assistant policy. Consult BEFORE adding a recipe, a layer, a kas feature fragment or a CI image to one of these repos, or before preparing a change for upstream AGL.
---

# Layers, features and contribution rules

This skill is a copy shared by meta-agl, meta-agl-demo and meta-agl-devel. It records the structure as found on 2026-10-29; check the tree before relying on it.

## Layout
- **meta-agl:** sublayers `meta-agl-core`, `meta-agl-bsp`, `meta-agl-drm-lease`, `meta-agl-flutter`, `meta-agl-ic`, `meta-agl-ivi`, `meta-agl-kuksa-val`, `meta-app-framework`, `meta-netboot`, `meta-pipewire`.
- **meta-agl-demo:** the repo root is a layer (`recipes-*`, `classes/`, `conf/`) plus `meta-agl-demo-shared` and `meta-agl-kvm-demo`.
- **meta-agl-devel:** its subdirectories are the real layers (`meta-agl-ic-container`, `meta-agl-xen`, `meta-agl-rdp`, `meta-agl-ros2`, `meta-agl-test`, `meta-uhmi`, `meta-virtio`, `meta-offline-voice-agent`, `meta-ekuiper`, `meta-nanomq`), not its root.
- **kas feature fragments** live in each repo under `kas/feature/<name>.yml` (the real fragment, with the layers the feature needs). agl-ucb has a one-line pointer per feature; a new feature needs both, plus an image entry in `ci/build-matrix.yaml` if CI should build it (skill `agl-layer-ci-caller`).
- External layers (poky parts, openembedded, BSPs, Qt, Flutter) are not in these repos: they are pinned in agl-ucb `kas/pins.yml`.

## Checking a layer
agl-ci's `validate.sh` runs yocto-check-layer on `meta-agl/meta-agl-core` by default. The layers of demo and devel are listed under `check_layers.layers` in their `ci/build-matrix.yaml` but `default: []` keeps the check off until they pass (open item M19); check one by hand with `CHECK_LAYERS="<path from check_layers.layers>" <agl-ci>/ci/scripts/validate.sh --machine qemux86-64 --target <image>` inside an agl-ucb tree (`CHECK_LAYERS=all` takes every listed layer; agl-ci has no Makefile).

## Contributing
- `.gitreview` in these repos points at AGL's Gerrit (`gerrit.automotivelinux.org`, project `AGL/<repo>`, branch `master`): changes meant for upstream AGL go through Gerrit. The GitHub repos of this organisation are where the shared CI runs and pull requests are rebase-merged.
- meta-agl has `ai-coding.md`, which points to the AGL documentation on AI coding assistants ("How To Contribute", "AI coding assistants"). Read it before using an AI tool on a change for upstream; this skill does not restate the policy because it could not be verified when it was written.
- Fix a recipe that belongs to another layer with a `.bbappend` in a layer of this repo, not by editing the original.
