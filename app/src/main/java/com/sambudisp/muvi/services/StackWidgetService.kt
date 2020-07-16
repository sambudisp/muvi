package com.sambudisp.muvi.services

import android.content.Intent
import android.widget.RemoteViewsService
import com.sambudisp.muvi.widget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}