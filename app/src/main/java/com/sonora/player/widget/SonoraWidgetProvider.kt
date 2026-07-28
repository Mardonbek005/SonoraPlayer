package com.sonora.player.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.sonora.player.R
import com.sonora.player.service.SonoraPlaybackService

class SonoraWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_player)

        // Play/Pause tugmasi uchun Intent (MediaSessionService bilan ishlash uchun 
        // maxsus Broadcast yoki Service start ishlatiladi)
        val playIntent = Intent(context, SonoraPlaybackService::class.java).apply {
            action = "com.sonora.player.ACTION_PLAY_PAUSE"
        }
        val playPendingIntent = PendingIntent.getService(
            context, 0, playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_btn_play_pause, playPendingIntent)

        // Next tugmasi uchun
        val nextIntent = Intent(context, SonoraPlaybackService::class.java).apply {
            action = "com.sonora.player.ACTION_NEXT"
        }
        val nextPendingIntent = PendingIntent.getService(
            context, 1, nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_btn_next, nextPendingIntent)

        // Previous tugmasi uchun
        val prevIntent = Intent(context, SonoraPlaybackService::class.java).apply {
            action = "com.sonora.player.ACTION_PREVIOUS"
        }
        val prevPendingIntent = PendingIntent.getService(
            context, 2, prevIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.widget_btn_prev, prevPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
