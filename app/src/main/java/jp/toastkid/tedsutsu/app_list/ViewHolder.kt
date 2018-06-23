/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.tedsutsu.app_list

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import jp.toastkid.tedsutsu.R

/**
 * View holder.
 *
 * @author toastkidjp
 */
class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Date and Time format.
     */
    private val format: String = view.context.getString(R.string.date_format)

    /**
     * Set image.
     * @param drawable
     */
    fun setImage(drawable: Drawable) {
        view.findViewById<ImageView>(R.id.app_icon).setImageDrawable(drawable)
    }

    /**
     * Set title.
     *
     * @param title
     */
    fun setTitle(title: String) {
        view.findViewById<TextView>(R.id.app_title).text = title
    }

    /**
     * Set target SDK information.
     *
     * @param targetSdkVersion
     */
    fun setTargetSdk(targetSdkVersion: Int) {
        view.findViewById<TextView>(R.id.app_target_sdk).text =
                Html.fromHtml("<b>Target SDK</b>: $targetSdkVersion")
    }

    /**
     * Set package name.
     *
     * @param packageName
     */
    fun setPackageName(packageName: String) {
        view.findViewById<TextView>(R.id.app_package_name).text =
                Html.fromHtml("<b>Package Name</b>: $packageName")
    }

    /**
     * Set installed ms.
     *
     * @param firstInstallTime
     */
    fun setInstalledMs(firstInstallTime: Long) {
        view.findViewById<TextView>(R.id.app_installed).text= Html.fromHtml(
                "<b>Installed</b>: " + DateFormat.format(format, firstInstallTime))
    }

    /**
     * Set version information.
     *
     * @param versionText
     */
    fun setVersionInformation(versionText: String) {
        view.findViewById<TextView>(R.id.app_version).text =
                Html.fromHtml("<b>Version</b>: $versionText")
    }
}