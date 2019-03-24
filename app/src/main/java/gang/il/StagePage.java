package gang.il;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static gang.il.CheckedStage.onCheckStage;

public class StagePage extends AppCompatActivity implements View.OnClickListener {
    static final int CLEAR_STAGE = 1;
    Context mContext = this;
    LinearLayout[] stageNum = new LinearLayout[4];
    ImageView[] lockImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_page);
        inti();
        matchStage();
    }

    public void inti() {
        for (int i = 0; i < stageNum.length; i++) {
            int stageID = getResources().getIdentifier("stage_" + (i + 1), "id", "team_2e.escape_farm");
            stageNum[i] = findViewById(stageID);
            stageNum[i].setOnClickListener(this);
        }
    }

    public void matchStage() {
        int succeedStage = Integer.parseInt(onCheckStage(mContext));
        lockImg = new ImageView[succeedStage + 1]; //클리어된 스테이지 확인
        for (int i = 0; i < lockImg.length; i++) {
            int stageID = getResources().getIdentifier("lock_" + (i + 1), "id", "team_2e.escape_farm");
            lockImg[i] = findViewById(stageID);
            lockImg[i].setVisibility(View.GONE);
            stageNum[i].setBackgroundColor(Color.parseColor("#F4DFAC"));

        }
    }

    @Override
    public void onClick(View v) {
        String stage = v.getTag().toString();

        if (Integer.parseInt(stage) > lockImg.length) return;
        //LoadDB DBA = new LoadDB(mContext);
        LoadDB.GetDB Data = new LoadDB.GetDB();
        Data.execute("http://106.10.57.117/getStage.php", stage,"");  //스테이지DB 로딩
        Data.execute("http://106.10.57.117/getMinimum.php",stage,"mini"); // 최소 횟수 로딩

        Intent intent = new Intent(StagePage.this, GamePage.class);
        startActivityForResult(intent, CLEAR_STAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CLEAR_STAGE) {
            if (resultCode == RESULT_OK) {
                matchStage();
            }
        }
    }
}
