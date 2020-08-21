package com.mohamedmgamal.lolPing;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.impa.pinger.PingInfo;
import me.impa.pinger.Pinger;

public class MainActivity extends AppCompatActivity {
    int ping=0;
    @BindView(R.id.txt_brazil)
    TextView txtBrazil;
    @BindView(R.id.txt_EUNE)
    TextView txtEUNE;
    @BindView(R.id.txt_EUW)
    TextView txtEUW;
    @BindView(R.id.txt_LAN)
    TextView txtLAN;
    @BindView(R.id.txt_NA)
    TextView txtNA;
    @BindView(R.id.txt_OCE)
    TextView txtOCE;
    @BindView(R.id.txt_RU)
    TextView txtRU;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;
    @BindView(R.id.adView)
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainFunc();
        btnRefresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    btnRefresh.setBackgroundResource(R.drawable.custome_button2);
                else
                    btnRefresh.setBackgroundResource(R.drawable.custome_button);
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainFunc();

    }

    void mainFunc() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("Error",loadAdError.getMessage());
            }
        });

        txtBrazil.setText("Pinging");
        txtBrazil.setTextColor(Color.parseColor("#fd7f00"));
        txtEUNE.setText("Pinging");
        txtEUNE.setTextColor(Color.parseColor("#fd7f00"));
        txtEUW.setText("Pinging");
        txtEUW.setTextColor(Color.parseColor("#fd7f00"));
        txtLAN.setText("Pinging");
        txtLAN.setTextColor(Color.parseColor("#fd7f00"));
        txtNA.setText("Pinging");
        txtNA.setTextColor(Color.parseColor("#fd7f00"));
        txtOCE.setText("Pinging");
        txtOCE.setTextColor(Color.parseColor("#fd7f00"));
        txtRU.setText("Pinging");
        txtRU.setTextColor(Color.parseColor("#fd7f00"));
        PingFunc("104.160.152.3", txtBrazil);
        PingFunc("104.160.142.3", txtEUNE);
        PingFunc("104.160.141.3", txtEUW);
        PingFunc("104.160.136.3", txtLAN);
        PingFunc("104.160.131.3", txtNA);
        PingFunc("104.160.156.1", txtOCE);
        PingFunc("162.249.73.2", txtRU);
ping ++;

    }

    void PingFunc(String s, final TextView textView) {
        Pinger pinger = new Pinger();
        pinger.setOnPingListener(new Pinger.OnPingListener() {

            @Override
            public void OnStart(@NonNull PingInfo pingInfo) {
                Log.i("PING", String.format("Pinging %s [%s]", pingInfo.ReverseDns, pingInfo.RemoteIp));
            }

            @Override
            public void OnStop(@NonNull PingInfo pingInfo) {
                Log.i("PING", "Ping complete");
            }

            @Override
            public void OnSendError(@NonNull PingInfo pingInfo, int sequence) {
            }

            @Override
            public void OnReplyReceived(@NonNull final PingInfo pingInfo, int sequence, int timeMs) {

                if (sequence >= 4) {
                    pingInfo.Pinger.Stop(pingInfo.PingId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pingInfo.Pinger.Stop(pingInfo.PingId);
                            textView.setText("Ping : " + timeMs);
                            if (timeMs < 120)
                                textView.setTextColor(Color.parseColor("#00BD72"));
                            else if (timeMs > 400)
                                textView.setTextColor(Color.parseColor("#f21414"));
                            else
                                textView.setTextColor(Color.parseColor("#fd7f00"));
                        }
                    });

                }
            }

            @Override
            public void OnTimeout(@NonNull PingInfo pingInfo, int sequence) {
                if (sequence >= 4) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pingInfo.Pinger.Stop(pingInfo.PingId);
                            textView.setText("TimeOut");
                            textView.setTextColor(Color.parseColor("#f21414"));
                        }
                    });

                }
            }

            @Override
            public void OnException(@NonNull PingInfo pingInfo, @NonNull Exception e, boolean isFatal) {
              Log.e("OnException Error : ",e.getMessage());

            }
        });
        pinger.Ping(s);
    }

    @OnClick(R.id.btn_refresh)
    public void onViewClicked() {
        mainFunc();
    }
}
