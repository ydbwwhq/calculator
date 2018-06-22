package com.halewang.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by shinezone on 2017/11/14.
 */
class Util
{
    companion object {
        fun getDeviceW(context: Context) :Int
        {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager;
            return windowManager.defaultDisplay.width;
        }

        fun getDeviceDensity(context: Context) :Float
        {
            val dispalyMetrics = DisplayMetrics();
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager;
            windowManager.defaultDisplay.getMetrics(dispalyMetrics)
            return dispalyMetrics.density;
        }
        fun pixToDp(context: Context ,pix: Float):Int
        {
            val scale = context.resources.displayMetrics.density;
            return (pix / scale + 0.5f) as Int;
        }

        fun dpToPIX(context: Context, dp: Float):Int
        {
            val scale = context.resources.displayMetrics.density;
            return (dp * scale + 0.5f).toInt();
        }
    }


}