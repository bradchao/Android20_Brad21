package tw.org.iii.brad.brad21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private EditText max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        max = findViewById(R.id.max);
        webView = findViewById(R.id.webView);
        initWebView();
    }

    private void initWebView(){
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(true);

        webView.addJavascriptInterface(new MyJSObject(), "brad");

        //webView.loadUrl("https://www.iii.org.tw");
        webView.loadUrl("file:///android_asset/brad.html");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
            //webView.goForward();
            //webView.reload();
        }else {
            super.onBackPressed();
        }
    }

    public void test1(View view) {
        String strMax = max.getText().toString();
        Log.v("brad", String.format("javascript:test1(%s)", strMax));
        webView.loadUrl(String.format("javascript:test1(%s)", strMax));
    }

    public class MyJSObject {
        @JavascriptInterface
        public void callFromJS(String urname){
            Log.v("brad", "OK: " + urname);
            Message message = new Message();
            Bundle data = new Bundle();
            data.putString("urname", urname);
            message.setData(data);
            uiHandler.sendMessage(message);
            //max.setText(urname);
        }
    }

    private UIHandler uiHandler = new UIHandler();
    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String urname = msg.getData().getString("urname");
            max.setText(urname);
        }
    }

}
