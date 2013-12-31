
package com.damon.ui;



import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.progress.AbCircleProgressBar;
import com.ab.view.pullview.AbListViewFooter;
import com.ab.view.pullview.AbPullListView;
import com.damon.R;
import com.damon.adapter.VideoListAdapter;
import com.damon.database.SQLiteHelper;
import com.damon.model.Video;
import com.damon.model.VideoProvider;


public class FragmentLocalVideo extends Fragment {
	
	private Activity mActivity = null;
	private List<Map<String, Object>> list = null;
	private List<Map<String, Object>> newList = null;
	private AbPullListView mAbPullListView = null;
	private AbTaskQueue mAbTaskQueue = null;
	private VideoListAdapter myListViewAdapter = null;
	private ProgressBar hProgressBar;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		 mActivity = this.getActivity();
		 
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
    	 myListViewAdapter = new VideoListAdapter(mActivity, list,R.layout.fragment_file_item,
				new String[] { "itemsIcon", "itemsTitle","itemsTime","itemsSize" }, new int[] { R.id.thumbnail,
						R.id.title,R.id.time,R.id.file_size });
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

			@SuppressLint("NewApi")
			@Override
			public void get() {
	   		    try {
	   		    	List<Video> videoList = new VideoProvider(mActivity.getApplicationContext()).getList();
	   		    	newList = new ArrayList<Map<String, Object>>();
	   		    	Map<String, Object> map = null;
	   				SQLiteHelper sqLiteHelper=new SQLiteHelper(mActivity.getApplicationContext());
	   				SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
	   		    	for (Video video : videoList) {
	   		    		if(video.getSize() == 0){
		   					continue;
		   				}
		   		    	long position = 0;
	   		    		map = new HashMap<String, Object>();
	   					//map.put("itemsIcon",mPhotoList.get(new Random().nextInt(mPhotoList.size())));
		   		    	map.put("itemsTitle", video.getTitle());		   		    	
		   		    	map.put("itemsSize", video.getSize()/1024/1024+"M");
		   		    	map.put("itemsPath", video.getPath());		   		    	
		   				Cursor c = db.rawQuery("select * from files where path=? ", new String[]{video.getPath()});
		   				if(c.moveToNext()){
		   					position=c.getLong(2);
		   				}
		   				c.close();
		   				map.put("itemsTime", stringForTime(position)+"/"+stringForTime(video.getDuration()));
		   				Bitmap bm = ThumbnailUtils.createVideoThumbnail(video.getPath(),MediaStore.Images.Thumbnails.MICRO_KIND);
		   				map.put("itemsIcon", bm);
		   		    	newList.add(map);
					}
	   				db.close();
	   		    	} 
	   		    	catch (Exception e) {

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
	
	private String stringForTime(long timeMs) {
		long totalSeconds = timeMs / 1000;

		long seconds = totalSeconds % 60;
		long minutes = (totalSeconds / 60) % 60;
		long hours   = totalSeconds / 3600;
        StringBuilder mFormatBuilder=new StringBuilder();
        Formatter mFormatter=new Formatter(mFormatBuilder, Locale.getDefault());;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}

