package com.sambudisp.muvi

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.sambudisp.muvi.services.StackWidgetService


class FavWidget : AppWidgetProvider() {

    companion object {
        private val TOAST_ACTION = "com.sambudisp.muvi.TOAST_ACTION"
        const val EXTRA_ITEM = "com.sambudisp.muvi.EXTRA_ITEM"

        private fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.fav_widget)
            views.setRemoteAdapter(R.id.stackview_fav, intent)
            views.setEmptyView(R.id.stackview_fav, R.id.txt_empty_fav)

            val toastIntent = Intent(context, FavWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val toastPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stackview_fav, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val id = appWidgetManager.getAppWidgetIds(
                    ComponentName(
                        context!!,
                        FavWidget::class.java
                    )
                );
                appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.stackview_fav)
                this.onUpdate(context, appWidgetManager, id)
            }
        }
    }
}
