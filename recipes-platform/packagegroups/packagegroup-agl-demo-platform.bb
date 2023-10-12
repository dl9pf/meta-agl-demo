SUMMARY = "The software for DEMO platform of AGL IVI profile"
DESCRIPTION = "A set of packages belong to AGL Demo Platform"

LICENSE = "MIT"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = "\
    packagegroup-agl-demo-platform \
    "

RDEPENDS:${PN} += "\
    packagegroup-agl-image-ivi \
    packagegroup-agl-kuksa-val-databroker \
    packagegroup-agl-demo \
    "

AGL_APPS = " \
    dashboard \
    hvac \
    ondemandnavi \
    settings \
    mediaplayer \
    messaging \
    phone \
    radio \
    window-management-client-grpc \
    camera-gstreamer \
    "

RDEPENDS:${PN}:append = " \
    weston-ini-conf-no-activate \
    ${@bb.utils.contains('DISTRO_FEATURES', 'agl-devel', 'unzip mpc' , '', d)} \
    psplash-portrait-config \
    "

#    homescreen \
#    launcher \
#    qtquickcontrols2-agl \
#    qtquickcontrols2-agl-style \
#