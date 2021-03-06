require automake.inc
LICENSE="GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe" 
DEPENDS_virtclass-native = "autoconf-native"

RDEPENDS_${PN} += "\
    autoconf \
    perl \
    perl-module-bytes \
    perl-module-constant \
    perl-module-cwd \
    perl-module-data-dumper \
    perl-module-dynaloader \
    perl-module-errno \
    perl-module-exporter-heavy \
    perl-module-file-basename \
    perl-module-file-compare \
    perl-module-file-copy \
    perl-module-file-glob \
    perl-module-file-spec-unix \
    perl-module-file-stat \
    perl-module-getopt-long \
    perl-module-io \
    perl-module-io-file \
    perl-module-posix \
    perl-module-strict \
    perl-module-text-parsewords \
    perl-module-vars "

RDEPENDS_${PN}_virtclass-native = "autoconf-native perl-native-runtime"
RDEPENDS_${PN}_virtclass-nativesdk = "autoconf-nativesdk"

PATHFIXPATCH = "file://path_prog_fixes.patch"
PATHFIXPATCH_virtclass-native = ""
PATHFIXPATCH_virtclass-nativesdk = ""

PERLPATH = "${bindir}/perl"
PERLPATH_virtclass-native = "/usr/bin/perl"
PERLPATH_virtclass-nativesdk = "/usr/bin/perl"

SRC_URI += "${PATHFIXPATCH} \
	    file://prefer-cpio-over-pax-for-ustar-archives.patch \
	    file://python-libdir.patch \
            file://py-compile-compile-only-optimized-byte-code.patch"

SRC_URI[md5sum] = "ec25c1855cacf47e4bdee76a776b96ba"
SRC_URI[sha256sum] = "24bf1640679ba4a9cbe2d36422f39a81eced7f556b576a7a2ccfc70ca85a1e2f"

PR = "r0"

do_install () {
    oe_runmake 'DESTDIR=${D}' install
    install -d ${D}${datadir}

    # Some distros have both /bin/perl and /usr/bin/perl, but we set perl location
    # for target as /usr/bin/perl, so fix it to /usr/bin/perl.
    for i in aclocal aclocal-1.11 automake automake-1.11; do
        if [ -f ${D}${bindir}/$i ]; then
            sed -i -e '1s,#!.*perl,#! ${PERLPATH},' \
            -e 's,exec .*/bin/perl \(.*\) exec .*/bin/perl \(.*\),exec ${PERLPATH} \1 exec ${PERLPATH} \2,' \
            ${D}${bindir}/$i
        fi
    done
}

BBCLASSEXTEND = "native nativesdk"
