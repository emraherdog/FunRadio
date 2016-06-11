package com.ufxmeng.je.funradio.reciver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.ufxmeng.je.funradio.R;
import com.ufxmeng.je.funradio.services.RadioService;
import com.ufxmeng.je.funradio.services.StackWidgetService;
import com.ufxmeng.je.funradio.utils.RadioState;

/**
 * Created by JE on 6/11/2016.
 */
public class StackWidgetProvider extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (TOAST_ACTION.equals(intent.getAction())) {
            final String url = intent.getStringExtra(EXTRA_ITEM);
            Intent serviceIntent = new Intent(context.getApplicationContext(), RadioService.class);
            serviceIntent.setAction(RadioState.ACTION_PLAY);
            serviceIntent.putExtra("url", url);
            context.startService(serviceIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {

            Intent intent = new Intent(context, StackWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.radio_widget_stack_layout);
            views.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);

            views.setEmptyView(R.id.stack_view, R.id.empty_view);

            Intent toastIntent = new Intent(context, StackWidgetProvider.class);
            toastIntent.setAction(StackWidgetProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
