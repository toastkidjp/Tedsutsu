/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.tedsutsu.app_list

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.MenuRes
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.toastkid.tedsutsu.MainActivity
import jp.toastkid.tedsutsu.R
import jp.toastkid.tedsutsu.preference.AppNamePreference
import kotlinx.android.synthetic.main.fragment_app_list.*
import timber.log.Timber

/**
 * @author toastkidjp
 */
class AppListFragment: Fragment() {

    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_app_list, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context: Context = context ?: return

        toolbar.inflateMenu(MENU_ID)
        toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.exit -> {
                    activity?.finish()
                    true
                }
                R.id.about_app -> {
                    takeIf { activity is MainActivity }
                            ?.let { (activity as MainActivity).switchAboutFragment() }
                    true
                }
                R.id.to_top -> {
                    RecyclerViewScroller.toTop(app_items_view)
                    true
                }
                R.id.to_bottom -> {
                    RecyclerViewScroller.toBottom(app_items_view)
                    true
                }
                else -> false
            }
        }

        adapter = Adapter(context, toolbar, { setCurrentApp(it) })
        app_items_view.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
            it.onFlingListener = object : RecyclerView.OnFlingListener() {
                override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                    if (!filter.hasFocus()) {
                        return false
                    }
                    it.requestFocus()
                    return false
                }
            }
        }

        initInput(adapter)

        setCurrentApp(AppNamePreference(context).readAppId())
    }

    private fun setCurrentApp(readAppId: String?) {
        if (TextUtils.isEmpty(readAppId)) {
            current_app_area.visibility = View.GONE
            return
        }
        try {
            context?.packageManager?.also {
                val applicationInfo =
                        it.getPackageInfo(
                                readAppId,
                                PackageManager.GET_META_DATA
                        )?.applicationInfo ?: return
                current_app.text = applicationInfo.loadLabel(it)
                current_app_icon.setImageDrawable(applicationInfo.loadIcon(it))
                current_app_area.visibility = View.VISIBLE
            }
        } catch (e: PackageManager.NameNotFoundException) {
            current_app_area.visibility = View.GONE
            Timber.e(e)
        }
    }

    /**
     * Initialize input.
     *
     * @param adapter [Adapter]
     */
    private fun initInput(adapter: Adapter) {
        filter.addTextChangedListener(object : TextWatcher {
            var prev: String = ""

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (TextUtils.equals(prev, s)) {
                    return
                }
                prev = s.toString()
                adapter.filter(s.toString())
                app_items_view.scheduleLayoutAnimation();
            }

            override fun afterTextChanged(s: Editable) = Unit
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.dispose()
    }

    companion object {

        @MenuRes
        private val MENU_ID: Int = R.menu.app_list
    }
}