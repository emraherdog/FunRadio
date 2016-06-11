package com.ufxmeng.je.funradio.reciver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.ufxmeng.je.funradio.R;
import com.ufxmeng.je.funradio.services.RadioService;
import com.ufxmeng.je.funradio.utils.PrefUtils;
import com.ufxmeng.je.funradio.utils.RadioState;

/**
 * Created by JE on 6/10/2016.
 */
public class RadioAppWidgetProvider extends AppWidgetProvider {

    private static final String NEXT_STATION = "funradio.intent.action.NEXT_STATION";
    private static final String PREV_STATION = "funradio.intent.action.PREV_STATION";
    private static final String PLAY_STATION = "funradio.intent.action.PLAY_STATION";
    final int[] mRadioIcons = {R.mipmap.rmc_info_talk_sport, R.mipmap.rtl, R.mipmap.europe1,
            R.mipmap.france_inter, R.mipmap.france_info, R.mipmap.radiomeuh,
            R.mipmap.fip, R.mipmap.fun_radio_fr, R.mipmap.cherie_fm,
            R.mipmap.bfm, R.mipmap.virgin_radio_officiel, R.mipmap.rfm_1039_fm,
            R.mipmap.nrj_france, R.mipmap.skyrock, R.mipmap.chantefrance,
            R.mipmap.ouifm, R.mipmap.france_bleu_nord, R.mipmap.rireetchansons,
            R.mipmap.bbc_world_service, R.mipmap.espn_radio, R.mipmap.npr_news};

    private String[] mRadioUrls;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {
            final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.radio_widget_layout);

            ///intent to open main activity
            Intent intent = new Intent(PLAY_STATION);
            final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.imageview_profile_radio_widget, pendingIntent);

            intent = new Intent(NEXT_STATION);
            PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.imageview_right_radio_widget, nextPendingIntent);

            intent = new Intent(PREV_STATION);
            nextPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.imageview_left_radio_widget, nextPendingIntent);

            PrefUtils prefUtils = PrefUtils.getInstance(context);
            changeImage(remoteViews, prefUtils.getCurrentstation());
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        PrefUtils prefUtils = PrefUtils.getInstance(context);
        int position = prefUtils.getCurrentstation();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.radio_widget_layout);
        remoteViews.setViewVisibility(R.id.progressbar_radio_widget, View.GONE);

        if (NEXT_STATION.equals(intent.getAction())) {
            position = checkPosition(++position);
        } else if (PREV_STATION.equals(intent.getAction())) {
            position = checkPosition(--position);
        } else if (PLAY_STATION.equals(intent.getAction())) {
//            remoteViews.setViewVisibility(R.id.progressbar_radio_widget, View.VISIBLE);

            mRadioUrls = context.getResources().getStringArray(R.array.radio_urls);
            Intent serviceIntent = new Intent(context.getApplicationContext(), RadioService.class);
            serviceIntent.setAction(RadioState.ACTION_PLAY);
            serviceIntent.putExtra("url", mRadioUrls[position]);
            context.startService(serviceIntent);

        } else if (RadioState.ACTION_BROADCAST_PREPARED.equals(intent.getAction())) {
            Logger.d("ACTION_BROADCAST_PREPARED");
        } else if (RadioState.ACTION_BROADCAST_ERROR.equals(intent.getAction())) {
            Logger.d("ACTION_BROADCAST_ERROR");
        }

        prefUtils.setCurrentstation(position);
        changeImage(remoteViews, position);

        ComponentName thisWidget = new ComponentName(context, RadioAppWidgetProvider.class);
        final AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(thisWidget, remoteViews);

        super.onReceive(context, intent);
    }

    private void changeImage(RemoteViews views, int position) {
        views.setImageViewResource(R.id.imageview_profile_radio_widget, mRadioIcons[position]);
    }

    private int checkPosition(int position) {

        return position >= 0 ?
                position <= mRadioIcons.length - 1 ? position : mRadioIcons.length - 1 : 0;
    }
}
