package gang.il;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class GamePage extends AppCompatActivity {
    StageClearDialog dialog;
    Context context = this;
    public  int moveCount, stageCount;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveCount=0;
        stageCount=0;
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_page);
    }

    public void setMoveCount(){
        TextView moveText = findViewById(R.id.moveCount);
        moveText.setText("이동횟수: "+ moveCount);
    }

    public void setStageCount(){
        TextView stageText = findViewById(R.id.stageCount);
        stageText.setText("스테이지: " + stageCount);
    }

    public void backStage(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }


    public void Dialog() {
        dialog = new StageClearDialog(context,
                "스테이지 완료"+"\n이동횟수:"+moveCount, // 내용
                DialogListener); // 왼쪽 버튼 이벤트
        // 오른쪽 버튼 이벤트

        //요청 이 다이어로그를 종료할 수 있게 지정함
        dialog.setCancelable(true);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    //다이얼로그 클릭이벤트
    private View.OnClickListener DialogListener = new View.OnClickListener() {
        public void onClick(View v) {
            backStage();
            finish();
            dialog.dismiss();
        }
    };
//출처: http://yoo-hyeok.tistory.com/51 [유혁의 엉터리 개발]
}
