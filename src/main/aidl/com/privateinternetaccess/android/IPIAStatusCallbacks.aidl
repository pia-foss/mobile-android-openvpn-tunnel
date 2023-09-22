/*
 * Copyright (c) 2012-2016 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package com.privateinternetaccess.android;


interface IPIAStatusCallbacks {
    /**
     * Called when the service has a new status for you.
     */
    oneway void triggerUpdateKillState(boolean inKillSwitch);
}
