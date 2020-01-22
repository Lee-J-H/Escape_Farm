package gang.il;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import static gang.il.LoadingImg.progressDialog;
import static gang.il.LoadingImg.progressOFF;
import static gang.il.Valiable.StagePage;
import static gang.il.Valiable.LOAD_FINISH;
import static gang.il.Valiable.StartPage;
import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.mContext;
import static gang.il.Valiable.btnSound;
import static gang.il.Valiable.soundPlay;
import static gang.il.Valiable.soundPool;

public class StagePage extends AppCompatActivity {
    private long mLastClickTime = 0;
    private TextView backBtn, gameModeTxt;
    RecyclerView mRecyclerView = null;
    StagePagerAdapter mAdapter = null;
    StageDBHelper StageDB;
    ArrayList<RecyclerItem> mList = new ArrayList<>();
    Drawable[][] imgData = new Drawable[15][4];
    int[][] staData = new int[15][4];
    String[][] moveData = new String[15][4];
    int succeedStage;
    Context StageContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        StageContext=this;
        StagePage = this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_page);
        init();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                if(soundPlay) soundPool.play(btnSound, 1f, 1f, 0, 0, 1f); //버튼 사운드 재생
                Intent intent = new Intent(StagePage, MainPage.class);
                StagePage.startActivity(intent);
                finish();
            }
        });

    }

    final static Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_FINISH:
                    if(progressDialog.isShowing())
                        progressOFF();
                    Intent intent = new Intent(StartPage, MainPage.class);
                    StartPage.startActivity(intent);
                    StartPage.finish();
                    break;
            }
        }
    };

    public void init(){
        backBtn = (TextView) findViewById(R.id.back_btn);
        gameModeTxt = (TextView) findViewById(R.id.gameMode);
        if(gameMode.equals("day")) gameModeTxt.setText("낮");
        else gameModeTxt.setText("밤");
        mRecyclerView = findViewById(R.id.recycler1);
        mAdapter = new StagePagerAdapter(mList,StageContext); // 리사이클러뷰에 StagePageReAdapter 객체 지정.
        mRecyclerView.setAdapter(mAdapter);
        // 리사이클러뷰에 LinearLayoutManager 지정. (vertical)
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        StageDB = new StageDBHelper(StageContext);
        succeedStage = StageDB.clearStageNum()+1;
        mList.clear();
        int count = 1;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 4; j++) {
                staData[i][j] = count;
                if (count++ <= succeedStage) {
                    imgData[i][j] = StageContext.getResources().getDrawable(R.drawable.button);
                    moveData[i][j] = StageDB.getMyMinCount(staData[i][j]) + "/" + StageDB.getMinCount(staData[i][j]);
                } else {
                    moveData[i][j] = "0/" + StageDB.getMinCount(staData[i][j]);
                    imgData[i][j] = StageContext.getResources().getDrawable(R.drawable.lock_button);
                }
            }
            addItem(imgData[i], staData[i], moveData[i]);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setStage(int stage, int minCount){
        int position=(stage-1)/4;
        int num=(stage%4)-1;
        if(num==-1) num=3;
        moveData[position][num]=minCount + "/"+StageDB.getMinCount(stage);
        if(num!=3)imgData[position][num+1] = StageContext.getResources().getDrawable(R.drawable.button);
        else imgData[position+1][0] = StageContext.getResources().getDrawable(R.drawable.button);
        mAdapter.notifyDataSetChanged();
    }

    public void addItem(Drawable[] btnImg, int[] stage, String[] move) {
        RecyclerItem item = new RecyclerItem();
        item.setBtnImg(btnImg);
        item.setStageNum(stage);
        item.setMoveNum(move);
        mList.add(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StagePage, MainPage.class);
        StagePage.startActivity(intent);
        finish();
    }
}
