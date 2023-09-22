[![PIA logo][pia-image]][pia-url]

# Private Internet Access

Private Internet Access is the world's leading consumer VPN service. At Private Internet Access we believe in unfettered access for all, and as a firm supporter of the open source ecosystem we have made the decision to open source our VPN clients. For more information about the PIA service, please visit our website [privateinternetaccess.com][pia-url] or check out the [Wiki][pia-wiki].

# Tunnel for Android

This library is based on the [VPN for Android][icsopenvpn] application. Almost all the files in this module are verbatim copies from the original project. The changes in this library are to allow it being build as a library and to include PIA's Killswitch functionality.


## Installation

### Requirements

- Android Studio with Android NDK installed
- Android 4.0+


## Documentation

The library consists from an almost unmodified OpenVPN binary that is build with cmake and a Android Java that controls the openvpn binary that is running as a regular linux process.


# OpenVPN integration in Android

## High level Overview

As high level picture, OpenVPN is compiled with a special TARGET for
Android and ship with the app. The OpenVPN binary is then launched by
the application and controlled via OpenVPN Management interface.

The communication between the Java code and OpenVPN is described in detail in
A detailed description of the Android specific management interface
and implementation is available in OpenVPN's source itself [doc/android.txt][android-management].


## Implementation

The classes *de.blinkt.openvpn.core* handle all the OpenVPN
abstraction. Called OpenVPN core in the following

## Interface to application

From a UI perspective there are few classes that are used to interact
with the OpenVPN core:

* *VPNProfile* This is a class that holds the configuration of a OpenVPN
  profile. It represent a parsed representation of a OpenVPN
  profile. There are also some extra settings in this class that
  modify the behaviour. For example the allowed app are also save in
  this class.

  The core needs these class to start a connection.

* *ConfigParser* A helper class that is used to parse a ovpn file and
  return a VPNProfile object. It allows to use existing config in
  OpenVPN config syntax.

* VPNStatus: The main class that allows the UI to get the status of
  the core. The class follows a listener pattern. There are a few
  listener callback implemented here. The callbacks will return on
  addListener also the last known status. They are indented to be
  registered/unregistered in the onStart/onStop or onResume/onPause
  pairs of activities that concern themselves with the status of the VPN.

    * LogListener, receives new messages each time when OpenVPN logs a
      message or the core logs messages. Not used in UI apart from
      tech support.

    * StateListener gets informed about the operational status of the
      VPN, generating config, connecting, authenticating, connected or
      authentication failed. Every activity that needs the status of
      the VPN should register to this as listener

    * ByteCountListener Gets the number of bytes in/out. Can be used
      to draw a graph or just show the current VPN activity like in
      notification.


# UI/Service separation

Android gets more and picky about what it allows as background
services and also a crash from the UI should not stop the VPN
connection. Also the background process is smaller, so it is less
likely to be killed under memory pressure. In the newer version, UI and
OpenVPN core are run in separate processes. The services that run in
the separate OpenVPN process have a ```android:process=":openvpn"```
attribute in the AndroidManifest.xml file.

Process separation is also used by other large apis (including Google's
own). While the process separation has advantages it also has disadvantages:

- Sparsely documented.
- No shared memory.
- Third party libraries often ignore that processes exists. This
  normally not a problem. For things like bus implementation you need
  to be aware that the bus is only per process and not per app.

Since there is no shared memory, all information between UI process
and OpenVPN process needs to explicitly send over. There is nothing
like a common singleton data holder class. The OpenVPN core does this transparently:

- AIDL (Android RPC) is used to communicate between the two
  processes. The interfaces aidl are in ```de.blinkt.openvpn.core```

- Application class inits StatusListener class that checks if it is in
  UI process or in OpenVPN process.

  - UI process: Bind the to ServiceStatus process of the openvpn
    process (this starts the process if not started already) and
    requests AIDL callback.

 - OpenVPN process: Setup a normal vpnstatus object that holds all
      information/logs to provide for a UI process



## Contributing

By contributing to this project you are agreeing to the terms stated in the Contributor License Agreement (CLA) [here](/CLA.rst).

For more details please see [CONTRIBUTING](/CONTRIBUTING.md).

Issues and Pull Requests should use these templates: [ISSUE](/.github/ISSUE_TEMPLATE.md) and [PULL REQUEST](/.github/PULL_REQUEST_TEMPLATE.md).

## Authors

- Arne Schwabe - [schwabe](https://github.com/schwabe)

## License

This project is licensed under the [GPNU General Public License v2.0 (GPL2)](https://choosealicense.com/licenses/gpl2-2.0/), which can be found [here](/LICENSE.GPL2).

## Acknowledgements

This product includes software developed by the OpenSSL Project for use in the OpenSSL Toolkit. ([https://www.openssl.org/][dep-openssl])

© 2002-2018 OpenVPN Inc. - OpenVPN is a registered trademark of OpenVPN Inc.

[pia-image]: https://www.privateinternetaccess.com/assets/PIALogo2x-0d1e1094ac909ea4c93df06e2da3db4ee8a73d8b2770f0f7d768a8603c62a82f.png
[pia-url]: https://www.privateinternetaccess.com/
[pia-wiki]: https://en.wikipedia.org/wiki/Private_Internet_Access
[icsopenvpn]: https://github.com/schwabe/ics-openvpn
[openvpn]: https://openvpn.net/index.php/open-source/overview.html
[android-management][https://github.com/OpenVPN/openvpn/blob/master/doc/android.txt]
