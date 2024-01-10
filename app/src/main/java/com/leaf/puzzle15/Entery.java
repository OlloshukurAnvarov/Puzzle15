package com.leaf.puzzle15;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class Entery extends AppCompatActivity {
    private TextView piFifeText;
    private TextView puzzleText;
    private ImageView mkLogo;
    private TextView editionText;
    private SwitchCompat editionSwitch;
    private static final String myPref = "appSwitch";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entery);


        mkLogo = findViewById(R.id.mk_logo);
        editionText = findViewById(R.id.edition_text);
        editionSwitch = findViewById(R.id.edition_switch);

        editor = getSharedPreferences(myPref, 0).edit();
        editionSwitch.setChecked(getSharedPreferences(myPref, 0).getBoolean("switch-index", false));
        SoundPlayer.setIsActive(getSharedPreferences(myPref, 0).getBoolean("switch-index", false));

        puzzleText = findViewById(R.id.puzzle_text);
        piFifeText = findViewById(R.id.pi5_text);
        piFifeText.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.circling_anim));
        });
    }

    public void startClicks(View view) {
        startActivity(new Intent(this, MainActivity.class));
        new CountDownTimer(1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (piFifeText.getVisibility() == View.VISIBLE) {

                    piFifeText.setVisibility(View.GONE);

                    mkLogo.setVisibility(View.GONE);

                    editionText.setVisibility(View.GONE);
                    editionSwitch.setVisibility(View.GONE);

                    puzzleText.setVisibility(View.VISIBLE);
                } else {

                    puzzleText.setVisibility(View.GONE);
                    mkLogo.setVisibility(View.VISIBLE);
                    piFifeText.setVisibility(View.VISIBLE);

                }
            }
        }.start();
    }

    public void exitClicks(View view) {
        finish();
    }

    public void infoClicks(View view) {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage("Contact us for services:\naorma0717@gmail.com")
                .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                .create().show();
    }

    public void puzzleTextClicks(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.angle_anim));
    }

    public void mkLogoClicks(View view) {
        view.setVisibility(View.GONE);
        editionText.setVisibility(View.VISIBLE);
        editionSwitch.setVisibility(View.VISIBLE);
        editionSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {

            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (isChecked)
                        new SoundPlayer(MediaPlayer.create(getApplicationContext(), R.raw.choose_your_desteny_sound)).play();
                }

                @Override
                public void onFinish() {
                    editionText.setVisibility(View.GONE);
                    editionSwitch.setVisibility(View.GONE);
                    view.setVisibility(View.VISIBLE);
                }
            }.start();

            editor.putBoolean("switch-index", editionSwitch.isChecked()).commit();

            SoundPlayer.setIsActive(getSharedPreferences(myPref, 0).getBoolean("switch-index", false));
        });
    }
}