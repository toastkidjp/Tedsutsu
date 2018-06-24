/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.tedsutsu.ad

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import jp.toastkid.tedsutsu.BuildConfig

/**
 * @author toastkidjp
 */
object AdInvoker {

    operator fun invoke(adView: AdView) {
        adView.loadAd(makeRequest())
    }

    private fun makeRequest(): AdRequest {
        val adRequest = AdRequest.Builder()
        if (BuildConfig.DEBUG) {
            adRequest.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("59A864957D348217B858A8CE956AA352")
                    .addTestDevice("41D3185792903C624B6E9045EBF43BB3")
                    .addTestDevice("FF30448442F5EAE65974D6E0FEB4C1BD")
        }
        return adRequest.build()
    }
}