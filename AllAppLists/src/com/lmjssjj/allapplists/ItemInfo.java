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

import com.lmjssjj.allapplists.compat.UserHandleCompat;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

/**
 * Represents an item in the launcher.
 */
public class ItemInfo {

    /**
     * Intent extra to store the profile. Format: UserHandle
     */
    public static final String EXTRA_PROFILE = "profile";

    /**
     * Title of the item
     */
    public CharSequence title;

    /**
     * Content description of the item.
     */
    public CharSequence contentDescription;

    public UserHandleCompat user;

    public ItemInfo() {
        user = UserHandleCompat.myUserHandle();
    }

    ItemInfo(ItemInfo info) {
        copyFrom(info);
        // tempdebug:
    }

    public void copyFrom(ItemInfo info) {
       
        user = info.user;
        contentDescription = info.contentDescription;
    }

    public Intent getIntent() {
        return null;
    }

    public ComponentName getTargetComponent() {
        return getIntent() == null ? null : getIntent().getComponent();
    }

    /**
     * Whether this item is disabled.
     */
    public boolean isDisabled() {
        return false;
    }
}
