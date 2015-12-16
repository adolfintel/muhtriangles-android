package com.dosse.muhtriangles.gdx.android;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdPreference extends Preference {

    public AdPreference(Context context, AttributeSet attrs, int defStyle) {super    (context, attrs, defStyle);}
    public AdPreference(Context context, AttributeSet attrs) {super(context, attrs);}
    public AdPreference(Context context) {super(context);}

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        try{((AdView)(((LinearLayout) view).findViewById(R.id.banner))).loadAd(new AdRequest.Builder().build());}catch (Throwable t){}
        return view;
    }
}
