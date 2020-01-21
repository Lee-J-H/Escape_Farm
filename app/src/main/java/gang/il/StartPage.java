package gang.il;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import static gang.il.GameOption.readSoundOp;
import static gang.il.LoadingImg.progressON;
import static gang.il.Valiable.StartPage;
import static gang.il.Valiable.clearSound;
import static gang.il.Valiable.eatSound;
import static gang.il.Valiable.holeSound;
import static gang.il.Valiable.btnSound;
import static gang.il.Valiable.passSound;
import static gang.il.Valiable.soundPlay;
import static gang.il.Valiable.soundPool;
import static gang.il.Valiable.trapSound;
import static gang.il.Valiable.wallSound;


public class StartPage extends AppCompatActivity {
    Thread w;
    boolean running = true, blink = true;
    Context context;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StartPage = this;
        context = this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        LinearLayout fullscreen = (LinearLayout) findViewById(R.id.screen);
        soundInit();
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                running = false;
                LoadDB loadDB = new LoadDB(context);
                LoadDB.GetDB Data = new LoadDB.GetDB();
                progressON(StartPage,null);
                Data.execute("http://34.74.154.52/escapefarm/checkversion.php", "version","check");  //DB 버전 체크
            }
        });
    }

    @Override
    public void onStart() {
        final TextView tap = (TextView) findViewById(R.id.tap);
        super.onStart();
        w = new Thread(new Runnable() {
            public void run() {
                while(running) {
                    try{
                        Thread.sleep(800);
                    }catch (InterruptedException e){
                    }
                    blink=!blink;
                    tap.post(new Runnable() {
                        @Override
                        public void run() {
                            if (blink) tap.setVisibility(View.VISIBLE);
                            else tap.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
        w.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        running = false;
    }

    private void soundInit(){
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        btnSound = soundPool.load(this, R.raw.main_btn, 1);
        clearSound = soundPool.load(this, R.raw.clear, 1);
        passSound = soundPool.load(this, R.raw.pass, 1);
        eatSound = soundPool.load(this, R.raw.eat, 1);
        trapSound  = soundPool.load(this, R.raw.trap, 1);
        holeSound = soundPool.load(this, R.raw.hole, 1);
        wallSound = soundPool.load(this, R.raw.wall, 1);

        if(readSoundOp(context).equals("off"))
            soundPlay=false;
        else
            soundPlay=true;
    }
}
