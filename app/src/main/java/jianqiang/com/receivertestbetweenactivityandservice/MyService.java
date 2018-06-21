package jianqiang.com.receivertestbetweenactivityandservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

import java.io.IOException;

import jianqiang.com.receivertestbetweenactivityandservice.data.MyMusics;

public class MyService extends Service {

    Receiver2 receiver2;
    AssetManager am;

    MediaPlayer mPlayer;
    int status = 0x11;
    int current = 0;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        am = getAssets();

        //register receiver in Service
        receiver2 = new Receiver2();
        IntentFilter filter = new IntentFilter();
        filter.addAction("UpdateService");
        registerReceiver(receiver2, filter);

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                current++;
                if (current >= 3) {
                    current = 0;
                }
                prepareAndPlay(MyMusics.musics[current].name);

                //send message to receiver in Activity
                Intent sendIntent = new Intent("UpdateActivity");
                sendIntent.putExtra("status", -1);
                sendIntent.putExtra("current", current);
                sendBroadcast(sendIntent);
            }
        });
        super.onCreate();
    }

    private void prepareAndPlay(String music) {
        try {
            AssetFileDescriptor afd = am.openFd(music);
            mPlayer.reset();
            mPlayer.setDataSource(afd.getFileDescriptor()
                    , afd.getStartOffset()
                    , afd.getLength());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Receiver2 extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            int command = intent.getIntExtra("command", -1);
            switch (command) {
                case 1:
                    if (status == 0x11) {
                        prepareAndPlay(MyMusics.musics[current].name);
                        status = 0x12;
                    }
                    else if (status == 0x12) {
                        mPlayer.pause();
                        status = 0x13;
                    }
                    else if (status == 0x13) {
                        mPlayer.start();
                        status = 0x12;
                    }
                    break;
                case 2:
                    if (status == 0x12 || status == 0x13) {
                        mPlayer.stop();
                        status = 0x11;
                    }
            }

            //send message to receiver in Activity
            Intent sendIntent = new Intent("UpdateActivity");
            sendIntent.putExtra("status", status);
            sendIntent.putExtra("current", current);
            sendBroadcast(sendIntent);
        }
    }
}
