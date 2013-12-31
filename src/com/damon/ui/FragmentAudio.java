
package com.damon.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.damon.R;
import com.damon.adapter.AudioListAdapter;
import com.damon.model.Audio;
import com.damon.model.AudioProvider;


public class FragmentAudio extends Fragment {
	
	private Activity mActivity = null;
	private List<Map<String, Object>> list = null;
	private List<Map<String, Object>> newList = null;
	private AbPullListView mAbPullListView = null;
	private AbTaskQueue mAbTaskQueue = null;
	private AudioListAdapter myListViewAdapter = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		 mActivity = this.getActivity();
		 //application = (MyApplication) mActivity.getApplication();
		 
		 View view = inflater.inflate(R.layout.pull_list, null);
		
		 mAbTaskQueue = AbTaskQueue.getInstance();
	     //获取ListView对象
         mAbPullListView = (AbPullListView)view.findViewById(R.id.mListView);
         //设置进度条的样式
         mAbPullListView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
         mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
         //ListView数据
    	 list = new ArrayList<Map<String, Object>>();
    	
    	 //使用自定义的Adapter
    	 myListViewAdapter = new AudioListAdapter(mActivity, list,R.layout.list_items,
				new String[] { "itemsPath", "itemsTitle","itemsText" }, new int[] { R.id.itemsIcon,
						R.id.itemsTitle,R.id.itemsText });
    	 mAbPullListView.setAdapter(myListViewAdapter);
    	 //item被点击事件
    	 mAbPullListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map<String, Object> map = list.get(position-1);
				Intent intent = new Intent(mActivity.getApplicationContext(), VideoPlayer.class);
				intent.putExtra("path", map.get("itemsPath").toString());
				intent.putExtra("title", map.get("itemsTitle").toString());
				startActivity(intent);
			}
    	 });
    	 mAbPullListView.showUpdateHeaderHeight();
		 return view;
	} 
	
	

	@Override
	public void onStart() {
		super.onStart();
		//定义两种查询的事件
    	final AbTaskItem item1 = new AbTaskItem();
		item1.listener = new AbTaskListener() {

			@Override
			public void update() {
				list.clear();
				if(newList!=null && newList.size()>0){
	                list.addAll(newList);
	                myListViewAdapter.notifyDataSetChanged();
	                newList.clear();
   		    	}
				mAbPullListView.stopRefresh();
			}

			@Override
			public void get() {
	   		    try {
	   		    	
	   		    	newList = new ArrayList<Map<String, Object>>();
	   		    	Map<String, Object> map = null;
	   		    	List<Audio> audoList = new AudioProvider(mActivity.getApplicationContext()).getList();
	   		    	for (Audio audio : audoList) {
	   		    		map = new HashMap<String, Object>();
	   					map.put("itemsPath",audio.getPath());
		   		    	map.put("itemsTitle", audio.getTitle());
		   		    	map.put("itemsText", audio.getArtist());
		   		    	newList.add(map);
					}
	   		    	
	   		    } catch (Exception e) {
	   		    }
		  };
		};
		
		
		
		mAbPullListView.setAbOnListViewListener(new AbOnListViewListener(){

			@Override
			public void onRefresh() {
				mAbTaskQueue.execute(item1);
			}

			@Override
			public void onLoadMore() {
				mAbTaskQueue.execute(item1);
			}
			
		});
		
    	//第一次下载数据
		mAbTaskQueue.execute(item1);
	}



	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

