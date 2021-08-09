WebView App Pay Demo
====================

> 此 Demo 用于展示如何在 Android/iOS WebView 中使用微信和支付宝 App 进行支付。

## Android

注意 `MainActivity.java` 中关于 `WebViewClient` 的设置，我们需要重载函数 [`shouldOverrideUrlLoading`](https://github.com/meican-dev/webview-app-pay-demo/blob/f9bd26882c3ffd01f0b8324dee26e2f3bb31adc2/app/src/main/java/com/miaonster/webviewwechatpay/MainActivity.java#L31-L58)，以拦截微信和支付宝唤起 App 的链接，这些链接以 `weixin://` 和 `alipays://` 开头。匹配到这些链接之后，尝试唤起对应的 App 就可以进行支付。

```java
webView.setWebViewClient(new WebViewClient() {
    @Override
    public void onLoadResource(WebView view, String url) {
        Log.i("onLoadResource", url);
        super.onLoadResource(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String s = request.getUrl().toString();

        Log.i("shouldOverrideUrlLoading", s);

        try {
            if (s.startsWith("weixin://wap/pay?")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(s));
                startActivity(intent);
                return true;
            }
            if (s.startsWith("alipays://platformapi/startApp?")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(s));
                startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Cannot open Wechat / Alipay");
            alert.show();
        }

        return super.shouldOverrideUrlLoading(view, request);
    }
});
```

## iOS

注意 `ios/MeicanWebviewPayDemo/ContentView.swift` 中对于 `webView` 的重载，我们需要处理微信和支付宝的跳转链接

```swift
func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
    let urlString = navigationAction.request.url?.absoluteString
    if urlString?.range(of: "weixin://wap/pay?") != nil || urlString?.range(of: "alipays://") != nil ||  urlString?.range(of: "alipay://") != nil {
        decisionHandler(.cancel)
        if let mUrlStr = urlString, let openUrl = URL(string: mUrlStr) {
            UIApplication.shared.open(openUrl, options: [:], completionHandler: nil)
        }
        return
    }
    decisionHandler(.allow)
    return
}
```
