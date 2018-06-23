/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.tedsutsu.preference

import android.content.Context
import timber.log.BuildConfig

/**
 * @author toastkidjp
 */
class AppNamePreference(context: Context) {

    private val sharedPreferences
            = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    fun registerAppId(packageName: String) {
        sharedPreferences.edit().putString(KEY_LAUNCH_APP_ID, packageName).apply()
    }

    fun readAppId(): String = sharedPreferences.getString(KEY_LAUNCH_APP_ID, "")

    companion object {

        private const val KEY_LAUNCH_APP_ID = "launch_app_id"

    }
}