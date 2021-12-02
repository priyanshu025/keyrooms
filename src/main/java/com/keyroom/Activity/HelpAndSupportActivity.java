package com.keyroom.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.keyroom.R;

public class HelpAndSupportActivity extends AppCompatActivity {

    TextView text;
    ImageView imgClose;

    String txt="UNDER PRIVACY POLICY FOR USE OF APPLICATION AND MOBILE APPLICATION\n" +
            "\n" +
            "Welcome to My Key Partner at www.keyrooms.in (the \"Website\") and our Mobile Application Platform \"Key Rooms\" (the \"App\").\n" +
            "\n" +
            "The Website, App and its content is owned and operated by Key Rooms INDIA LLP (\"Keys\", \"us\" or \"we\"), a company incorporated under the provisions of the LLP Act.\n" +
            "\n" +
            "Keys facilitates the connection of a user with independent third party hotel operators or owners (\"KEY HOLDER\") who offer clean and fresh budget hotel rooms and such other accommodations on rent (\"Accomodations\"), through the Website/ App to the users, with assured essentials and prompt services (\"Services\"). Reference to \"you\", \"guest\", \"your\" or \"user\", whether registered or not, in these Terms of Use (as defined below) refers to the users of this Website, whether or not you access the services offered by us (\"Services\") or consummate a transaction on the Website or the App.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);
        imgClose=findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        text=findViewById(R.id.text);
        text.setText(txt);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}