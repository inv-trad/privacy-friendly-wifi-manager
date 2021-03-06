/*
Copyright 2016-2018 Jan Henzel, Patrick Jauernig, Dennis Werner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package org.secuso.privacyfriendlywifimanager.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import org.secuso.privacyfriendlywifimanager.logic.util.SettingsEntry;
import org.secuso.privacyfriendlywifimanager.logic.util.StaticContext;
import org.secuso.privacyfriendlywifimanager.service.Controller;

import secuso.org.privacyfriendlywifi.R;


/**
 * A widget that is able to start/stop the ManagerService via the Controller.
 */
public class WifiWidget extends AppWidgetProvider {

    /**
     * Updates an app widget instance
     *
     * @param context          A Context.
     * @param appWidgetManager An AppWidgetManager instance.
     * @param appWidgetId      The ID of the widget to update.
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews widgetActiveButtonView = updateButton(context, SettingsEntry.isServiceActive(context), false);

        // intent for widget button click
        Intent buttonIntent = new Intent(context, WidgetClickListener.class);
        PendingIntent pendingButtonIntent = PendingIntent.getBroadcast(context, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        widgetActiveButtonView.setOnClickPendingIntent(R.id.appwidget_switch, pendingButtonIntent);
        widgetActiveButtonView.setOnClickPendingIntent(R.id.appwidget_text, pendingButtonIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, widgetActiveButtonView);
    }

    /**
     * Updates the on/off button in every widget instance
     *
     * @param context A Context.
     * @param active  Whether the button should be on (true) or off (false).
     * @return RemoveViews referring to the button instances.
     */
    private static RemoteViews updateButton(Context context, boolean active) {
        return updateButton(context, active, true);
    }

    /**
     * Updates the on/off button in every widget instance
     *
     * @param context   A Context.
     * @param active    Whether the button should be on (true) or off (false).
     * @param broadcast Whether this update should trigger a broadcast or not.
     * @return RemoveViews referring to the button instances.
     */
    private static RemoteViews updateButton(Context context, boolean active, boolean broadcast) {
        RemoteViews buttonView = new RemoteViews(context.getPackageName(), R.layout.widget_wifi);
        buttonView.setTextViewText(R.id.appwidget_switch, active ? context.getString(R.string.on) : context.getString(R.string.off));
        buttonView.setInt(R.id.appwidget_switch, "setBackgroundColor", active ? ContextCompat.getColor(context, R.color.colorAccent) : ContextCompat.getColor(context, R.color.colorPrimary));

        // now update the widgets
        if (broadcast) {
            broadcastUpdate(context);
        }

        return buttonView;
    }

    /**
     * Broadcasts that widget needs to be updated.
     *
     * @param context A context to use.
     */
    private static void broadcastUpdate(Context context) {
        final Intent updateWidgetsIntent = new Intent(context, WifiWidget.class);
        updateWidgetsIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, WifiWidget.class));
        updateWidgetsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(updateWidgetsIntent);
    }

    /**
     * Listener for click event in main notification area. Starts MainActivity.
     */
    public static class WidgetClickListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean active = !SettingsEntry.isServiceActive(context);
            SettingsEntry.setActiveFlag(context, active);

            if (active) {
                Controller.registerReceivers(context);
            } else {
                Controller.unregisterReceivers(context);
            }

            updateButton(context, active);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        StaticContext.setContext(context);
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

