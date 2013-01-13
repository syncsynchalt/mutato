#!/bin/sh -e -x
git clone git://github.com/syncsynchalt/mutato.git mutato-latest
(cd mutato-latest && mvn package)
zip -qr mutato-latest.zip mutato-latest
scp mutato-latest.zip w.ulfheim.net:/var/www/files/mutato/
rm mutato-latest.zip
rm -rf mutato-latest
