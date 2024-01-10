package com.leaf.puzzle15;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView step;
    private TextView dialogTitle, dialogMessage;
    private Button positiveButton;
    private Chronometer timer;
    private final Button[][] buttons = new Button[4][4];
    private List<Integer> data;
    private Coordinate space;
    private int stepCounter;
    private LinearLayout dialogLayout;
    private FrameLayout dialogLayoutBack;
    private long realtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadViews();
        loadData();
        loadDataToViews();

    }

    private void loadViews() {

        step = findViewById(R.id.step);
        timer = findViewById(R.id.timer);

        dialogLayoutBack = findViewById(R.id.dialog_layout_bg);
        dialogLayout = findViewById(R.id.dialog_layout);
        dialogTitle = findViewById(R.id.dialog_title);
        dialogMessage = findViewById(R.id.dialog_message);
        positiveButton = findViewById(R.id.positive_button);

        ViewGroup puzzleLayout = findViewById(R.id.puzzle_layout);
        for (int i = 0; i < puzzleLayout.getChildCount(); i++) {
            ViewGroup innerLayout = (ViewGroup) puzzleLayout.getChildAt(i);
            for (int j = 0; j < innerLayout.getChildCount(); j++) {

                buttons[i][j] = (Button) innerLayout.getChildAt(j);

                buttons[i][j].setOnClickListener(this::click);
                buttons[i][j].setOnLongClickListener(this::longClick);

                buttons[i][j].setTag(new Coordinate(i, j));
            }
        }

    }

    private void loadData() {
        data = new ArrayList<>();
        for (int i = 1; i <= 15; i++)
            data.add(i);
        Collections.shuffle(data);
    }

    private void loadDataToViews() {
        stepCounter = 0;
        step.setText("" + stepCounter);
        timer.setBase(realtime = SystemClock.elapsedRealtime());
        timer.start();

        for (int i = 0; i < 15; i++)
            buttons[i / 4][i % 4].setText(data.get(i).toString());

        if (space != null)
            buttons[space.getX()][space.getY()].setBackgroundResource(R.drawable.circle_wooden);

        buttons[3][3].setText("");
        buttons[3][3].setBackground(null);
        buttons[3][3].setForeground(null);
        space = new Coordinate(3, 3);
    }

    private boolean isWin() {
        if (space.getX() != 3 || space.getY() != 3) return false;

        boolean isWin = true;
        for (int i = 0; i < 15; i++)
            isWin &= buttons[i / 4][i % 4].getText().equals(String.valueOf(i + 1));
        return isWin;
    }

    private void click(View view) {
        Coordinate coordinate = (Coordinate) view.getTag();

        if (coordinate.equals(space)) return;

        int dx = Math.abs(coordinate.getX() - space.getX());
        int dy = Math.abs(coordinate.getY() - space.getY());

        if ((dx == 1 && dy == 0) || (dx == 0 && dy == 1)) {
            buttons[space.getX()][space.getY()].setBackgroundResource(R.drawable.circle_wooden);
            buttons[space.getX()][space.getY()].setForeground(getDrawable(R.drawable.clickable_effect));
            buttons[space.getX()][space.getY()].setText(((Button) view).getText());
            buttons[space.getX()][space.getY()].setTag(space);

            buttons[coordinate.getX()][coordinate.getY()].setBackground(null);
            buttons[coordinate.getX()][coordinate.getY()].setForeground(null);
            buttons[coordinate.getX()][coordinate.getY()].setText("");

            space = coordinate;

            if (step != null)
                step.setText("" + ++stepCounter);

            if (isWin() && step != null) {
                timer.stop();
                step = null;
                letDialogLayout(true);
                new SoundPlayer(MediaPlayer.create(this, R.raw.well_done_sound)).play();
            }
        }
    }

    private boolean longClick(View view) {
        if (view.getTag().equals(space)) return false;
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.circling_anim));
        return true;
    }

    private void restart() {
        loadViews();
        letDialogLayout(false);
        loadData();
        loadDataToViews();
    }

    public void positiveClicks(View view) {
        if (positiveButton.getText().equals("Ok")) {
            step = null;
            new SoundPlayer(MediaPlayer.create(this, R.raw.loughing_sound)).play();
            onBackPressed();
        } else {
            restart();
        }
    }

    public void backClicks(View view) {
        onBackPressed();
    }

    public void cancelClicks(View view) {
        letDialogLayout(false);
        dialogTitle.setText(R.string.well_done);
        dialogMessage.setText(R.string.you_win);
        positiveButton.setText(R.string.replay);
    }

    private void makeSureDialog() {
        dialogTitle.setText(R.string.warning);
        dialogMessage.setText(R.string.alert_data);
        positiveButton.setText(R.string.ok);
        letDialogLayout(true);
    }

    private void letDialogLayout(boolean visibility) {
        if (visibility) {
            dialogLayoutBack.setVisibility(View.VISIBLE);
            dialogLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.realising_anim));
            dialogLayout.setVisibility(View.VISIBLE);
        } else {
            dialogLayout.setVisibility(View.GONE);
            dialogLayoutBack.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (step == null)
            super.onBackPressed();
        else if (dialogLayout.getVisibility() == View.VISIBLE)
            letDialogLayout(false);
        else
            makeSureDialog();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("step", stepCounter);
        outState.putLong("timer", realtime);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                outState.putString(i + "_" + j, buttons[i][j].getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        stepCounter = savedInstanceState.getInt("step");
        realtime = savedInstanceState.getLong("timer");
        step.setText("" + stepCounter);
        timer.setBase(realtime);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++) {
                String data = savedInstanceState.getString(i + "_" + j);
                buttons[i][j].setText(data);
                if (data.isEmpty()) {
                    buttons[i][j].setBackground(null);
                    buttons[i][j].setForeground(null);
                    space = new Coordinate(i, j);
                } else {
                    buttons[i][j].setBackgroundResource(R.drawable.circle_wooden);
                    buttons[j][j].setForeground(getDrawable(R.drawable.clickable_effect));
                }
            }
    }
}