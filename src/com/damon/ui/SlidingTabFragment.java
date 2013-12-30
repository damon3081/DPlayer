package com.damon.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ab.view.sliding.AbSlidingTabView;
import com.damon.R;


public class SlidingTabFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sliding_tab, null);
		AbSlidingTabView mAbSlidingTabView = (AbSlidingTabView)view.findViewById(R.id.mAbSlidingTabView);
		
		//如果里面的页面列表不能下载原因：
		//Fragment里面用的AbTaskQueue,由于有多个tab，顺序下载有延迟，还没下载好就被缓存了。改成用AbTaskPool，就ok了。
		//或者setOffscreenPageLimit(0)
		
		//缓存数量
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(5);
		
		//禁止滑动
		/*mAbSlidingTabView.getViewPager().setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
			
		});*/

		Fragment1 page1 = new Fragment1();
		FragmentOnline page2 = new FragmentOnline();
		Fragment4 page3 = new Fragment4();
		Fragment5 page4 = new Fragment5();
		
		
		List<Fragment> mFragments = new ArrayList<Fragment>();
		mFragments.add(page1);
		mFragments.add(page2);
		mFragments.add(page3);
		mFragments.add(page4);
		
		List<String> tabTexts = new ArrayList<String>();
		tabTexts.add("本地视频");
		tabTexts.add("在线视频");
		tabTexts.add("音乐");
		tabTexts.add("书籍");
		//设置样式
		mAbSlidingTabView.setTabTextColor(Color.BLACK);
		mAbSlidingTabView.setTabSelectColor(Color.rgb(30, 168, 131));
		mAbSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
		mAbSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);
		//演示增加一组
		mAbSlidingTabView.addItemViews(tabTexts, mFragments);
		
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

