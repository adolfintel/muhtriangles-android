package com.dosse.muhtriangles.gdx.android;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dosse.muhtriangles.gdx.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent();
                i.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(MainActivity.this,LiveWallpaper.class));
                startActivityForResult(i, 0);
                finish();
            }
        });
        ((Button)findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager p = getPackageManager();
                p.setComponentEnabledSetting(new ComponentName(MainActivity.this,MainActivity.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                Toast.makeText(getApplicationContext(),getString(R.string.hide_clicked),Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

}
