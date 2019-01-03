package it.alessandro.lazzari.googleshoppinglistwebapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.delight.android.webview.AdvancedWebView;

import static it.alessandro.lazzari.googleshoppinglistwebapp.Constants.SHOPPING_LIST_PREFS;
import static it.alessandro.lazzari.googleshoppinglistwebapp.Constants.SHOPPING_LIST_PREFS_WELCOME_MESSAGE;

public class MainActivity extends AppCompatActivity
        implements AdvancedWebView.Listener, ListenerPageFinished.IListenerPageFinished, SwipeRefreshLayout.OnRefreshListener {

    private static final String LOGCAT = MainActivity.class.getSimpleName();

    @BindView(R.id.webview)
    AdvancedWebView mWebView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.imageViewRefresh)
    ImageView mImageViewRefresh;
    @BindView(R.id.linearLayoutOffline)
    LinearLayout mLinearLayoutOffline;
    @BindView(R.id.linearLayoutRefresh)
    LinearLayout mLinearLayoutRefresh;

    private ListenerPageFinished mListenerPageFinished;
    private Boolean mIsOnline = Boolean.TRUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mListenerPageFinished = new ListenerPageFinished(this);

        mWebView.setListener(this, this);
        mWebView.addJavascriptInterface(mListenerPageFinished, "HTMLOUT");
        mWebView.setGeolocationEnabled(Boolean.TRUE);

        final SharedPreferences prefs = getSharedPreferences(SHOPPING_LIST_PREFS, MODE_PRIVATE);
        if (!prefs.getBoolean(SHOPPING_LIST_PREFS_WELCOME_MESSAGE, Boolean.FALSE)) {
            new MaterialDialog.Builder(this)
                    .title(R.string.information)
                    .content(R.string.message_welcome)
                    .positiveText(android.R.string.ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            loadPageShoppingList();
                            prefs.edit().putBoolean(SHOPPING_LIST_PREFS_WELCOME_MESSAGE, Boolean.TRUE).commit();
                            dialog.dismiss();
                        }
                    })
                    .autoDismiss(Boolean.FALSE)
                    .show();
        } else
            loadPageShoppingList();

    }

    @Override
    protected void onStart() {
        super.onStart();
        InternetCheck.Consumer consumer = new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                mIsOnline = internet;
            }
        };
        new InternetCheck(consumer);
    }

    public void loadPageShoppingList() {
        mWebView.loadUrl("https://shoppinglist.google.com/", Boolean.TRUE);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        mLinearLayoutRefresh.setVisibility(View.VISIBLE);
        mLinearLayoutOffline.setVisibility(View.GONE);

        mWebView.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(Boolean.TRUE);
    }

    @Override
    public void onPageFinished(String url) {
        mLinearLayoutRefresh.setVisibility(View.GONE);
        mLinearLayoutOffline.setVisibility(!mIsOnline ? View.VISIBLE : View.GONE);

        mWebView.setVisibility(mIsOnline ? View.VISIBLE : View.GONE);

        mSwipeRefreshLayout.setRefreshing(Boolean.FALSE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        mLinearLayoutOffline.setVisibility(View.VISIBLE);

        mLinearLayoutRefresh.setVisibility(View.GONE);
        mWebView.setVisibility(View.GONE);

        mSwipeRefreshLayout.setRefreshing(Boolean.FALSE);
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

    @Override
    public void onRefresh() {
        mWebView.reload();
    }
}
