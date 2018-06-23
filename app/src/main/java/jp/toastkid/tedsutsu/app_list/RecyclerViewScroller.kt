/*
 * Copyright (c) 2018 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.tedsutsu.app_list

import android.support.v7.widget.RecyclerView

/**
 * @author toastkidjp
 */
object RecyclerViewScroller {

    private const val THRESHOLD: Int = 30

    fun toTop(recyclerView: RecyclerView) {
        if (recyclerView.adapter.itemCount > THRESHOLD) {
            recyclerView.scrollToPosition(0)
            return
        }
        recyclerView.post { recyclerView.smoothScrollToPosition(0) }
    }

    fun toBottom(recyclerView: RecyclerView) {
        val itemCount = recyclerView.adapter.itemCount
        if (itemCount > THRESHOLD) {
            recyclerView.scrollToPosition(itemCount - 1)
            return
        }
        recyclerView.post { recyclerView.smoothScrollToPosition(itemCount - 1) }
    }
}