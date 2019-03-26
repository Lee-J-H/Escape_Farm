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

import static gang.il.StagePage.loadFinish;
import static gang.il.StagePage.mhandler;

public class LoadDB {
    public static Context mContext;
    public static String mJsonString;
    public static String getStage;
    public static String getMini;
    public static int MinCount;
    public static int boardSize;

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
                Log.d("test1234","result:faild");
                //mhandler.sendEmptyMessage(Faild_internet);
            } else {
                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            getStage = params[1];
            getMini = params[2];

            String postParameters = "Stage=" + params[1] + "&Mini=" + params[2];

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
                Log.d("Failed get DB","result:"+errorString);
                return null;
            }

        }
    }

    public static void showResult() {

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                if (jsonArray.length()==1) {
                    MinCount = item.getInt("Count");
                    Log.d("testMinCount","result:" + MinCount);
                    mhandler.sendEmptyMessage(loadFinish);
                }
                else {
                    String structure = item.getString("Structure");
                    int x = item.getInt("X");
                    int y = item.getInt("Y");
                    if(i==1) boardSize= x/2;
                    else if(i == jsonArray.length()-1) {
                        LoadDB.GetDB Data = new LoadDB.GetDB();
                        Data.execute("http://106.10.57.117/EscapeFarm/getminimum.php", item.getString("Stage"), "mini"); // 최소 횟수 로딩
                    }
                    Log.d("testStageDB","result:"+structure + "/" + x + "/" + y);
                }
            }
        } catch (JSONException e) {

        }
    }
}
