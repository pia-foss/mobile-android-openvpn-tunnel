#! /bin/bash
# This script should only be used if you know what you doing and requires careful inspection/modification after copying

for path in java/de/blinkt/openvpn/core/ java/de/blinkt/openvpn/VpnProfile.java
do
            rm -rv $path
            rsync -Pa ~/software/icsopenvpn/main/src/main/$path $path
done
