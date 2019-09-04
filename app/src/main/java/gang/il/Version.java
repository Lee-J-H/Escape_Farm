package gang.il;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class Version extends AppCompatActivity {
    public static void writeVersion(Context context, String version) {
        SharedPreferences mode = context.getSharedPreferences("version", MODE_PRIVATE);
        SharedPreferences.Editor editor = mode.edit();
        editor.putString("version", version);
        editor.commit();
    }

    public static String readVersion(Context context) {
        SharedPreferences mode = context.getSharedPreferences("version", MODE_PRIVATE);
        SharedPreferences.Editor editor = mode.edit();
        String version = mode.getString("version", "");
        return version;
    }//http://devstory.ibksplatform.com/2017/12/android-sharedpreferences.html

}