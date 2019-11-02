package gang.il;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import static gang.il.LoadingImg.progressON;
import static gang.il.Valiable.StartPage;



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
}
