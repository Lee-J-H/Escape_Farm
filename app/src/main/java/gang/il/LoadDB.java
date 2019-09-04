package gang.il;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static gang.il.LoadingImg.progressDialog;
import static gang.il.LoadingImg.progressOFF;
import static gang.il.StagePage.mhandler;
import static gang.il.Valiable.Failed_internet;
import static gang.il.Valiable.LOAD_FINISH;
import static gang.il.Valiable.totalObj;
import static gang.il.Valiable.dialog;
import static gang.il.Version.readVersion;
import static gang.il.Version.writeVersion;

public class LoadDB {
    public static Context mContext;
    public static String mJsonString;
    public static String loadMode;
    public static String loadType;


    public LoadDB(Context context) {
        mContext = context;
    }

    public static class GetDB extends AsyncTask<String, Void, String> {
        String errorString = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null) {
                Log.d("failed", "result:failed_internet");
                mhandler.sendEmptyMessage(Failed_internet);
            } else {
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = "&gameMode=" + params[1];
            loadMode = params[1];
            loadType = params[2];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {
                errorString = e.toString();
                Log.d("Failed get DB", "result:" + errorString);
                return null;
            }

        }
    }

    public static void failedInternet(Context mContext) {

        View.OnClickListener DialogListener = new View.OnClickListener() {
            LoadDB.GetDB Data = new LoadDB.GetDB();

            public void onClick(View v) {
                if (progressDialog.isShowing()) progressOFF();
                dialog.dismiss();
            }
        };
        dialog = new StageClearDialog(mContext,
                "인터넷 연결을 확인해주세요", // 내용
                DialogListener); // 왼쪽 버튼 이벤트
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    private static void showResult() {
        if(loadType.equals("minCount"))getMinCount(loadMode);
        else if(loadType.equals("check"))
            checkVersion();
        else getStage(loadMode);
    }

    private static void checkVersion(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            LoadDB.GetDB Data = new LoadDB.GetDB();
            JSONObject item = jsonArray.getJSONObject(0);
            String version = item.getString("Count");
            if(!readVersion(mContext).equals(version)) {
                Data.execute("http://106.10.57.117/EscapeFarm/minimumcount.php", "day", "minCount");  //서버에서 주간모드 최소횟수 로딩
                writeVersion(mContext,version);
            }
            else mhandler.sendEmptyMessage(LOAD_FINISH);
        } catch (JSONException e) {
        }
    }

    private static void getMinCount(String mode){
        try {
            StageDBHelper stageDB = new StageDBHelper(mContext);
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                int stage = item.getInt("Stage");
                int Count = item.getInt("Count");
                stageDB.initCountDB(mode,Count,stage);  //처음 한번만 실행되도록 처리해야됨

                if(i == jsonArray.length() -1){ //주간모드 마지막 스테이지 로드 후 야간모드 로드 시작
                    if(mode.equals("day")) {

                        LoadDB.GetDB Data = new LoadDB.GetDB();
                        Data.execute("http://106.10.57.117/EscapeFarm/minimumcount.php", "night", "minCount");  //서버에서 스테이지 정보(최소횟수) 로딩
                    }
                    else{
                        LoadDB.GetDB Data = new LoadDB.GetDB();
                        Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", "day", "gameObj");  //서버에서 스테이지 정보 로딩
                    }
                }
            }
        } catch (JSONException e) {
        }
    }
    private static void getStage(String mode){
        try {
            StageDBHelper stageDB = new StageDBHelper(mContext);
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            TotalObject[] obj = new TotalObject[1];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                int stage = item.getInt("Stage");
                int posX = item.getInt("posX");
                int posY = item.getInt("posY");
                int sortNum = item.getInt("sort_num");
                String structure = item.getString("Structure");
                obj[0] = new TotalObject(posX,posY,structure,false);
                stageDB.initObjDB(mode, stage,sortNum, obj[0]); //처음 한번만 실행되도록 처리해야됨

                if(i == jsonArray.length() -1){ //주간모드 마지막 스테이지 로드 후 야간모드 로드 시작
                    if(mode.equals("day")) {
                        LoadDB.GetDB Data = new LoadDB.GetDB();
                        Data.execute("http://106.10.57.117/EscapeFarm/getStage.php", "night", "gameObj");  //서버에서 스테이지 정보(최소횟수) 로딩
                    }
                    else mhandler.sendEmptyMessage(LOAD_FINISH); //모든 스테이지 정보 로딩 완료
                }

            }
        } catch (JSONException e) {
        }
    }


    private static void putInFood(String animalName, int animalIndex) {
        switch (animalName) {
            case "dog":
                totalObj[animalIndex].foods.add("food_bone");
                break;
            case "squirrel":
                totalObj[animalIndex].foods.add("food_acorn");
                break;
            case "panda":
                totalObj[animalIndex].foods.add("food_bamboo");
                break;
            case "rabbit":
                totalObj[animalIndex].foods.add("food_carrot");
                break;
            case "tiger":
                totalObj[animalIndex].foods.add("food_meat");
                break;
        }
    }
}
