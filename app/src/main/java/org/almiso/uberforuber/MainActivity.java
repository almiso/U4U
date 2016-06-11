package org.almiso.uberforuber;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String uberPackageName = "com.ubercab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton buttonRequest = (AppCompatButton) findViewById(R.id.buttonRequest);
        assert buttonRequest != null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator animator = new StateListAnimator();
            animator.addState(new int[]{android.R.attr.state_pressed}, ObjectAnimator.ofFloat(buttonRequest, "translationZ", 4, 8).setDuration(200));
            animator.addState(new int[]{}, ObjectAnimator.ofFloat(buttonRequest, "translationZ", 4, 8).setDuration(200));
            buttonRequest.setStateListAnimator(animator);
        }

        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUberInstalled()) {
                    launchUber();
                } else {
                    openGooglePlay();
                }
            }
        });
    }

    private boolean isUberInstalled() {
        PackageManager pm = getPackageManager();
        boolean appInstalled;
        try {
            pm.getPackageInfo(uberPackageName, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    private void openGooglePlay() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + uberPackageName));
        if (!tryStartActivity(intent)) {
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + uberPackageName));
            if (!tryStartActivity(intent)) {
                Toast.makeText(this, getString(R.string.st_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean tryStartActivity(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private void launchUber() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(uberPackageName);
        startActivity(launchIntent);
    }

}
