/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.ScreenTester;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import SVFoster.Android.ScreenTester.databinding.ActivityTestBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {
    private Timer Timer1 = null;
    private ScreenTestParametersClass ScreenTestParameters = null;
    private BatteryManager BatteryStatus = null;
    private PowerManager PowerManager1 = null;
    private PowerManager.WakeLock WakeLock1 = null;

    /**
     * Some older devices need a small delay between UI widget update
     * and a change of the status and navigation bar.
    **/
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler(Looper.myLooper());
    private View mContentView;
    private final Runnable HideBarsRunn = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= 30) {
                mContentView.getWindowInsetsController().hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                return;
            }

            mContentView.setSystemUiVisibility(
                      View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
    };

    private ActivityTestBinding UIBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UIBinding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(UIBinding.getRoot());

        mContentView = UIBinding.fullscreenContent;
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnScreenClick();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        this.PowerManager1 = (PowerManager) getSystemService(POWER_SERVICE);
        this.WakeLock1 = this.PowerManager1.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, GlobalConstsClass.WakelockTagName);
        this.BatteryStatus = (BatteryManager)this.getSystemService(BATTERY_SERVICE);

        TestParametersGet();
        ColorsListInit();

        if (this.ScreenTestParameters.ColorsChangePolicyGet() == ScreenTestParametersClass.ConstColorsChangePolicyTimer){
            TimerInit();
        } else
        {
            ManualClick();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        WindowGoFullScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ScreenOnKeepToggle(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        ScreenOnKeepToggle(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.Timer1 != null)
            Timer1.cancel();
    }

    @Override
    public void onBackPressed(){
        this.finish();
    }

    private void TimerInit(){
        Timer1 = new Timer();
        TimerTask TimerTask1 = new TimerTask() {
            public void run() {
                TimerTickASync();
            };
        };

        Timer1.schedule(
            TimerTask1,
            0,
            this.ScreenTestParameters.TimerIntervalMSGet()
        );
    }

    private void TimerTickASync() {
        this.runOnUiThread(TimerTickSync);
    }

    private Runnable TimerTickSync = new Runnable() {
        public void run() {
            TimerModeAppExitCheck();
            int ColorNext = ColorNextGet();
            setActivityBackgroundColor( ColorNext );
        }
    };

    private void ManualClick(){
        int ColorNext = ColorNextGet();
        setActivityBackgroundColor( ColorNext );
    }

    private void OnScreenClick(){
        if (this.ScreenTestParameters.ColorsChangePolicyGet() == ScreenTestParametersClass.ConstColorsChangePolicyManual){
            ManualClick();
        }
    }

    private void WindowGoFullScreen() {
        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.postDelayed(HideBarsRunn, UI_ANIMATION_DELAY);
    }

    public void setActivityBackgroundColor(int resid) {
        final FrameLayout lyt;
        lyt = findViewById(R.id.rlVar1);
        lyt.setBackgroundResource(resid);
    }

    private int ColorLast = 0;
    private List<Integer> ColorsList = null;

    private void ColorsListInit(){
        ColorsList = new ArrayList<>();

        if (this.ScreenTestParameters.ColorRGB) {
            ColorsList.add(R.color.ColorTestR);
            ColorsList.add(R.color.ColorTestG);
            ColorsList.add(R.color.ColorTestB);
        }

        if (this.ScreenTestParameters.ColorCMYK) {
            ColorsList.add(R.color.ColorTestC);
            ColorsList.add(R.color.ColorTestM);
            ColorsList.add(R.color.ColorTestY);
        }

        if (this.ScreenTestParameters.ColorBW) {
            ColorsList.add(R.color.ColorTestBl);
            ColorsList.add(R.color.ColorTestWh);
        }

        if (ColorsList.size() == 0){
            ColorsList.add(R.color.ColorTestBl);
        }
    }

    public int ColorNextGet(){
        int Result = ColorsList.get(ColorLast);
        ColorLast++;
        if (ColorLast >= ColorsList.size()){
            ColorLast = 0;
        }
        return Result;
    }

    private void TestParametersGet(){
        this.ScreenTestParameters = (ScreenTestParametersClass)getIntent().getSerializableExtra( GlobalConstsClass.ScreenTestParametersIntentName );

        if ( this.ScreenTestParameters != null ){
            return;
        }
        this.ScreenTestParameters = new ScreenTestParametersClass();
    }

    private void ScreenOnKeepToggle(boolean state){
        if (!state){
            if (!this.WakeLock1.isHeld()) {
                return;
            }
            this.WakeLock1.release();
            return;
        }

        if (!this.ScreenTestParameters.ScreenOnKeep){
            return;
        }

        this.WakeLock1.acquire();
        // no need to clear this flag later
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void TimerModeAppExitCheck(){
        if (this.ScreenTestParameters.ColorsChangePolicyGet() != ScreenTestParametersClass.ConstColorsChangePolicyTimer){
            return;
        }

        if (!this.ScreenTestParameters.ExitOnBatteryDischarged){
            return;
        }

        Intent BatteryIntent = this.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int BufferChg = BatteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1); // or use EXTRA_PLUGGED instead of EXTRA_STATUS
        if (BufferChg != BatteryManager.BATTERY_STATUS_DISCHARGING){
            return;
        }

        int ChargeLevel = this.BatteryStatus.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        if (ChargeLevel <= this.ScreenTestParameters.ExitOnBatteryLevelGet()){
            Toast.makeText(this, R.string.exitbatterylow, Toast.LENGTH_LONG).show();
            this.finishAffinity();
        }
    }
}