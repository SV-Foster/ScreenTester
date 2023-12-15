/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.ScreenTester;

import java.util.Calendar;

public class ProgramVersionClass {
    public static String CopyrightTextGet(Boolean HighYearIsCurrent){
        int YearHigh;

        String BufferString =
                "Ⓒ " +
                GlobalConstsClass.AuthorName +
                ", " +
                Integer.toString( GlobalConstsClass.CopyrightYearLow );

        if ( HighYearIsCurrent ){
            YearHigh = Calendar.getInstance().get(Calendar.YEAR);
        } else {
            YearHigh = GlobalConstsClass.CopyrightYearHigh;
        }

        if (YearHigh > GlobalConstsClass.CopyrightYearLow){
            BufferString += "—" + Integer.toString( YearHigh );
        }

        return BufferString;
    }

    public static String VersionGet(){
        String BufferString =
                GlobalConstsClass.ProgramVersionMain + "." +
                        GlobalConstsClass.ProgramVersionSecond + "." +
                        VersionDigTo4Str( GlobalConstsClass.ProgramVersionThird );

        if ( GlobalConstsClass.ProgramVersionForth != 0 ){
            BufferString += "." + VersionDigTo4Str( GlobalConstsClass.ProgramVersionForth );
        }

        return BufferString;
    }

    public static String VersionShortGet(){

        return GlobalConstsClass.ProgramVersionMain + "." + GlobalConstsClass.ProgramVersionSecond;
    }

    public static String VersionDigTo4Str(int v){
        StringBuilder BufferString = new StringBuilder(Integer.toString(v));
        for (;;){
            if ( BufferString.length()>=4 ){
                break;
            }

            BufferString.insert(0, "0" );
        }

        return BufferString.toString();
    }
}
