package gang.il;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import static gang.il.LoadDB.failedInternet;
import static gang.il.Valiable.CLEAR_STAGE;
import static gang.il.Valiable.Faild_internet;
import static gang.il.Valiable.LOAD_STAGE_COUNT;
import static gang.il.Valiable.STAGE_RESET;
import static gang.il.Valiable.StagePage;
import static gang.il.Valiable.LOAD_FINISH;
import static gang.il.Valiable.MainPage;
import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.mContext;

public class StagePage extends AppCompatActivity {

    private ViewPager viewPager ;
    private StagePagerAdapter pagerAdapter ;
    private TextView backBtn, gameModeTxt;
    private int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        StagePage = this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_page);
        StageDBHelper StageDB = new StageDBHelper(mContext);
        StageDB.selectDB();
        pageNum = StageDB.getClearedStage()/20;
        backBtn = (TextView) findViewById(R.id.back_btn);
        gameModeTxt = (TextView) findViewById(R.id.gameMode);
        gameModeTxt.setText(gameMode);
        viewPager = (ViewPager) findViewById(R.id.viewPager) ;
        pagerAdapter = new StagePagerAdapter(this) ;
        viewPager.setAdapter(pagerAdapter) ;
        viewPager.setCurrentItem(pageNum);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    final static Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_FINISH:
                    Intent intent = new Intent(StagePage, GamePage.class);
                    StagePage.startActivityForResult(intent, CLEAR_STAGE);
                    break;
                case LOAD_STAGE_COUNT:
                    intent = new Intent(MainPage,StagePage.class);
                    MainPage.startActivity(intent);
                    break;
                case Faild_internet:
                    failedInternet(mContext);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CLEAR_STAGE) {
            if (resultCode == RESULT_OK) {
                mContext=this;
                //stageListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
