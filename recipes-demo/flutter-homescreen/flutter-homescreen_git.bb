SUMMARY = "AGL Flutter Homescreen"
DESCRIPTION = "Demo Flutter homescreen for Automotive Grade Linux."
HOMEPAGE = "https://gerrit.automotivelinux.org/gerrit/apps/flutter-homescreen"
SECTION = "graphics"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI = "git://gerrit.automotivelinux.org/gerrit/apps/flutter-homescreen;protocol=https;branch=${AGL_BRANCH} \
           file://flutter-homescreen-debug.json \
           file://flutter-homescreen-profile.json \
           file://flutter-homescreen-release.json \
           file://flutter-homescreen.service \
"
SRCREV = "e21709c9601209e26d09dea0a45e37f0636bb605"

S = "${WORKDIR}/git"

PUBSPEC_APPNAME = "flutter_homescreen"
FLUTTER_APPLICATION_INSTALL_PREFIX = "/flutter"

FLUTTER_BUILD_ARGS = "bundle -v"

inherit flutter-app

APP_CONFIG = "flutter-homescreen-release.json"
APP_CONFIG:class-runtimedebug = "flutter-homescreen-debug.json"
APP_CONFIG:class-runtimeprofile = "flutter-homescreen-profile.json"

do_install:append() {
    install -D -m 0644 ${WORKDIR}/flutter-homescreen.service ${D}${systemd_user_unitdir}/flutter-homescreen.service
    install -d ${D}${systemd_user_unitdir}/agl-session.target.wants
    ln -s ../flutter-homescreen.service ${D}${systemd_user_unitdir}/agl-session.target.wants/flutter-homescreen.service

    install -D -m 0644 ${WORKDIR}/${APP_CONFIG} ${D}${datadir}/flutter/flutter-homescreen.json
}

FILES:${PN} += "${datadir} ${systemd_user_unitdir}"
