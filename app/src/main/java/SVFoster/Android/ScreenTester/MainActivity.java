/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.ScreenTester;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private Button btn1 = null;
    private Button btn2 = null;
    private Switch chk1 = null;
    private TextView tw = null;
    private SeekBar sb = null;
    private Switch chk2 = null;
    private Switch chk3 = null;
    private Switch chk4 = null;
    private Switch chk5 = null;
    private TextView twbl = null;
    private TextView twprc = null;
    private TextView twsw1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VisualsInit();
    }

    private void VisualsInit(){
        this.chk1 = this.findViewById(R.id.CheckAuto);
        this.twsw1 = this.findViewById(R.id.textView3);
        this.chk1.setOnCheckedChangeListener(this::GUIControlsOnOff);
        this.tw = this.findViewById(R.id.textView);
        this.sb = this.findViewById(R.id.seekBar);
        this.twprc = this.findViewById(R.id.textView2);
        this.sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 100) {
                    progress = 100;
                    seekBar.setProgress(100);
                }
                // update the TextView with the current progress value
                MainActivity.this.twprc.setText(String.format(getResources().getString(R.string.mils), String.valueOf(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.chk2 = this.findViewById(R.id.CheckColorRGB);
        this.chk3 = this.findViewById(R.id.CheckColorCMYK);
        this.chk4 = this.findViewById(R.id.CheckColorBW);
        this.chk5 = this.findViewById(R.id.CheckStopOnBatteryLow);
        this.twbl = this.findViewById(R.id.twOnBatteryLow);

        this.btn1 = this.findViewById(R.id.button1);
        this.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTest();
            }
        });

        this.btn2 = this.findViewById(R.id.button2);
        this.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntentNew = new Intent( MainActivity.this, AboutActivity.class);
                startActivity(IntentNew);
            }
        });

        GUIUpdateData(new ScreenTestParametersClass());
    }

    private void StartTest(){
        Intent IntentNew = new Intent( this, TestActivity.class);
        IntentNew.putExtra(GlobalConstsClass.ScreenTestParametersIntentName, GUICollectData());
        startActivity(IntentNew);
    }

    private void GUIControlsOnOff(CompoundButton buttonView, boolean isChecked)
    {
        this.tw.setEnabled(isChecked);
        //this.twsw1.setEnabled(isChecked);
        this.twbl.setEnabled(isChecked);
        this.twprc.setEnabled(isChecked);
        this.sb.setEnabled(isChecked);
        this.chk5.setEnabled(isChecked);
    }

    private ScreenTestParametersClass GUICollectData()
    {
        ScreenTestParametersClass stp = new ScreenTestParametersClass();
        stp.ColorsChangePolicySet(this.chk1.isChecked() ? ScreenTestParametersClass.ConstColorsChangePolicyTimer : ScreenTestParametersClass.ConstColorsChangePolicyManual);
        stp.TimerIntervalMSSet(this.sb.getProgress());
        stp.ColorRGB = this.chk2.isChecked();
        stp.ColorCMYK = this.chk3.isChecked();
        stp.ColorBW = this.chk4.isChecked();
        stp.ExitOnBatteryDischarged = this.chk5.isChecked();

        return stp;
    }

    private void GUIUpdateData(ScreenTestParametersClass d)
    {
        this.chk1.setChecked(d.ColorsChangePolicyGet() == ScreenTestParametersClass.ConstColorsChangePolicyTimer);
        this.sb.setProgress(d.TimerIntervalMSGet());
        this.chk2.setChecked(d.ColorRGB);
        this.chk3.setChecked(d.ColorCMYK);
        this.chk4.setChecked(d.ColorBW);
        this.chk5.setChecked(d.ExitOnBatteryDischarged);

        GUIControlsOnOff(null, this.chk1.isChecked());
    }
}
