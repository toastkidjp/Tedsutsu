/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.tedsutsu.libs

import android.content.Context
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import jp.toastkid.tedsutsu.R

/**
 * @author toastkidjp
 */
object CustomTabsIntentFactory {

    operator fun invoke(context: Context?): CustomTabsIntent? =
            context?.let {
                CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setStartAnimations(it, android.R.anim.fade_in, android.R.anim.fade_out)
                        .setExitAnimations(it, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .addDefaultShareMenuItem()
                        .build()
            }
}