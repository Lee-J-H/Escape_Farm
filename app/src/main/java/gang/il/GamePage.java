package gang.il;


import android.content.Context;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import static gang.il.GameOption.writeSoundOp;
import static gang.il.GameSurfaceView.drawView;
import static gang.il.Valiable.btnClick;
import static gang.il.Valiable.btnClickType;
import static gang.il.Valiable.drawInit;
import static gang.il.Valiable.finishObj;
import static gang.il.Valiable.mContext;
import static gang.il.Valiable.btnSound;
import static gang.il.Valiable.minCount;
import static gang.il.Valiable.moveCount;
import static gang.il.Valiable.soundPlay;
import static gang.il.Valiable.soundPool;
import static gang.il.Valiable.stageCount;
import static gang.il.Valiable.tutorialNum;
import static gang.il.Valiable.dialog;
import static gang.il.Valiable.adCount;

public class GamePage extends AppCompatActivity {
    ImageView backBtn, resetBtn, soundBtn;
    ImageView[] animalWidget = new ImageView[5];
    StageDBHelper StageDB;
    GameSurfaceView gameSurfaceView;
    private long mLastClickTime = 0;
    Context GameContext;
    private InterstitialAd mInterstitialAd;//광고

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backStage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameContext = this;
        StageDB = new StageDBHelper(GameContext);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);
        init();
        animalWidget();
    }

    public void init() {
        backBtn = (ImageView) findViewById(R.id.back_btn);
        resetBtn = (ImageView) findViewById(R.id.reset_btn);
        soundBtn = (ImageView) findViewById(R.id.sound_btn_game);
        gameSurfaceView = (GameSurfaceView) findViewById(R.id.GameSurfaceView);
        backBtn.setOnClickListener(backBtnListener);
        resetBtn.setOnClickListener(ResetListener);
        soundBtn.setOnClickListener(soundBtnListener);
        setMinCount();
        gameSurfaceView.setZOrderOnTop(true);
        gameSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888); //출처 https://mparchive.tistory.com/103  (surfaceVIew 배경 투명화)
        if (stageCount.equals("1")) tutorialNum = 1;
        if (soundPlay)
            soundBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sound_on));
        else soundBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sound_off));
        MobileAds.initialize(this, GameContext.getResources().getString(R.string.ad_unit_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(GameContext.getResources().getString(R.string.ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                btnClick = true;
            }

        });
        adCount = 0;
    }

    public void setMoveCount() {
        TextView moveText = findViewById(R.id.moveCount);
        moveText.setText("이동횟수: " + moveCount);
    }

    public void setStageCount() {
        TextView stageText = findViewById(R.id.stageCount);
        stageText.setText("스테이지: " + stageCount);
    }

    public void setMinCount() {
        TextView min_text = findViewById(R.id.tv_min_count);
        min_text.setText("최소횟수: " + StageDB.getMinCount(Integer.valueOf(stageCount)));
        minCount = StageDB.getMinCount(Integer.valueOf(stageCount));
    }

    public void animalWidget() {
        animalWidget[0] = findViewById(R.id.widget_dog);
        animalWidget[1] = findViewById(R.id.widget_squirrel);
        animalWidget[2] = findViewById(R.id.widget_rabbit);
        animalWidget[3] = findViewById(R.id.widget_panda);
        animalWidget[4] = findViewById(R.id.widget_tiger);
        animalWidget[0].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.dog));
        animalWidget[1].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.squirrel));
        animalWidget[2].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.rabbit));
        animalWidget[3].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.panda));
        animalWidget[4].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.tiger));
        animalWidget[0].setVisibility(View.GONE);
        animalWidget[1].setVisibility(View.GONE);
        animalWidget[2].setVisibility(View.GONE);
        animalWidget[3].setVisibility(View.GONE);
        animalWidget[4].setVisibility(View.GONE);
        for (int i = 0; i < finishObj.size(); i++) {
            if (finishObj.get(i).equals("dog"))
                animalWidget[0].setVisibility(View.VISIBLE);
            if (finishObj.get(i).equals("squirrel"))
                animalWidget[1].setVisibility(View.VISIBLE);
            if (finishObj.get(i).equals("rabbit"))
                animalWidget[2].setVisibility(View.VISIBLE);
            if (finishObj.get(i).equals("panda"))
                animalWidget[3].setVisibility(View.VISIBLE);
            if (finishObj.get(i).equals("tiger"))
                animalWidget[4].setVisibility(View.VISIBLE);
        }
    }

    public void finWidget(String animal) {
        switch (animal) {
            case "dog":
                animalWidget[0].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.dog_widget));
                break;
            case "squirrel":
                animalWidget[1].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.squirrel_widget));
                break;
            case "rabbit":
                animalWidget[2].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.rabbit_widget));
                break;
            case "panda":
                animalWidget[3].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.panda_widget));
                break;
            case "tiger":
                animalWidget[4].setImageDrawable(GameContext.getResources().getDrawable(R.drawable.tiger_widget));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void loadAD() {
        if (adCount == 0) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("ADFall", "The Interstitial wasn't loaded yet.");
            }
        }
    }

    public void backStage() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
            return;
        }
        drawView = false;
        mLastClickTime = SystemClock.elapsedRealtime();
        adCount++;
        if (adCount == 4) adCount = 0;
        if (soundPlay) soundPool.play(btnSound, 1f, 1f, 0, 0, 1f); //버튼 사운드 재생
        moveCount = 0;
        finish();
    }

    public void Dialog() {
        dialog = new StageClearDialog(GameContext, "클리어", backBtnListener, // 내용
                nextDialogListener, clr_ResetListener); // 왼쪽 버튼 이벤트
        // 오른쪽 버튼 이벤트
        //요청 이 다이어로그를 종료할 수 있게 지정함
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    //다이얼로그 클릭이벤트
    private View.OnClickListener backBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private View.OnClickListener soundBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (soundPlay) {
                soundPlay = false;
                soundBtn.setImageDrawable(GameContext.getResources().getDrawable(R.drawable.sound_off));
                writeSoundOp(mContext, "off");
            } else {
                soundPlay = true;
                soundBtn.setImageDrawable(GameContext.getResources().getDrawable(R.drawable.sound_on));
                writeSoundOp(mContext, "on");
                soundPool.play(btnSound, 1f, 1f, 0, 0, 1f); //버튼 사운드 재생
            }
        }
    };


    //다이얼로그 클릭이벤트
    private View.OnClickListener nextDialogListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            stageCount = String.valueOf(Integer.parseInt(stageCount) + 1);
            if (stageCount.equals("2")) {
                drawView = false;
                gameSurfaceView.setVisibility(View.INVISIBLE);
                gameSurfaceView.setVisibility(View.VISIBLE);
            } //튜토리얼 지우기
            setStageCount();
            setMinCount();
            btnClickType = "next";
            if (adCount == 0) loadAD();
            else btnClick = true;
            adCount++;
            if (adCount == 4) adCount = 0;
            if (dialog.isShowing())
                dialog.dismiss();
        }
        // }
    };
    private View.OnClickListener clr_ResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            btnClickType = "clr_reset";
            if (adCount == 0) loadAD();
            else btnClick = true;
            adCount++;
            if (adCount == 4) adCount = 0;
            if (dialog.isShowing())
                dialog.dismiss();
        }
    };

    private View.OnClickListener ResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            btnClickType = "reset";
            if (adCount == 0) loadAD();
            else btnClick = true;
            adCount++;
            if (adCount == 4) adCount = 0;
        }
    };

    public void onBtnClick(String type) {
        moveCount = 0;
        setMoveCount();
        if (soundPlay) soundPool.play(btnSound, 1f, 1f, 0, 0, 1f); //버튼 사운드 재생
        {
            StageDB.getMinCount(Integer.parseInt(stageCount));
            StageDB.getStageObj();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    animalWidget();
                }
            });
        }
        if (type.equals("next")) drawInit = false;
        else if (type.endsWith("reset")) tutorialNum = 1;
        btnClick = false;
    }
}
