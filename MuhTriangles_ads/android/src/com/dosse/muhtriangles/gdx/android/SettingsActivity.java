package com.dosse.muhtriangles.gdx.android;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.SwitchPreference;
import android.util.Log;


//TODO: IMPROVE _ALL_ THIS SHIT
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private boolean isPackageInstalled(String p){
        try {
            getApplicationContext().getPackageManager().getPackageInfo(p, PackageManager.GET_META_DATA);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //let's tell ad block users to support us
        try{
            if(isPackageInstalled("tw.fatminmin.xposed.minminguard")||isPackageInstalled("org.adaway")||isPackageInstalled("org.adblockplus.android")){
                try{openFileInput("minminfag");}catch (Throwable t) {
                    openFileOutput("minminfag", MODE_PRIVATE).close();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SettingsActivity.this, MinMinFaggot.class));
                        }
                    });
                }
            }
        }catch (Throwable t){}
        getPreferenceManager().setSharedPreferencesName("MuhTriangles");
        addPreferencesFromResource(R.xml.pref_general);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(
                this);
        try {
            PreferenceGroup p = ((PreferenceGroup) getPreferenceScreen());
            for (int i = 0; i < p.getPreferenceCount(); i++) {
                Preference pref = p.getPreference(i);
                if (pref instanceof ListPreference) {
                    ListPreference listPref = (ListPreference) pref;
                    pref.setSummary(listPref.getEntry());
                }
            }
        }catch (Throwable t){}
        onSharedPreferenceChanged(null,null);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                this);
        super.onDestroy();
    }
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key!=null){
            if(key.equalsIgnoreCase("antialiasing")){
                    new Thread(){public void run(){
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        System.exit(0);}
                    }.start();
                    finish();

                }
            Preference pref = findPreference(key);

        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
        }}
        findPreference("hueMode").setEnabled(!(((ListPreference) findPreference("satMode")).getValue().equals("2")));
        if((((ListPreference) findPreference("instability")).getValue().equals("3"))){
            findPreference("outline").setEnabled(false);
            findPreference("outlineOnOff").setEnabled(false);
        }else{
            findPreference("outlineOnOff").setEnabled(true);
            findPreference("outline").setEnabled(((SwitchPreference) findPreference("outlineOnOff")).isChecked());
        }

        findPreference("outlineThickness").setEnabled((((SwitchPreference) findPreference("outlineOnOff")).isChecked())&&findPreference("outline").isEnabled());
    }
}

