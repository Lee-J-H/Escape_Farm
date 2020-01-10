package gang.il;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import static gang.il.Valiable.gameMode;
import static gang.il.Valiable.MainPage;
import static gang.il.Valiable.mContext;
import static gang.il.Valiable.main_btnSound;
import static gang.il.Valiable.soundPool;

public class MainPage extends AppCompatActivity {
    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext=this;
        MainPage=this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        main_btnSound = soundPool.load(this, R.raw.main_btn, 1);
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
        soundPool.play(main_btnSound, 1f, 1f, 0, 0, 1f); //버튼 사운드 재생
        if (SystemClock.elapsedRealtime() - mLastClickTime < 100){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Intent intent = new Intent(MainPage, StagePage.class);
        MainPage.startActivity(intent);
    }
}