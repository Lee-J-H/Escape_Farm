package gang.il;

import android.content.Context;
import android.content.Intent;
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
                gameMode = "classic";
                init();
            }
        });
        Button blind_btn =(Button)findViewById(R.id.blind_btn);
        blind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = "blind";
                init();
            }
        });
    }
    public void init(){
        progressON(MainPage,null);
        StageDBHelper StageDB = new StageDBHelper(mContext);
        StageDB.selectDB();
        stageCount=String.valueOf(StageDB.getClearedStage());
        LoadDB.GetDB Data = new LoadDB.GetDB();
        Data.execute("http://106.10.57.117/EscapeFarm/getClearedStageMin.php", String.valueOf(Integer.valueOf(stageCount)+1), "min_count", gameMode);  //클리어 된 스테이지들의 최소 회수 로딩
    }
}