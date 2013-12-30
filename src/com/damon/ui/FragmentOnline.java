package com.damon.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.damon.R;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;



@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentOnline extends Fragment  {
	private WebView mWebView;
	private ListView mListView;
	/** 网页正在加载 */
	private View mLoading;
	/** 历史记录 */
	private List<String> mHistory = new ArrayList<String>();
	/** 显示当前正在加载的url */
	private TextView mUrl;
	private String mTitle;
	private ProgressBar mProgressBar = null;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.fragment_online, container,
				false);
		
		mWebView = (WebView) mView.findViewById(R.id.webview);
		mUrl = (TextView) mView.findViewById(R.id.url);
		mLoading = mView.findViewById(R.id.loading);
		mProgressBar = (ProgressBar) mView.findViewById(R.id.progressBar);
		
		initWebView();
		
		return mView;
	}
	
	
	
	
	/** 初始化WebView */
	private void initWebView() {
		mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginsEnabled(true);
		mWebView.loadUrl("http://3g.youku.com");
		mWebView.setWebViewClient(new WebViewClient() {

			/** 页面开始加载 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mUrl.setText(url);
				mUrl.setVisibility(View.VISIBLE);
			}
         
			
			/** 页面加载完成 */
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mLoading.setVisibility(View.GONE);
				mWebView.setVisibility(View.VISIBLE);
				if (!mHistory.contains(url))
					mHistory.add(0, url);
				mUrl.setVisibility(View.GONE);
				// 取得title
				mTitle = view.getTitle();
			};
			

			/** 页面跳转 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view,
					final String url) {
				return false;
			};
		});

		/** 处理后退键 */
		mWebView.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView != null
						&& mWebView.canGoBack()) {
					if (mHistory.size() > 1) {
						mHistory.remove(0);
						mWebView.loadUrl(mHistory.get(0));
						return true;
					}
				}
				return false;
			}
		});
		
		mWebView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if (newProgress < 100)
            	{
            		mProgressBar.setVisibility(View.VISIBLE);
            		mProgressBar.setProgress(newProgress);
            	}
            	else
            	{
            		mProgressBar.setVisibility(View.GONE);
            	}
			}
			
		});
	}
	
	
	
	
	// ~~~~~~~~~~~~~处理FLASH退出的问题 ~~~~~~~~

		private void callHiddenWebViewMethod(String name) {
			if (mWebView != null) {
				try {
					Method method = WebView.class.getMethod(name);
					method.invoke(mWebView);
				} catch (NoSuchMethodException e) {
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				}
			}
		}
		
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
		}

		
		
		
}
