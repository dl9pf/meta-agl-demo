# AGENTS.md

This is the AGL layer repository `meta-agl-demo`. It holds the demo images and recipes; its images are built by the nightly of meta-agl through the `needs` chain. CI is not defined here: `.github/workflows/ci.yml` calls the reusable workflow of `dl9pf-agl/agl-ci` (pinned by commit), and `ci/build-matrix.yaml` lists the images that CI builds. Skills for working here are in `.claude/skills/`: `agl-layer-ci-caller`, `agl-layer-local-repro`, `agl-layer-recipe-conventions`. Build locally through `agl-ucb` (`scripts/aglsetup.sh`).

## Working rules
- Show the complete commit message and the complete diff before any push or pull request; do not summarise them.
- Before every push: fetch, check that the pull request is still open and whether the branch needs a rebase onto its base; push only then.
- Name pull requests with their repository (`meta-agl#8`, not `#8`): the numbers repeat across repos.
- Rebase-merge only (the ruleset allows nothing else). Only commit or push when asked.
- A push to a pull request cancels its CI run in progress; do not push to a pull request with a cold build running without saying so.
- No links to chat sessions in commit messages or pull request texts; no tokens and no host addresses in committed files.
- Changes under `.github/`, `.claude/` and `ci/` are reviewed by the owner (`.github/CODEOWNERS`).

## AI assistants
AGL tracks AI involvement in contributions. The AGL documentation on AI coding assistants is linked from `ai-coding.md` in meta-agl; follow it for the commit trailer. This file does not restate the policy.
