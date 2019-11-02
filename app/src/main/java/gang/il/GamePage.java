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

import static gang.il.GameSurfaceView.mThread;
import static gang.il.GameSurfaceView.surfaceViewRunning;
import static gang.il.GameSurfaceView.drawView;
import static gang.il.Valiable.drawInit;
import static gang.il.Valiable.finishObj;
import static gang.il.Valiable.mContext;
import static gang.il.Valiable.minCount;
import static gang.il.Valiable.moveCount;
import static gang.il.Valiable.stageCount;
import static gang.il.Valiable.objCount;
import static gang.il.Valiable.totalObj;
import static gang.il.Valiable.tutorialNum;
import static gang.il.Valiable.dialog;

public class GamePage extends AppCompatActivity {
    ImageView backBtn, resetBtn;
    ImageView[] animalWidget = new ImageView[5];
    StageDBHelper StageDB;
    GameSurfaceView gameSurfaceView;
    private long mLastClickTime = 0;
    Context GameContext;

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
        gameSurfaceView = (GameSurfaceView) findViewById(R.id.GameSurfaceView) ;
        backBtn.setOnClickListener(backBtnListener);
        resetBtn.setOnClickListener(ResetListener);
        setMinCount();
        gameSurfaceView.setZOrderOnTop(true);
        gameSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888); //출처 https://mparchive.tistory.com/103  (surfaceVIew 배경 투명화)
        if(stageCount.equals("1")) tutorialNum = 1;
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
        Log.d("backPressTest","onPause()");

    }

    public void backStage() {
        drawView=false;
        //gameSurfaceView.setVisibility(View.INVISIBLE);
        Log.d("backPressTest","backStage()");
        if (SystemClock.elapsedRealtime() - mLastClickTime < 100){
            return;
        }
        surfaceViewRunning = false;
        mLastClickTime = SystemClock.elapsedRealtime();
            finish();
    }
    public void Dialog() {
        dialog = new StageClearDialog(GameContext, "클리어",backBtnListener, // 내용
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

    //다이얼로그 클릭이벤트
    private View.OnClickListener nextDialogListener = new View.OnClickListener() {
        public void onClick(View v) {
            ((StagePage) mContext).setStage();
            stageCount = String.valueOf(Integer.parseInt(stageCount) + 1);
            if(stageCount.equals("2")){
                drawView=false;
                gameSurfaceView.setVisibility(View.INVISIBLE);
                gameSurfaceView.setVisibility(View.VISIBLE);
            }
            moveCount = 0;
            setMoveCount();
            setStageCount();
            setMinCount();
            surfaceViewRunning = false;
            StageDB.getMinCount(Integer.parseInt(stageCount));
            StageDB.getStageObj();
            animalWidget();
            drawInit=false;
            if (dialog != null)
                dialog.dismiss();
            surfaceViewRunning = true;
        }
    };
    private View.OnClickListener clr_ResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((StagePage) mContext).setStage();
            moveCount = 0;
            //tutorialNum = 1;
            setMoveCount();
            surfaceViewRunning = false;
            StageDB.getMinCount(Integer.parseInt(stageCount));
            StageDB.getStageObj();
            animalWidget();
            if (dialog != null)
                dialog.dismiss();
            surfaceViewRunning = true;
        }
    };

    private View.OnClickListener ResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            moveCount = 0;
            //tutorialNum = 1;
            setMoveCount();
            surfaceViewRunning = false;
            StageDB.getMinCount(Integer.parseInt(stageCount));
            StageDB.getStageObj();
            animalWidget();
            if (dialog != null)
                dialog.dismiss();
            surfaceViewRunning=true;
        }
    };
//출처: http://yoo-hyeok.tistory.com/51 [유혁의 엉터리 개발]
}
