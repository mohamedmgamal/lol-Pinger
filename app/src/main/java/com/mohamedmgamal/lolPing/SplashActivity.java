package com.mohamedmgamal.lolPing;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.progress_circular)
    ProgressBar progressCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        chickConnection0();


    }

    private void chickConnection0() {
        progressCircular.setVisibility(View.VISIBLE);
        //Toast.makeText(this, "Testing Connection", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                chickConnection();
            }
        }, 150);
    }


    void chickConnection() {
        if (isNetworkAvailable() && isOnline()) {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            progressCircular.setVisibility(View.GONE);
            View parentLayout = findViewById(android.R.id.content);
            Snackbar.make(parentLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    chickConnection0();

                                }
                            }, 200);


                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .show();
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isOnline() {
        try {
            Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

}