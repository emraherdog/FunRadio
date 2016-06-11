package com.ufxmeng.je.funradio.utils;

import com.ufxmeng.je.funradio.R;

/**
 * Created by JE on 6/4/2016.
 */
public class RadioState {
    public static final int STATE_STOP = 0;
    public static final int STATE_PLAY = 1;

    public static final String ACTION_PLAY = "com.ufxmeng.je.funradio.ACTION_PLAY";
    public static final String ACTION_STOP = "com.ufxmeng.je.funradio.ACTION_STOP";

    public static final String ACTION_BROADCAST_PREPARED = "com.ufxmeng.je.funradio.ACTION_BROADCAST_PREPARED";
    public static final String ACTION_BROADCAST_ERROR = "com.ufxmeng.je.funradio.ACTION_BROADCAST_ERROR";
    public static final String ACTION_BROADCAST_PAUSE = "com.ufxmeng.je.funradio.ACTION_BROADCAST_PAUSE";
    public static final String ACTION_BROADCAST_PLAYTIME = "com.ufxmeng.je.funradio.ACTION_BROADCAST_PLAYTIME";
    public static final String ACTION_BROADCAST_CACHE = "com.ufxmeng.je.funradio.ACTION_BROADCAST_CACHE";

    public static final int[] RADIO_ICONS = {R.mipmap.rmc_info_talk_sport, R.mipmap.rtl, R.mipmap.europe1,
            R.mipmap.france_inter, R.mipmap.france_info, R.mipmap.radiomeuh,
            R.mipmap.fip, R.mipmap.fun_radio_fr, R.mipmap.cherie_fm,
            R.mipmap.bfm, R.mipmap.virgin_radio_officiel, R.mipmap.rfm_1039_fm,
            R.mipmap.nrj_france, R.mipmap.skyrock, R.mipmap.chantefrance,
            R.mipmap.ouifm, R.mipmap.france_bleu_nord, R.mipmap.rireetchansons,
            R.mipmap.bbc_world_service, R.mipmap.espn_radio, R.mipmap.npr_news};



}
