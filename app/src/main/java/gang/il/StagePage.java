package gang.il;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static gang.il.CheckedStage.onCheckStage;
import static gang.il.Valiable.CLEAR_STAGE;
import static gang.il.Valiable.StagePage;
import static gang.il.Valiable.LOAD_FINISH;

public class StagePage extends AppCompatActivity implements View.OnClickListener {
    Context mContext = this;
    LinearLayout[] stageNum = new LinearLayout[4];
    ImageView[] lockImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StagePage = this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_page);
        inti();
        matchStage();
    }

    public void inti() {
        for (int i = 0; i < stageNum.length; i++) {
            int stageID = getResources().getIdentifier("stage_" + (i + 1), "id", "gang.il");
            stageNum[i] = findViewById(stageID);
            stageNum[i].setOnClickListener(this);
        }
    }

    public void matchStage() {
        int succeedStage = Integer.parseInt(onCheckStage(mContext));
        lockImg = new ImageView[succeedStage + 1]; //클리어된 스테이지 확인
        for (int i = 0; i < lockImg.length; i++) {
            int stageID = getResources().getIdentifier("lock_" + (i + 1), "id", "gang.il");
            lockImg[i] = findViewById(stageID);
            lockImg[i].setVisibility(View.GONE);
            stageNum[i].setBackgroundColor(Color.parseColor("#F4DFAC"));

        }
    }

    @Override
    public void onClick(View v) {
        String stage = v.getTag().toString();
        Log.d("test1235","stageclick");
        if (Integer.parseInt(stage) > lockImg.length) return;
        LoadDB.GetDB Data = new LoadDB.GetDB();
        Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", stage);  //스테이지DB 로딩
    }

    final static Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_FINISH:
                    Intent intent = new Intent(StagePage, GamePage.class);
                    StagePage.startActivityForResult(intent, CLEAR_STAGE);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CLEAR_STAGE) {
            if (resultCode == RESULT_OK) {
                matchStage();
            }
        }
    }
}
