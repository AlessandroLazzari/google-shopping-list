package it.alessandro.lazzari.googleshoppinglistwebapp;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class ListenerPageFinished {
    public interface IListenerPageFinished {
        public void onLoadPageFinished(String html);
    }

    private static final String LOGCAT = ListenerPageFinished.class.getSimpleName();

    private IListenerPageFinished mListenerPageFinished;

    public ListenerPageFinished(IListenerPageFinished listenerPageFinished) {
        mListenerPageFinished = listenerPageFinished;
    }

    @JavascriptInterface
    public void processHTML(String html) {
        Log.i(LOGCAT, html);

        if (mListenerPageFinished != null)
            mListenerPageFinished.onLoadPageFinished(html);
    }
}