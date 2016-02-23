package com.dosse.muhtriangles.gdx.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.util.Log;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.dosse.muhtriangles.gdx.MyGdxGame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;

public class LiveWallpaper extends AndroidLiveWallpaperService{
    private final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

    @Override
    public void onCreateApplication () {
        super.onCreateApplication();

        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useCompass = false;
        config.useWakelock = false;
        config.useAccelerometer = false;
        config.getTouchEventsForLiveWallpaper = true;
        config.numSamples=getSharedPreferences("MuhTriangles", MODE_PRIVATE).getBoolean("antialiasing", false)?2:0;

        final LWP listener = new LWP();
        initialize(listener, config);
    }

    private class LWP extends MyGdxGame implements AndroidWallpaperListener{
        private boolean isPreviewing=false;
        private boolean justStarted=true;
        @Override
        public void offsetChange(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            setXOffset(xOffset);
        }

        @Override
        public void resume(){
            super.resume();
            readBattery();
            if(isPreviewing||getSharedPreferences("MuhTriangles", MODE_PRIVATE).getString("hueMode","c0").charAt(0)=='r') readPrefs();
        }

        @Override
        public void previewStateChange(boolean isPreview) {
            if(!justStarted&&isPreview==isPreviewing) return;
            isPreviewing=isPreview;
            justStarted=false;
            readPrefs();
        }

        @Override
        public void readBattery() {
            BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    context.unregisterReceiver(this);
                    float rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                    float scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                    if (rawlevel >= 0 && scale > 0) {
                        setBattery(rawlevel / scale);
                    }
                }
            };
            IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(batteryLevelReceiver, batteryLevelFilter);
        }

        private void readPrefs(){
            SharedPreferences sp = getSharedPreferences("MuhTriangles", MODE_PRIVATE);
            boolean gradientMode=sp.getBoolean("gradientMode", true);
            int gradientSubtle=Integer.parseInt(sp.getString("gradientSubtle", "2"));
            int gradientType=Integer.parseInt(sp.getString("gradientType", "0"));
            boolean gradientInvert=sp.getBoolean("gradientInvert", false);
            String hueModeS=sp.getString("hueMode", "c0");
            int hueMode=4, customHue=0;
            if(hueModeS.charAt(0)=='c'){
                hueMode=Integer.parseInt(hueModeS.substring(1));
            }else{
                hueMode=4;
                customHue=hueModeS.charAt(0)=='r'?(int)(Math.random()*360):Integer.parseInt(hueModeS);
            }
            int satMode=Integer.parseInt(sp.getString("satMode", "0"));
            int lumaMode=Integer.parseInt(sp.getString("lumaMode", "0"));
            boolean touchReact=sp.getBoolean("touchReact", true);
            int instability=Integer.parseInt(sp.getString("instability", "1"));
            int outline=sp.getBoolean("outlineOnOff",true)?Integer.parseInt(sp.getString("outline", "1")):0;
            int speed=Integer.parseInt(sp.getString("speed", "1"));
            int density=Integer.parseInt(sp.getString("density", "1"));
            int fpsLimit=Integer.parseInt(sp.getString("maxFps", "20"));
            int outlineThickness=Integer.parseInt(sp.getString("outlineThickness", "1"));
            int algorithm=Integer.parseInt(sp.getString("algorithm","0"));
            setPreferences(gradientMode?MyGdxGame.GRADIENTMODE_SMOOTH:GRADIENTMODE_OLD, gradientSubtle, gradientType, gradientInvert?MyGdxGame.GRADIENTINVERT_YES:MyGdxGame.GRADIENTINVERT_NO, hueMode,customHue, satMode, lumaMode, touchReact?MyGdxGame.TOUCHREACT_ANIMATE:MyGdxGame.TOUCHREACT_NOTHING,speed,density,instability, outline,outlineThickness,algorithm,fpsLimit);
        }

    }



}

