package com.example.kelvin.web2;



import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;


public class MainActivity extends AppCompatActivity {

    final String username = "myUser";
    final String password = "myPasscode";

    Button btn;
    TextView tv;
    TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);

        Authenticator.setDefault(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute("https://www.hotmail.com");
            }

        });

    }

    public static String readStream(InputStream in) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }



    class MyTask extends AsyncTask<String, Integer, String> {
        private Exception exception;

        protected String doInBackground(String... urls) {

            publishProgress(0);
            String out = null;
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                //urlConnection.setRequestMethod("GET");
                //urlConnection.setRequestProperty("Proxy-Authorization", "Basic bXlVc2VyOm15UGFzc2NvZGU=");
                publishProgress(1);

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                publishProgress(2);

                out = readStream(in);
                publishProgress(3);
                tv2.setText(Integer.toString(urlConnection.getResponseCode()));

                writeToFile(out, getApplicationContext());
            }
            catch (Exception e){}
            finally
            {
                urlConnection.disconnect();
                publishProgress(4);
            }

            return out;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            tv.setText(Integer.toString(values[0]));
        }

        protected void onPostExecute(String result) {
            tv.setText(result);
        }
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("ABCconfig.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (Exception e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
