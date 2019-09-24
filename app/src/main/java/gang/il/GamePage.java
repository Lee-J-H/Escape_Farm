package gang.il;


import android.content.Context;
import android.graphics.PixelFormat;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import static gang.il.GameSurfaceView.mThread;
import static gang.il.Valiable.drawInit;
import static gang.il.Valiable.mContext;
import static gang.il.Valiable.minCount;
import static gang.il.Valiable.moveCount;
import static gang.il.Valiable.stageCount;
import static gang.il.Valiable.tutorialNum;
import static gang.il.Valiable.dialog;

public class GamePage extends AppCompatActivity {
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
    }

    public void init() {
        gameSurfaceView = (GameSurfaceView) findViewById(R.id.GameSurfaceView) ;
        minCount = StageDB.getMinCount(Integer.valueOf(stageCount));
        tutorialNum = 1;
        gameSurfaceView.setZOrderOnTop(true);
        gameSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888); //출처 https://mparchive.tistory.com/103  (surfaceVIew 배경 투명화)
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void backStage() {
        if(mThread.isAlive())mThread.interrupt();
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        moveCount = 0;
        tutorialNum = 1;
        stageCount = "0";
        finish();
        gameSurfaceView.setVisibility(View.INVISIBLE);
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
            if(mThread.isAlive())mThread.interrupt();
            ((StagePage) mContext).setStage();
            stageCount = String.valueOf(Integer.parseInt(stageCount) + 1);
            if(stageCount.equals("2")){
                gameSurfaceView.setVisibility(View.INVISIBLE);
                gameSurfaceView.setVisibility(View.VISIBLE);
            }
            moveCount = 0;
            StageDB.getMinCount(Integer.parseInt(stageCount));
            StageDB.getStageObj();
            drawInit=false;
            if (dialog != null)
                dialog.dismiss();
        }
    };
    private View.OnClickListener clr_ResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mThread.isAlive())mThread.interrupt();
            ((StagePage) mContext).setStage();
            moveCount = 0;
            tutorialNum = 1;
            StageDB.getMinCount(Integer.parseInt(stageCount));
            StageDB.getStageObj();
            if (dialog != null)
                dialog.dismiss();
        }
    }; //출처: http://yoo-hyeok.tistory.com/51 [유혁의 엉터리 개발]

    public void gameReset(){
        if(mThread.isAlive())mThread.interrupt();
        moveCount = 0;
        tutorialNum = 1;
        StageDB.getMinCount(Integer.parseInt(stageCount));
        StageDB.getStageObj();
    }
}
