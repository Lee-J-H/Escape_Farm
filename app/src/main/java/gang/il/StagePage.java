package gang.il;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import static gang.il.Valiable.CLEAR_STAGE;
import static gang.il.Valiable.STAGE_RESET;
import static gang.il.Valiable.StagePage;
import static gang.il.Valiable.LOAD_FINISH;
import static gang.il.Valiable.stageCount;

public class StagePage extends AppCompatActivity {
    Context mContext = this;
    ListView stageList;
    StageList stageListAdapter = new StageList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StagePage = this;
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//제목 없음
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stage_page);
        stageList = findViewById(R.id.stage_list);
        stageList.setAdapter(stageListAdapter);
    }

    final static Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_FINISH:
                    Intent intent = new Intent(StagePage, GamePage.class);
                    StagePage.startActivityForResult(intent, CLEAR_STAGE);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CLEAR_STAGE) {
            if (resultCode == RESULT_OK) {
                stageListAdapter.notifyDataSetChanged();
            }
        }
    }
}
