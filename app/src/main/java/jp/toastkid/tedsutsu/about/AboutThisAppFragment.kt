/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.tedsutsu.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import jp.toastkid.tedsutsu.BuildConfig
import jp.toastkid.tedsutsu.R
import jp.toastkid.tedsutsu.libs.CustomTabsIntentFactory
import kotlinx.android.synthetic.main.fragment_about_this_app.*
import timber.log.Timber

/**
 * @author toastkidjp
 */
class AboutThisAppFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_about_this_app, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings_toolbar.setTitle(R.string.about_app)
        licenses.setOnClickListener {
            val intent = Intent(context, OssLicensesMenuActivity::class.java)
            intent.putExtra("title", view.context.getString(R.string.licenses))
            startActivity(intent)
        }
        privacy_policy.setOnClickListener {
            CustomTabsIntentFactory(context)?.launchUrl(context, "https://tmblr.co/ZDG7Be2NVdctY".toUri())
        }
        settings_app_version.also {
            it.text = BuildConfig.VERSION_NAME
            it.setOnClickListener {
                try {
                    startActivity(
                            Intent(Intent.ACTION_VIEW)
                                    .apply { data = Uri.parse("market://id?=${BuildConfig.APPLICATION_ID}") }
                    )
                } catch (e: ActivityNotFoundException) {
                    Timber.e(e)
                }
            }
        }
    }
}