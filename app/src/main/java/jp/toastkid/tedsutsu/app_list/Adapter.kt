/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.tedsutsu.app_list

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import jp.toastkid.tedsutsu.R
import jp.toastkid.tedsutsu.preference.AppNamePreference
import timber.log.Timber
import java.util.*

/**
 * RecyclerView's adapter.
 *
 * @author toastkidjp
 */
internal class Adapter(
        private val context: Context,
        private val parent: View,
        private val onClick: (String) -> Unit
): RecyclerView.Adapter<ViewHolder>() {

    /**
     * Master list.
     */
    private val master: List<ApplicationInfo>

    /**
     * Current list.
     */
    private val installedApps: MutableList<ApplicationInfo>

    /**
     * Package manager.
     */
    private val packageManager: PackageManager = context.packageManager

    /**
     * Disposables.
     */
    private val disposables: CompositeDisposable = CompositeDisposable()

    init {
        this.master = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        this.installedApps = ArrayList(master)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_launcher, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = installedApps[position]
        holder.setImage(info.loadIcon(packageManager))
        holder.setTitle(info.loadLabel(packageManager).toString())
        holder.setTargetSdk(info.targetSdkVersion)
        holder.setPackageName(info.packageName)
        try {
            val packageInfo = packageManager.getPackageInfo(info.packageName, PackageManager.GET_META_DATA)
            holder.setVersionInformation(packageInfo.versionName + "(" + packageInfo.versionCode + ")")
            holder.setInstalledMs(packageInfo.firstInstallTime)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e)
        }

        setOnClick(holder, info)
    }

    /**
     * Set on click actions.
     *
     * @param holder [ViewHolder]
     * @param info [ApplicationInfo]
     */
    private fun setOnClick(holder: ViewHolder, info: ApplicationInfo) {
        val packageName = info.packageName
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent == null) {
            holder.itemView.setOnClickListener { snackCannotLaunch() }
            return
        }

        val label = info.loadLabel(packageManager)

        holder.itemView.setOnClickListener {
            AppNamePreference(context).registerAppId(packageName)
            onClick(packageName)
            Snackbar.make(
                    parent,
                    "Set: \"$label\"",
                    Snackbar.LENGTH_SHORT
            ).show()
        }

        holder.itemView.setOnLongClickListener {
            Snackbar.make(
                    parent,
                    "Would you like to launch \"$label\"?",
                    Snackbar.LENGTH_SHORT
            ).also {
                it.setAction(R.string.launch) {
                    try {
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Timber.e(e)
                        snackCannotLaunch()
                    }
                }
            }.show()
            true
        }
    }

    /**
     * Show cannot launch message with snackbar.
     */
    private fun snackCannotLaunch() {
        Snackbar.make(parent, R.string.message_failed_launching, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Do filtering with text.
     *
     * @param str filter query
     */
    fun filter(str: String) {
        installedApps.clear()
        if (TextUtils.isEmpty(str)) {
            installedApps.addAll(master)
            notifyDataSetChanged()
            return
        }
        master.toObservable()
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .filter { appInfo -> appInfo.packageName.contains(str) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { this.notifyDataSetChanged() }
                .subscribe { installedApps.add(it) }
                .addTo(disposables)
    }

    override fun getItemCount(): Int = installedApps.size

    /**
     * Dispose disposables.
     */
    fun dispose() {
        disposables.clear()
    }
}