/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.logic.util;

import android.content.Context;

public class StaticContext {
    private static Context context;
    private static final String TAG = StaticContext.class.getSimpleName();

    /**
     * Sets the static context if passed context is not null.
     *
     * @param context A context to use.
     */
    public static void setContext(Context context) {
        if (/*StaticContext.context == null && */context != null) {
            Logger.logADB("v", TAG, "Static context set!");
            StaticContext.context = context;
        }
    }

    /**
     * Returns a context.
     *
     * @return A context. This may be null.
     */
    public static Context getContext() {
        if (StaticContext.context == null) {
            Logger.logADB("e", TAG, "Context has not been set yet!");
        }

        return StaticContext.context;
    }
}
