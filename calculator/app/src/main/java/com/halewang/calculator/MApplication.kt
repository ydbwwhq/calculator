package com.halewang.calculator

import android.app.Application
import android.util.Log


/**
 * Created by shinezone on 2017/11/21.
 */
class MApplication:Application()
{
    override fun onCreate() {
        super.onCreate()
        Log.i("hh","Application OnCreate")

    }
}