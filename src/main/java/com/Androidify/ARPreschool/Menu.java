package com.Androidify.ARPreschool;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

   //private int STORAGE_PERMISSION_CODE = 1;
    Button scan, help, aboutus, quit;
    HomeWatcher mHomeWatcher;
    MediaPlayer button_sound;
    private boolean mIsBound = false;
    private MusicService mServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        scan = findViewById(R.id.scan);
        help = findViewById(R.id.help);
        aboutus = findViewById(R.id.aboutus);
        quit = findViewById(R.id.quit);

        button_sound= MediaPlayer.create(Menu.this, R.raw.button_sound);
        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_sound.start();
                    Intent intent1 = new Intent(Menu.this, UnityPlayerActivity.class);
                    startActivity(intent1);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_sound.start();
                Intent intent = new Intent(Menu.this, OnBoardingScreen.class);
                startActivity(intent);
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_sound.start();
                Intent intent = new Intent(Menu.this, AboutUs.class);
                startActivity(intent);
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_sound.start();
                final AlertDialog.Builder builder = new AlertDialog.Builder(Menu.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to exit ??");
                builder.setPositiveButton("Yes. Exit now!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        System.exit(0);
                }
            });
	    builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        }
    });
}
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    private ServiceConnection Scon =new ServiceConnection(){
        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder)binder).getService();
        }
        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };
    void doBindService(){
        bindService(new Intent(this,MusicService.class),
                Scon,Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }
    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }
        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        Intent music = new Intent();
        music.setClass(this,MusicService.class);
        stopService(music);
    }
}
