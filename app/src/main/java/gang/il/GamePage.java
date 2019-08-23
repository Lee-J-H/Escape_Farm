package gang.il;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import static gang.il.LoadingImg.progressDialog;
import static gang.il.LoadingImg.progressOFF;
import static gang.il.LoadingImg.progressON;
import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.mContext;
import static gang.il.Valiable.minCount;
import static gang.il.Valiable.min_count_ser;
import static gang.il.Valiable.moveCount;
import static gang.il.Valiable.stageCount;
import static gang.il.Valiable.tutorialNum;
import static gang.il.Valiable.dialog;

public class GamePage extends AppCompatActivity {

    ImageView backBtn, resetBtn;
    Activity GamePage = this;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backStage();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);
        init();
    }

    public void init() {
        backBtn = (ImageView) findViewById(R.id.back);
        resetBtn = (ImageView) findViewById(R.id.reset);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backStage();
            }
        });
        resetBtn.setOnClickListener(ResetListener);
        setMinCount();
        tutorialNum=1;
        if(progressDialog != null)
            progressOFF();
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
        min_text.setText("최소횟수: " + min_count_ser.get(Integer.parseInt(stageCount)-1));
    }

    public void backStage() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        moveCount = 0;
        tutorialNum=1;
        stageCount = "0";
        progressON(GamePage,null);
        finish();
    }


    public void Dialog() {
        dialog = new StageClearDialog(mContext,
                "스테이지 완료" + "\n이동횟수:" + moveCount + "\n점수:", // 내용
                OKDialogListener, clr_ResetListener); // 왼쪽 버튼 이벤트
        // 오른쪽 버튼 이벤트

        //요청 이 다이어로그를 종료할 수 있게 지정함
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    //다이얼로그 클릭이벤트
    private View.OnClickListener OKDialogListener = new View.OnClickListener() {
        public void onClick(View v) {
            progressON(GamePage,null);
            stageCount = String.valueOf(Integer.parseInt(stageCount)+1);
            LoadDB.GetDB Data = new LoadDB.GetDB();
            Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", stageCount, "next_stage_min", gameMode);  //스테이지DB 로딩
            moveCount=0;
            setMoveCount();
            setStageCount();
            setMinCount();
            dialog.dismiss();
        }
    };
    private View.OnClickListener clr_ResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressON(GamePage,null);
            moveCount=0;
            tutorialNum=1;
            setMoveCount();
            LoadDB.GetDB Data = new LoadDB.GetDB();
            Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", stageCount, "clr_reset_min", gameMode);  //스테이지 재로딩
            if(dialog!=null)
                dialog.dismiss();
        }
    };
    private View.OnClickListener ResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressON(GamePage,null);
            moveCount=0;
            tutorialNum=1;
            setMoveCount();
            LoadDB.GetDB Data = new LoadDB.GetDB();
            Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", stageCount, "non_clr_reset", gameMode);  //스테이지 재로딩
            if(dialog!=null)
                dialog.dismiss();
        }
    };
//출처: http://yoo-hyeok.tistory.com/51 [유혁의 엉터리 개발]

    public int checkMinimumMove() {
        int rating;
        if (moveCount <= minCount)
            rating = 10;
        else if (moveCount < minCount + 2)
            rating = 5;
        else
            rating = 1;
        return rating;
    }
}
