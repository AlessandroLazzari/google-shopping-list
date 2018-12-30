package it.alessandro.lazzari.googleshoppinglistwebapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;

public class MainActivity extends AppCompatActivity implements AdvancedWebView.Listener, ListenerPageFinished.IListenerPageFinished {

    @BindView(R.id.webview)
    AdvancedWebView mWebView;

    private ListenerPageFinished mListenerPageFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mListenerPageFinished = new ListenerPageFinished(this);

        mWebView.setListener(this, this);
        mWebView.addJavascriptInterface(mListenerPageFinished, "HTMLOUT");
        mWebView.setGeolocationEnabled(Boolean.TRUE);

        mWebView.loadUrl("https://shoppinglist.google.com/", Boolean.TRUE);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {
        mWebView.loadUrl("javascript:window.HTMLOUT.processHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @Override
    public void onLoadPageFinished(String html) {

    }
}
