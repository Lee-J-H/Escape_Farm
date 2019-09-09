package gang.il;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import static gang.il.LoadingImg.progressDialog;
import static gang.il.LoadingImg.progressON;
import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.stageCount;
import static gang.il.Valiable.MainPage;
import static gang.il.Valiable.mContext;

public class MainPage extends AppCompatActivity {
    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        MainPage=this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Button classic_btn =(Button)findViewById(R.id.classic_btn);
        classic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = "day";
                init();
            }
        });
        Button blind_btn =(Button)findViewById(R.id.blind_btn);
        blind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = "night";
                init();
            }
        });
    }
    public void init(){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Intent intent = new Intent(MainPage, StagePage.class);
        MainPage.startActivity(intent);
    }
}