package gang.il;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.MainPage;
import static gang.il.Valiable.mContext;
import static gang.il.Valiable.btnSound;
import static gang.il.Valiable.soundPlay;
import static gang.il.Valiable.soundPool;

public class MainPage extends AppCompatActivity {
    private long mLastClickTime = 0;
    ImageView sound_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        MainPage=this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        init();
        soundBtnImg();
    }
    public void init(){
        Button classic_btn =(Button)findViewById(R.id.classic_btn);
        classic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = "day";
                onBtnClick();
            }
        });
        Button blind_btn =(Button)findViewById(R.id.blind_btn);
        blind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = "night";
                onBtnClick();
            }
        });
        Button gameOff_btn =(Button)findViewById(R.id.gameOff_btn);
        gameOff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sound_btn =(ImageView)findViewById(R.id.sound_btn);
        sound_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundPlay){
                    soundPlay=false;
                    sound_btn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sound_off));
                }
                else{
                    soundPlay=true;
                    sound_btn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sound_on));
                    soundPool.play(btnSound, 1f, 1f, 0, 0, 1f); //버튼 사운드 재생
                }
            }
        });
    }

    public void soundBtnImg(){
        if(soundPlay) sound_btn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sound_on));
        else sound_btn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sound_off));
    }


    public void onBtnClick(){
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        if(soundPlay)soundPool.play(btnSound, 1f, 1f, 0, 0, 1f); //버튼 사운드 재생
        Intent intent = new Intent(MainPage, StagePage.class);
        MainPage.startActivity(intent);
        finish();
    }
}