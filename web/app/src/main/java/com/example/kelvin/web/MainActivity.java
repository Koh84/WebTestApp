package com.example.kelvin.web;

import android.content.Context;
import android.content.res.Resources;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/*
    https://blog.csdn.net/mrxiagc/article/details/75329629
    https://developer.android.google.cn/training/articles/security-config
*/


public class MainActivity extends AppCompatActivity {

    WebView myWebView = null;
    CertificateFactory cf = null;
    InputStream caInput = null;
    Certificate certificate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      /*  try {
            // Get cert from raw resource...
            cf = CertificateFactory.getInstance("X.509");
            caInput = getResources().openRawResource(R.raw.rootca); // stored at \app\src\main\res\raw
            certificate = cf.generateCertificate(caInput);
            caInput.close();
        }catch(Exception e)
        {}
*/
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // Get cert from SslError
             /*   SslCertificate sslCertificate = error.getCertificate();
                Certificate cert = getX509Certificate(sslCertificate);
                if (cert != null && certificate != null){
                    try {
                        // Reference: https://developer.android.com/reference/java/security/cert/Certificate.html#verify(java.security.PublicKey)
                        Log.d("KEYYYYYYYYY1", certificate.getPublicKey().toString());
                        Log.d("KEYYYYYYYYY2", cert.getPublicKey().toString());

                        cert.verify(certificate.getPublicKey()); // Verify here...
                        handler.proceed();
                    } catch (Exception e) {
                        super.onReceivedSslError(view, handler, error);
                        e.printStackTrace();
                        Log.d("XXXXXXXXX", "onReceivedSslError Exception");
                    }
                } else {
                    super.onReceivedSslError(view, handler, error);
                }*/
            }
        });
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);






        Button buttonGo = (Button) findViewById(R.id.buttonGO);

        final EditText editTextGo = (EditText) findViewById(R.id.editTextGO);

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWebView.loadUrl(editTextGo.getText().toString());
            }
        });
    }

    // credits to @Heath Borders at http://stackoverflow.com/questions/20228800/how-do-i-validate-an-android-net-http-sslcertificate-with-an-x509trustmanager
    private Certificate getX509Certificate(SslCertificate sslCertificate){
        Bundle bundle = SslCertificate.saveState(sslCertificate);
        byte[] bytes = bundle.getByteArray("x509-certificate");
        if (bytes == null) {
            return null;
        } else {
            try {
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                return certFactory.generateCertificate(new ByteArrayInputStream(bytes));
            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
