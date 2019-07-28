package gang.il;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class CheckedStage extends Activity {
    static String[] stageText; //스테이지 클리어 점수 저장 변수
    public static void onClearStage(Context context, int stage, int rating) {  //스테이지 완료시 저장
        StringBuilder stageTextBuf = new StringBuilder();
        if(stage < stageText.length) {  //현재 저장된 마지막 스테이지 이전의 스테이지들을 완료한 경우
            if (Integer.parseInt(stageText[stage]) < rating);  //저장된 점수 이상의 점수를 얻은 경우
            stageText[stage] = String.valueOf(rating);
            for(int i=0; i<stageText.length; i++) {
                stageTextBuf.append(stageText[i]);
                if(i!=stageText.length)stageTextBuf.append(",");
            }
        }
        else{ //현재 저장된 마지막 스테이지 다음의 스테이지를 완료한 경우
            for(int i=0; i<stageText.length; i++) {
                stageTextBuf.append(stageText[i]);
                stageTextBuf.append(",");
            }
            stageTextBuf.append(rating);
        }
        SharedPreferences mode = context.getSharedPreferences("stageTest", MODE_PRIVATE);
        SharedPreferences.Editor editor = mode.edit();
        editor.putString("stage", stageTextBuf.toString());
        editor.commit();
        stageTextBuf.delete(0,stageTextBuf.length());
    }

    public static int onCheckStage(Context context) { //현재까지 완료된 스테이지 확인
        SharedPreferences mode = context.getSharedPreferences("stageTest", MODE_PRIVATE);
        SharedPreferences.Editor editor = mode.edit();
        String stage = mode.getString("stage", "0");
        stageText = stage.split(",");
        return stageText.length;
    }//http://devstory.ibksplatform.com/2017/12/android-sharedpreferences.html
}