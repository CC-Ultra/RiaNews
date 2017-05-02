package com.ultra.rianews2;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ultra.rianews2.Callbacks.AdapterCallback;
import com.ultra.rianews2.Units.Category;
import com.ultra.rianews2.Units.CategoryDao;
import com.ultra.rianews2.Units.NewsPreview;
import com.ultra.rianews2.Utils.CacheManager;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(01.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedAdapter.Holder>
	 {
	 private ArrayList<NewsPreview> previews;
	 private AdapterCallback adapterCallback;
	 private String categoryStr;

	 private class Listener implements View.OnClickListener
		 {
		 NewsPreview preview;

		 void setPreview(NewsPreview _preview)
			 {
			 preview=_preview;
			 }

		 @Override
		 public void onClick(View v)
			 {
			 adapterCallback.onClick(preview);
			 }
		 }
	 class Holder extends RecyclerView.ViewHolder
		 {
		 TextView news,theme;
		 ImageView img;
		 Listener listener;

		 public Holder(View itemView)
			 {
			 super(itemView);
			 listener= new Listener();
			 news= (TextView)itemView.findViewById(R.id.news);
			 theme= (TextView)itemView.findViewById(R.id.theme);
			 img= (ImageView)itemView.findViewById(R.id.img);
			 itemView.setOnClickListener(listener);
			 }
		 }

	 public NewsfeedAdapter(ArrayList<NewsPreview> _list,AdapterCallback callback,String _categoryStr)
		 {
		 categoryStr=_categoryStr;
		 previews=_list;
		 adapterCallback=callback;
		 }

	 @Override
	 public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		 {
		 View view= LayoutInflater.from(parent.getContext() ).inflate(R.layout.newsfeed_item,parent,false);
		 return new Holder(view);
		 }

	 @Override
	 public void onBindViewHolder(Holder holder,int position)
		 {
		 NewsPreview preview= previews.get(position);
		 String newsStr= preview.getNewsStr();
		 if(newsStr.length()>30)
			 newsStr= newsStr.substring(0,30) +"...";
		 holder.news.setText(newsStr);
		 holder.theme.setText(categoryStr);
		 holder.img.setImageURI(Uri.parse(CacheManager.getStoredPicURI(preview.getPic() ) ) );
		 holder.listener.setPreview(preview);
		 }

	 @Override
	 public int getItemCount()
		 {
		 return previews.size();
		 }
	 }
