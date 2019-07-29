package gang.il;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import static gang.il.Valiable.gameMode;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Button classic_btn =(Button)findViewById(R.id.classic_btn);
        classic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = "classic";
                Intent intent = new Intent(MainPage.this,StagePage.class);
                startActivity(intent);
            }
        });
        Button blind_btn =(Button)findViewById(R.id.blind_btn);
        blind_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameMode = "blind";
                Intent intent = new Intent(MainPage.this,StagePage.class);
                startActivity(intent);
            }
        });
    }
}