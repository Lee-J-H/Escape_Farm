package gang.il;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartPage extends AppCompatActivity {
    Thread w;
    boolean running = true, blink = true;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);
        LinearLayout fullscreen = (LinearLayout) findViewById(R.id.screen);
        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                running = false;
                Intent intent = new Intent(StartPage.this,MainPage.class);
                startActivity(intent);
                finish();
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
