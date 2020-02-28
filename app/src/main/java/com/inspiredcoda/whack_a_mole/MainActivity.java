package com.inspiredcoda.whack_a_mole;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    ImageView bug00, bug01, bug02, bug10, bug11, bug12, bug20, bug21, bug22, soundState, life1, life2, life3;
    FrameLayout frm00, frm01, frm02, frm10, frm11, frm12, frm20, frm21, frm22;
    TextView gameScore;

    ImageView [][] moles;
    ImageView [] lives;

    Animation showMole, hideMole;
    Random randomI, randomJ;

    int score = 0;
    int currentLife = 2;
    boolean exitStatus = false;
    boolean playing = true;
    boolean isSounding = true;
    boolean isMoleHit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initializeWidgets();
        showMole = AnimationUtils.loadAnimation(this, R.anim.show_mole_animation);
        hideMole = AnimationUtils.loadAnimation(this, R.anim.hide_mole_animation);
        moles = new ImageView[3][3];
        lives = new ImageView[3];
        life1.setAnimation(showMole);
        life2.setAnimation(showMole);
        life3.setAnimation(showMole);
        addAllImageViews(moles);
        randomI = new Random();
        randomJ = new Random();

        showMole.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isMoleHit = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        startGame();
    }

    private void initializeWidgets(){
        bug00 = findViewById(R.id.mole_0_0);
        bug01 = findViewById(R.id.mole_0_1);
        bug02 = findViewById(R.id.mole_0_2);
        bug10 = findViewById(R.id.mole_1_0);
        bug11 = findViewById(R.id.mole_1_1);
        bug12 = findViewById(R.id.mole_1_2);
        bug20 = findViewById(R.id.mole_2_0);
        bug21 = findViewById(R.id.mole_2_1);
        bug22 = findViewById(R.id.mole_2_2);

        frm00 = findViewById(R.id.frm00);
        frm01 = findViewById(R.id.frm01);
        frm02 = findViewById(R.id.frm02);
        frm10 = findViewById(R.id.frm10);
        frm11 = findViewById(R.id.frm11);
        frm12 = findViewById(R.id.frm12);
        frm20 = findViewById(R.id.frm20);
        frm21 = findViewById(R.id.frm21);
        frm22 = findViewById(R.id.frm22);

        gameScore = findViewById(R.id.game_score);
        soundState = findViewById(R.id.soundStatus);
        life1 = findViewById(R.id.life_1);
        life2 = findViewById(R.id.life_2);
        life3 = findViewById(R.id.life_3);
    }

    private void addAllImageViews(ImageView [][] someImages){
        someImages[0][0] = bug00;
        someImages[0][1] = bug01;
        someImages[0][2] = bug02;
        someImages[1][0] = bug10;
        someImages[1][1] = bug11;
        someImages[1][2] = bug12;
        someImages[2][0] = bug20;
        someImages[2][1] = bug21;
        someImages[2][2] = bug22;

        lives[0] = life1;
        lives[1] = life2;
        lives[2] = life3;

    }

    private void showDialog(){
        AlertDialog customDialog = new AlertDialog.Builder(this)
                .setMessage("Game Over!!!")
                .create();
        customDialog.show();
    }

    private void nextMole(View view){
        int i = randomI.nextInt(3) ;
        int j = randomJ.nextInt(3) ;

        view.setVisibility(View.GONE);
        moles[i][j].setVisibility(View.VISIBLE);
        moles[i][j].startAnimation(showMole);
    }

    private void startGame(){
        Log.d(TAG, "startGame: Game Started");
        if (playing){
            Log.d(TAG, "startGame: still playing...................");
            final int i = randomI.nextInt(3) ;
            final int j = randomJ.nextInt(3) ;

            moles[i][j].setVisibility(View.VISIBLE);
            moles[i][j].startAnimation(showMole);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "run: Visibility changed");
                    moles[i][j].startAnimation(hideMole);
                    moles[i][j].setVisibility(View.GONE);
                    if (!isMoleHit){
                        startGame();
                    }
                }
            }, 1500);

        }


    }

    public void holeClicked(View view){
        Log.d(TAG, "holeClicked: Hole Clicked!!!!!!!!!!!!!!!!!!!!!!!");

        if(lives.length != 0){
            if(currentLife < 3 && currentLife >= 1){
                lives[currentLife].startAnimation(hideMole);
                lives[currentLife].setVisibility(View.GONE);
                currentLife = currentLife - 1;
            }else{
                lives[0].setVisibility(View.VISIBLE);
                lives[1].setVisibility(View.VISIBLE);
                lives[2].setVisibility(View.VISIBLE);

                lives[0].startAnimation(showMole);
                lives[1].startAnimation(showMole);
                lives[2].startAnimation(showMole);
                currentLife = 2;
                score = 0;
                gameScore.setText(String.valueOf(score));
                playing = false;
                showDialog();
            }
        }
    }

    public void moleClicked(View view){
        Log.d(TAG, "moleClicked: Mole Clicked!!!!!!!!!!!!!!!!!!!!!!!");
        score += 1;
        isMoleHit = true;
        String newScore = String.valueOf(score);
        gameScore.setText(newScore);
        view.startAnimation(hideMole);
        nextMole(view);
    }

    public void speakerMute(View view){
        if (isSounding){
            soundState.setBackgroundResource(R.drawable.ic_volume_off);
            isSounding = false;
        }else{
            soundState.setBackgroundResource(R.drawable.ic_volume_up);
            isSounding = true;
        }
    }


    @Override
    public void onBackPressed() {
        if(exitStatus){
            if(playing){
                playing = false;
            }
            playing = false;
            super.onBackPressed();
            return;
        }
        Toast.makeText(this, "click again to exit", Toast.LENGTH_SHORT).show();
        exitStatus = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                exitStatus = false;
            }
        }, 2000);
    }

    class myAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }

}
