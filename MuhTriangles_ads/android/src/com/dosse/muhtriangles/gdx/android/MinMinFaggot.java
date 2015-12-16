package com.dosse.muhtriangles.gdx.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MinMinFaggot extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minminfaggot);
        ((Button)findViewById(R.id.letsgo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
