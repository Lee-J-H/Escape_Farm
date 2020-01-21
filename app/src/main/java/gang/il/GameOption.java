package gang.il;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class GameOption extends AppCompatActivity {
    public static void writeSoundOp(Context context, String sound) {
        SharedPreferences mode = context.getSharedPreferences("sound", MODE_PRIVATE);
        SharedPreferences.Editor editor = mode.edit();
        editor.putString("sound", sound);
        editor.commit();
    }

    public static String readSoundOp(Context context) {
        SharedPreferences mode = context.getSharedPreferences("sound", MODE_PRIVATE);
        SharedPreferences.Editor editor = mode.edit();
        String sound = mode.getString("sound", "");
        return sound;
    }//http://devstory.ibksplatform.com/2017/12/android-sharedpreferences.html
}
