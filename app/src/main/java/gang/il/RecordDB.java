package gang.il;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class RecordDB {
    public static Context mContext;
    public static String mJsonString;

    public RecordDB(Context context) {
        mContext = context;
    }

    public static class SetDB extends AsyncTask<String, Void, String> {
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
            String postParameters = "Stage=" + params[1]+"&min_count="+params[2];

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
                Toast.makeText(mContext,"recordSucceed",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

}

