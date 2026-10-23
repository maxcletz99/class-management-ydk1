package id.smkyadika.classmanagement;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.net.Uri;
import android.content.Intent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceError;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
    // PENTING: ganti URL ini dengan URL website Netlify milikmu sebelum build APK.
    private static final String WEBSITE_URL = "https://classmanagementbyyadika.netlify.app/";
    private static final int FILE_CHOOSER_REQUEST = 2401;
    private WebView webView;
    private ProgressBar progressBar;
    private ValueCallback<Uri[]> filePathCallback;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(android.graphics.Color.rgb(18, 34, 65));
        getWindow().setNavigationBarColor(android.graphics.Color.rgb(18, 34, 65));
        FrameLayout root = new FrameLayout(this);
        webView = new WebView(this);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        FrameLayout.LayoutParams progressParams = new FrameLayout.LayoutParams(-1, dp(3));
        root.addView(webView, new FrameLayout.LayoutParams(-1, -1));
        root.addView(progressBar, progressParams);
        setContentView(root);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setSupportMultipleWindows(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String scheme = uri.getScheme() == null ? "" : uri.getScheme();
                if (scheme.equals("https") && uri.getHost() != null && (uri.getHost().equals("classmanagementbyyadika.netlify.app") || uri.getHost().endsWith(".netlify.app"))) return false;
                if (scheme.equals("https") || scheme.equals("mailto") || scheme.equals("tel")) {
                    try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); } catch (Exception ignored) { }
                    return true;
                }
                return true;
            }
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) { progressBar.setVisibility(View.VISIBLE); }
            @Override public void onPageFinished(WebView view, String url) { progressBar.setVisibility(View.GONE); }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                progressBar.setVisibility(progress >= 100 ? View.GONE : View.VISIBLE);
            }
            @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (filePathCallback != null) filePathCallback.onReceiveValue(null);
                filePathCallback = callback;
                Intent intent;
                try { intent = params.createIntent(); startActivityForResult(intent, FILE_CHOOSER_REQUEST); }
                catch (Exception e) { filePathCallback = null; Toast.makeText(MainActivity.this, "Tidak bisa membuka pemilih file", Toast.LENGTH_SHORT).show(); return false; }
                return true;
            }
        });
        if (savedInstanceState != null) webView.restoreState(savedInstanceState);
        else if (WEBSITE_URL.startsWith("https://GANTI-")) {
            webView.loadDataWithBaseURL(null, "<meta name='viewport' content='width=device-width, initial-scale=1'><div style='font-family:sans-serif;padding:24px'><h2>Konfigurasi aplikasi belum selesai</h2><p>Buka file MainActivity.java dan ganti WEBSITE_URL dengan URL Netlify website Class Management Online, lalu build ulang APK.</p></div>", "text/html", "UTF-8", null);
        } else webView.loadUrl(WEBSITE_URL);
    }
    private int dp(int value) { return (int) (value * getResources().getDisplayMetrics().density); }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST && filePathCallback != null) {
            Uri[] results = WebChromeClient.FileChooserParams.parseResult(resultCode, data);
            filePathCallback.onReceiveValue(results);
            filePathCallback = null;
        }
    }
    @Override protected void onSaveInstanceState(Bundle outState) { super.onSaveInstanceState(outState); webView.saveState(outState); }
    @Override public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed();
    }
    @Override protected void onDestroy() {
        if (filePathCallback != null) { filePathCallback.onReceiveValue(null); filePathCallback = null; }
        if (webView != null) { webView.destroy(); webView = null; }
        super.onDestroy();
    }
}
