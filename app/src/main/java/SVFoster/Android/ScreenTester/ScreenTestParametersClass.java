/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.ScreenTester;

import java.io.Serializable;

public class ScreenTestParametersClass implements Serializable {
    public static final int ConstColorsChangePolicyManual = 0;
    public static final int ConstColorsChangePolicyTimer = 1;

    private int ColorsChangePolicy = ConstColorsChangePolicyManual;
    public int ColorsChangePolicyGet(){ return ColorsChangePolicy; };
    public void ColorsChangePolicySet(int v){ this.ColorsChangePolicy = Math.min(v, ConstColorsChangePolicyTimer); };
    private int TimerIntervalMS = 250;
    public int TimerIntervalMSGet(){ return TimerIntervalMS; };
    public void TimerIntervalMSSet(int v){ this.TimerIntervalMS = Math.max(v, 100); };
    public boolean ScreenOnKeep = true;

    public boolean ColorRGB = true;
    public boolean ColorCMYK = true;
    public boolean ColorBW = true;

    public boolean ExitOnBatteryDischarged = true;
    private int ExitOnBatteryLevel = 10;
    public int ExitOnBatteryLevelGet(){ return ExitOnBatteryLevel; };
    public void ExitOnBatteryLevelSet(int v){ this.ExitOnBatteryLevel = Math.max(v, 1); };
}
