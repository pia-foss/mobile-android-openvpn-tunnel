// IPIAServiceStatus.aidl
package com.privateinternetaccess.android;

// Declare any non-default types here with import statements

import com.privateinternetaccess.android.IPIAStatusCallbacks;

interface IPIAServiceStatus {
    void registerStatusCallback(in IPIAStatusCallbacks cb);
    void unregisterStatusCallback(in IPIAStatusCallbacks cb);

    void stopKillSwitchIfActive();

}
