package com.ufxmeng.je.funradio.utils;

import com.ufxmeng.je.funradio.bean.PodcastDetail;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by JE on 6/7/2016.
 */
public class XmlUtils {

    private static final String TAG = "XmlUtils";
    private List<PodcastDetail> mPodcastDetails = new ArrayList<>();
    private String text;
    private PodcastDetail mPodcastDetail = null;

    public List<PodcastDetail> parseXml(String xmlString, int res_id) {

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            final XmlPullParser xmlPullParser = factory.newPullParser();

            StringReader reader = new StringReader(xmlString);
            xmlPullParser.setInput(reader);

            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                final String tagName = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("item")) {

                            mPodcastDetail = new PodcastDetail();
                            mPodcastDetail.setImageResID(res_id);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xmlPullParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("item")) {
                            mPodcastDetails.add(mPodcastDetail);
                        } else if (mPodcastDetail != null) {

                            if (tagName.equalsIgnoreCase("title")) {
                                mPodcastDetail.setTitle(text);
                            } else if (tagName.equalsIgnoreCase("guid")) {
                                mPodcastDetail.setPodcastMp3Url(text);
                            } else if (tagName.equalsIgnoreCase("pubDate")) {
                                //// TODO: 6/7/2016 parse date
                                // 	EEE, dd MMM yyyy HH:mm:ss z
                                final Date date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(text);
                                mPodcastDetail.setPubDate(date);
                            } else if (tagName.equalsIgnoreCase("duration")) {
                                mPodcastDetail.setDuration(text);
                            }

                        }
                        break;
                }

                eventType = xmlPullParser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        for (PodcastDetail podcastDetail : mPodcastDetails) {
            Log.d(TAG, "parseXml: " + podcastDetail.toString());
        }
*/
        return mPodcastDetails;
    }

}
