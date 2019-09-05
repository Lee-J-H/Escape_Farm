package gang.il;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import static gang.il.LoadDB.failedInternet;
import static gang.il.LoadingImg.progressDialog;
import static gang.il.LoadingImg.progressOFF;
import static gang.il.Valiable.CLEAR_STAGE;
import static gang.il.Valiable.StagePage;
import static gang.il.Valiable.LOAD_FINISH;
import static gang.il.Valiable.StartPage;
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
        pageNum = StageDB.clearStageNum()/20; //마지막으로 클리어한 페이지 기준으로 보기
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
                    if(progressDialog.isShowing())
                        progressOFF();
                    Intent intent = new Intent(StartPage, MainPage.class);
                    StartPage.startActivity(intent);
                    StartPage.finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CLEAR_STAGE) {
            if (resultCode == RESULT_OK) {
                mContext=this;
                pagerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
