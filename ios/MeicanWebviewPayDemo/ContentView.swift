//
//  ContentView.swift
//  MeicanWebviewPayDemo
//
//  Created by Ding Yi on 2021/8/9.
//

import SwiftUI
import WebKit

struct ContentView: View {
    var body: some View {
        Webview(url: URL(string: "https://meican.com")!)
    }
}

struct Webview: UIViewRepresentable {
    let url: URL
    let navigationHelper = WebViewHelper()

    func makeUIView(context: UIViewRepresentableContext<Webview>) -> WKWebView {
        let webview = WKWebView()
        webview.navigationDelegate = navigationHelper

        let request = URLRequest(url: self.url, cachePolicy: .returnCacheDataElseLoad)
        webview.load(request)

        return webview
    }

    func updateUIView(_ webview: WKWebView, context: UIViewRepresentableContext<Webview>) {
        let request = URLRequest(url: self.url, cachePolicy: .returnCacheDataElseLoad)
        webview.load(request)
    }
}


class WebViewHelper: NSObject, WKNavigationDelegate {
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
}
