SUMMARY = "A digital instrument cluster written in Rust and Slint, built for Automotive Grade Linux."
HOMEPAGE = "https://github.com/signal-slot/agl-slint-cluster"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1cd9f3535d038f9d1bb320918e6474b1"

PV = "0.1"

SRC_URI = " \
    git://github.com/signal-slot/agl-slint-cluster.git;protocol=https;branch=main \
    file://agl-slint-cluster.service \
"
SRCREV = "37e37108d94a577e9f9beb85fe4c1901d0cc9507"

inherit cargo cargo-update-recipe-crates systemd agl-slint

require ${BPN}-crates.inc
CARGO_MANIFEST_PATH = "${S}/rust/Cargo.toml"

SYSTEMD_SERVICE:${PN} = "agl-slint-cluster.service"

do_install:append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/agl-slint-cluster.service ${D}${systemd_unitdir}/system/
}
