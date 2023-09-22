/*
 * Copyright (c) 2012-2016 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by arne on 08.01.17.
 */

// Until I find a good solution

public class Preferences {

    static MultiPreferences getSharedPreferencesMulti(String name, Context c) {
        return new MultiPreferences(name, c.getContentResolver());
    }

    public static MultiPreferences getDefaultSharedPreferences(Context c) {
        return new MultiPreferences(c.getPackageName() + "_preferences", c.getContentResolver());
    }

    public static SharedPreferences getSharedPreferences(String name, Context c) {
        return c.getSharedPreferences(name, Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
    }
}
