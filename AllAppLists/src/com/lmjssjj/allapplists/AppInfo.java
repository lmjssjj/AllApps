/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lmjssjj.allapplists;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.lmjssjj.allapplists.compat.LauncherActivityInfoCompat;
import com.lmjssjj.allapplists.compat.UserHandleCompat;
import com.lmjssjj.allapplists.compat.UserManagerCompat;
import com.lmjssjj.allapplists.utils.ComponentKey;
import com.lmjssjj.allapplists.utils.PackageManagerHelper;
import com.lmjssjj.allapplists.utils.Utilities;

import java.util.ArrayList;

/**
 * Represents an app in AllAppsView.
 */
public class AppInfo extends ItemInfo {

    /**
     * The intent used to start the application.
     */
    public Intent intent;

    /**
     * A bitmap version of the application icon.
     */
    public Drawable iconBitmap;

    /**
     * Indicates whether we're using a low res icon
     */
    boolean usingLowResIcon;

    public ComponentName componentName;

    static final int DOWNLOADED_FLAG = 1;
    static final int UPDATED_SYSTEM_APP_FLAG = 2;

    int flags = 0;

    /**
     * {@see ShortcutInfo#isDisabled}
     */

    public AppInfo() {
    }

    @Override
    public Intent getIntent() {
        return intent;
    }

    protected Intent getRestoredIntent() {
        return null;
    }

    /**
     * Must not hold the Context.
     */
    public AppInfo(Context context, LauncherActivityInfoCompat info, UserHandleCompat user
           ) {
        this(context, info, user,
                UserManagerCompat.getInstance(context).isQuietModeEnabled(user));
    }

    public AppInfo(Context context, LauncherActivityInfoCompat info, UserHandleCompat user,
            boolean quietModeEnabled) {
        this.componentName = info.getComponentName();
        flags = initFlags(info);
        title = Utilities.trim(info.getLabel());
        intent = makeLaunchIntent(context, info, user);
        iconBitmap = info.getIcon(DisplayMetrics.DENSITY_XHIGH);
        this.user = user;
    }

    public static int initFlags(LauncherActivityInfoCompat info) {
        int appFlags = info.getApplicationInfo().flags;
        int flags = 0;
        if ((appFlags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0) {
            flags |= DOWNLOADED_FLAG;

            if ((appFlags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                flags |= UPDATED_SYSTEM_APP_FLAG;
            }
        }
        return flags;
    }

    public AppInfo(AppInfo info) {
        super(info);
        componentName = info.componentName;
        title = Utilities.trim(info.title);
        intent = new Intent(info.intent);
        flags = info.flags;
        iconBitmap = info.iconBitmap;
    }
    /**
     * Helper method used for debugging.
     */
    public static void dumpApplicationInfoList(String tag, String label, ArrayList<AppInfo> list) {
        Log.d(tag, label + " size=" + list.size());
        for (AppInfo info: list) {
            Log.d(tag, "   title=\"" + info.title + "\" iconBitmap=" + info.iconBitmap
                    + " componentName=" + info.componentName.getPackageName());
        }
    }


    public ComponentKey toComponentKey() {
        return new ComponentKey(componentName, user);
    }

    public static Intent makeLaunchIntent(Context context, LauncherActivityInfoCompat info,
            UserHandleCompat user) {
        long serialNumber = UserManagerCompat.getInstance(context).getSerialNumberForUser(user);
        return new Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .setComponent(info.getComponentName())
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            .putExtra(EXTRA_PROFILE, serialNumber);
    }


}
