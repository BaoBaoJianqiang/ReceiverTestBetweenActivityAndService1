package jianqiang.com.receivertestbetweenactivityandservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import jianqiang.com.receivertestbetweenactivityandservice.data.MyMusics;

public class MainActivity extends Activity {
    TextView tvTitle, tvAuthor;
    ImageButton btnPlay, btnStop;

    Receiver1 receiver1;

    //0x11: stoping; 0x12: playing; 0x13:pausing
    int status = 0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);

        btnPlay = (ImageButton) this.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send message to receiver in Service
                Intent intent = new Intent("UpdateService");
                intent.putExtra("command", 1);
                sendBroadcast(intent);
            }
        });

        btnStop = (ImageButton) this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send message to receiver in Service
                Intent intent = new Intent("UpdateService");
                intent.putExtra("command", 2);
                sendBroadcast(intent);
            }
        });

        //register receiver in Activity
        receiver1 = new Receiver1();
        IntentFilter filter = new IntentFilter();
        filter.addAction("UpdateActivity");
        registerReceiver(receiver1, filter);

        //start Service
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    public class Receiver1 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            status = intent.getIntExtra("status", -1);
            int current = intent.getIntExtra("current", -1);
            if (current >= 0) {
                tvTitle.setText(MyMusics.musics[current].title);
                tvAuthor.setText(MyMusics.musics[current].author);
            }

            switch (status) {
                case 0x11:
                    btnPlay.setImageResource(R.drawable.play);
                    break;
                case 0x12:
                    btnPlay.setImageResource(R.drawable.pause);
                    break;
                case 0x13:
                    btnPlay.setImageResource(R.drawable.play);
                    break;
                default:
                    break;
            }
        }
    }
}