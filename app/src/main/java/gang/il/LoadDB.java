package gang.il;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static gang.il.StagePage.mhandler;
import static gang.il.Valiable.LOAD_FINISH;
import static gang.il.Valiable.STAGE_RESET;
import static gang.il.Valiable.clrDialogBtn;
import static gang.il.Valiable.finishObj;
import static gang.il.Valiable.minCount;
import static gang.il.Valiable.objCount;
import static gang.il.Valiable.stageCount;

import static gang.il.Valiable.stageSize_x;
import static gang.il.Valiable.stageSize_y;
import static gang.il.Valiable.totalObj;

public class LoadDB {
    public static Context mContext;
    public static String mJsonString;

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
                Log.d("test1234", "result:faild");
                //mhandler.sendEmptyMessage(Faild_internet);
            } else {
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String postParameters = "Stage=" + params[1];

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

    public static void showResult() {

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            if (jsonArray.length() != 1) {
                finishObj.clear();
                objCount = jsonArray.length();
                totalObj = new TotalObject[objCount];
            }
            int caveNum=0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                if (jsonArray.length() == 1) {
                    minCount = item.getInt("Count");
                    if(clrDialogBtn.equals("stageClk"))mhandler.sendEmptyMessage(LOAD_FINISH);
                } else {
                    String structure = item.getString("Structure");
                    int x = item.getInt("posX");
                    int y = item.getInt("posY");
                    int sort = item.getInt("sort_num");
                    switch (sort) {
                        case 10:
                            totalObj[i] = new TotalObject(x, y, structure, true);
                            putInFood(structure, i); //해당 동물의 음식 속성 부여
                            break;
                        case 110:
                            totalObj[i] = new TotalObject(x, y, structure, false);
                            totalObj[i].caveNum = ++caveNum;
                            break;
                        default:
                            totalObj[i] = new TotalObject(x, y, structure, false);
                            break;
                    }
                    if (totalObj[i].getType().endsWith("_fin")) finishObj.add(totalObj[i].getType().substring(0,totalObj[i].getType().indexOf("_"))); //finish 있는 동물 저장
                    if (i == 1) {
                        stageSize_x = x / 2;
                        stageSize_y = y / 2;
                    }
                    else if (i == jsonArray.length() - 1) {
                        //if (onReset)
                            //onReset = false;
                        if(!clrDialogBtn.equals("reset")) {
                            LoadDB.GetDB Data = new LoadDB.GetDB();
                            Data.execute("http://106.10.57.117/EscapeFarm/getminimum.php", stageCount); // 최소 횟수 로딩
                        }
                    }
                }
            }
        } catch (JSONException e) {
        }
    }

    public static void putInFood(String animalName, int animalIndex) {
        switch (animalName) {
            case "dog":
                totalObj[animalIndex].foods.add("food_bone");
                break;
            case "horse":
                totalObj[animalIndex].foods.add("food_carrot");
                break;
            case "mouse":
                totalObj[animalIndex].foods.add("food_cheese");
                break;
            case "rabbit":
                totalObj[animalIndex].foods.add("food_carrot");
                break;
        }
    }
}
