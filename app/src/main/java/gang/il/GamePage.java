package gang.il;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import static gang.il.Valiable.minCount;
import static gang.il.Valiable.moveCount;
import static gang.il.Valiable.onReset;
import static gang.il.Valiable.stageCount;

public class GamePage extends AppCompatActivity {
    StageClearDialog dialog;
    Context context = this;
    Button backBtn, resetBtn;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveCount = 0;
        stageCount = "0";
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);
        init();
    }

    public void init() {
        backBtn = (Button) findViewById(R.id.back);
        resetBtn = (Button) findViewById(R.id.reset);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backStage();
            }
        });
        resetBtn.setOnClickListener(ResetListener);
    }

    public void setMoveCount() {
        TextView moveText = findViewById(R.id.moveCount);
        moveText.setText("이동횟수: " + moveCount);
    }

    public void setStageCount() {
        TextView stageText = findViewById(R.id.stageCount);
        stageText.setText("스테이지: " + stageCount);
    }

    public void backStage() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        moveCount = 0;
        finish();
    }


    public void Dialog() {
        dialog = new StageClearDialog(context,
                "스테이지 완료" + "\n이동횟수:" + moveCount + "\n점수:" + checkMinimumMove()+"점", // 내용
                OKDialogListener, ResetListener); // 왼쪽 버튼 이벤트
        // 오른쪽 버튼 이벤트

        //요청 이 다이어로그를 종료할 수 있게 지정함
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    //다이얼로그 클릭이벤트
    private View.OnClickListener OKDialogListener = new View.OnClickListener() {
        public void onClick(View v) {
            backStage();
            dialog.dismiss();
        }
    };
    private View.OnClickListener ResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onReset=true;
            moveCount=0;
            setMoveCount();
            LoadDB.GetDB Data = new LoadDB.GetDB();
            Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", stageCount);  //스테이지 재로딩
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
