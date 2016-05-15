package com.nfcwormhole.liquor;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {
    public static String EXTRA_1 = "EXTRA_1";

    @BindView(R.id.tx1)
    TextView textView1;
    @BindView(R.id.tx2)
    TextView textView2;
    @BindView(R.id.tx3)
    TextView textView3;
    @BindView(R.id.tx4)
    TextView textView4;
    @BindView(R.id.button3)
    Button button3;

    String[] msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        String sReadNdef = extras.getString(EXTRA_1);
        if (sReadNdef != null) {
            msgs = sReadNdef.split("\\r\\n");
            if (msgs.length < 5)
                return;
            textView1.setText(msgs[1]);
            textView2.setText(msgs[2]);
            textView3.setText(msgs[3]);
            textView4.setText(msgs[4]);

            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.jinjiu);
            mediaPlayer.start();
        }

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
