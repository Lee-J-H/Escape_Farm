package gang.il;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CheckedStage extends Activity {
    public static void onClearStage(Context context, String stage) {  //스테이지 완료시 저장
        SharedPreferences mode = context.getSharedPreferences("stage", MODE_PRIVATE);
        SharedPreferences.Editor editor = mode.edit();
        editor.putString("stage", stage);
        editor.commit();
    }

    public static String onCheckStage(Context context) { //현재까지 완료된 스테이지 확인
        SharedPreferences mode = context.getSharedPreferences("stage", MODE_PRIVATE);
        SharedPreferences.Editor editor = mode.edit();
        String stage = mode.getString("stage", "");
        if(stage.equals("")) stage = "0";
        return stage;
    }//http://devstory.ibksplatform.com/2017/12/android-sharedpreferences.html
}