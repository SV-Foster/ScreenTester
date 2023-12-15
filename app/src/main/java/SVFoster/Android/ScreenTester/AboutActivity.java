/***

Copyright 2023, SV Foster. All rights reserved.

License:
    This program is free for personal, educational and/or non-profit usage    

Revision History:

***/

package SVFoster.Android.ScreenTester;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView LinkWebSite = null;
    private TextView LinkEmail = null;
    private TextView CopyrightText = null;
    private TextView AppVersionText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        VisualsInit();
    }

    private void VisualsInit(){
        this.setTitle( R.string.app_name );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.LinkWebSite = findViewById(R.id.LinkWebSite);
        this.LinkWebSite.setMovementMethod(LinkMovementMethod.getInstance());
        this.LinkWebSite.setText(Html.fromHtml("<a href=\"" + GlobalConstsClass.AuthorWebsiteLinkGo + "\">" + GlobalConstsClass.AuthorWebsite + "</a>"));

        this.LinkEmail = findViewById(R.id.LinkEmail);
        this.LinkEmail.setMovementMethod(LinkMovementMethod.getInstance());
        this.LinkEmail.setText(Html.fromHtml("<a href=\"mailto:" + GlobalConstsClass.AuthorEmail + "?subject=Mail%20from%20Unique%20Identifiers%20Harverster&body=Hello!%0D%0A%0D%0AI'm%20using%20Unique%20Identifiers%20Harverster%20version%20" + ProgramVersionClass.VersionShortGet() + "%20for%20Android.%0D%0A%0D%0A\">" + GlobalConstsClass.AuthorEmail + "</a>"));

        this.CopyrightText = findViewById(R.id.CopyrightText);
        this.CopyrightText.setText( ProgramVersionClass.CopyrightTextGet(false));

        this.AppVersionText = findViewById(R.id.AppVersionText);
        String BufferString = getString(R.string.about_version) + " " + ProgramVersionClass.VersionShortGet();
        this.AppVersionText.setText( BufferString );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // back button on the title handler
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
