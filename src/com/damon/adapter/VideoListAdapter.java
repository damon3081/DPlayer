package com.damon.adapter;

import java.util.List;
import java.util.Map;
import com.damon.global.Constant;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VideoListAdapter extends BaseAdapter{
	
	private static String TAG = "VideoListAdapter";
	private static final boolean D = Constant.DEBUG;
  
	private Context mContext;
	//xml转View对象
    private LayoutInflater mInflater;
    //单行的布局
    private int mResource;
    //列表展现的数据
    private List mData;
    //Map中的key
    private String[] mFrom;
    //view的id
    private int[] mTo;
    
    
   /**
    * 构造方法
    * @param context
    * @param data 列表展现的数据
    * @param resource 单行的布局
    * @param from Map中的key
    * @param to view的id
    */
    public VideoListAdapter(Context context, List data,
            int resource, String[] from, int[] to){
    	this.mContext = context;
    	this.mData = data;
    	this.mResource = resource;
    	this.mFrom = from;
    	this.mTo = to;
        //用于将xml转为View
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }   
    
    @Override
    public int getCount() {
        return mData.size();
    }
    
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position){
      return position;
    }
    
    @SuppressLint("NewApi")
	@Override
    public View getView(int position, View convertView, ViewGroup parent){
    	  final ViewHolder holder;
          if(convertView == null){
	           //使用自定义的list_items作为Layout
	           convertView = mInflater.inflate(mResource, parent, false);
	           //减少findView的次数
			   holder = new ViewHolder();
	           //初始化布局中的元素
			   holder.itemsIcon = ((ImageView) convertView.findViewById(mTo[0]));
			   holder.itemsTitle = ((TextView) convertView.findViewById(mTo[1]));
			   holder.itemsTime = ((TextView) convertView.findViewById(mTo[2]));
			   holder.itemsSize = ((TextView) convertView.findViewById(mTo[3]));
			   convertView.setTag(holder);
          }else{
        	   holder = (ViewHolder) convertView.getTag();
          }
          
		  //获取该行的数据
          @SuppressWarnings("unchecked")
		final Map<String, Object>  obj = (Map<String, Object>)mData.get(position);
          holder.itemsTitle.setText((String)obj.get("itemsTitle"));
          holder.itemsTime.setText((String)obj.get("itemsTime"));
          holder.itemsSize.setText((String)obj.get("itemsSize"));
          Bitmap bm = (Bitmap)obj.get("itemsIcon");
          if(bm != null){
        	  holder.itemsIcon.setImageBitmap(bm);
          }
          
          return convertView;
    }
    
    /**
	 * View元素
	 */
	static class ViewHolder {
		ImageView itemsIcon;
		TextView itemsTitle;
		TextView itemsTime;
		TextView itemsSize;
	}

}
