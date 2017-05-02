package com.ultra.rianews2.Parsers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.ultra.rianews2.App;
import com.ultra.rianews2.Callbacks.OnLoadNewsfeedCallback;
import com.ultra.rianews2.R;
import com.ultra.rianews2.Units.Category;
import com.ultra.rianews2.Units.CategoryDao;
import com.ultra.rianews2.Units.DaoSession;
import com.ultra.rianews2.Units.NewsPreview;
import com.ultra.rianews2.Utils.CacheManager;
import com.ultra.rianews2.Utils.O;
import com.ultra.rianews2.Utils.Toaster;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(30.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class NewsfeedParser extends AsyncTask<Void,Void,ArrayList<NewsPreview> >
	 {
	 private ProgressDialog dialog;
	 private Context context;
	 private OnLoadNewsfeedCallback callback;
	 private String pageSrc;
	 private Document docDOM;

	 public NewsfeedParser(Context _context,String _pageSrc,OnLoadNewsfeedCallback _callback)
		 {
		 context=_context;
		 callback=_callback;
		 pageSrc=_pageSrc;
		 }
	 private void fillBasicURLparams(HttpURLConnection urlConnn)
		 {
		 urlConnn.setRequestProperty("Connection","keep-alive");
		 urlConnn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		 urlConnn.setRequestProperty("User-Agent","bot");
		 urlConnn.setRequestProperty("Accept-Language","ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
		 urlConnn.setDoOutput(true);
		 urlConnn.setUseCaches(false);
		 urlConnn.setInstanceFollowRedirects(false);
		 }
	 private String getHtmlString() throws IOException
		 {
		 String result;
		 URL url;
		 try
			 {
			 url=new URL(pageSrc);
			 HttpURLConnection urlConnn=(HttpURLConnection) url.openConnection();
			 fillBasicURLparams(urlConnn);
			 urlConnn.setConnectTimeout( (int) O.date.MINUTE_MILLIS/4);
			 urlConnn.setRequestMethod("GET");
			 String charset="UTF-8";
			 BufferedReader htmlIn=new BufferedReader(new InputStreamReader(urlConnn.getInputStream(),charset) );
			 StringBuilder sb=new StringBuilder();
			 String line;
			 while( (line=htmlIn.readLine() ) != null)
				 sb.append(line+"\n");
			 result=sb.toString();
			 htmlIn.close();
			 urlConnn.disconnect();
			 }
		 catch(IOException err)
			 {
			 Log.d(O.TAG,"getHtmlString: fail");
			 throw err;
			 }
		 return result;
		 }
	 private void initDOM() throws IOException
		 {
		 docDOM= Jsoup.parse(getHtmlString() );
		 }

	 @Override
	 protected ArrayList<NewsPreview> doInBackground(Void... params)
		 {
		 ArrayList<NewsPreview> result= new ArrayList<>();
		 try
			 {
			 initDOM();
			 }
		 catch(IOException e)
			 {
			 Toaster.makeHomeToast(context,"Ошибка соединения");
			 return null;
			 }
		 NewsPreview preview;
		 Elements topElements= docDOM.getElementsByAttributeValue("class","b-rubric-top__main-news");
		 if(!topElements.isEmpty() )
			 {
			 Element top= topElements.first();
			 preview= new NewsPreview();
			 String href= O.web.HOST + top.getElementsByTag("a").first().attr("href");
			 String pic= top.getElementsByTag("img").first().attr("src");
			 String title= docDOM.getElementsByAttributeValue("class","b-rubric-top__main-news-title").first().text();
			 preview.setLink(href);
			 preview.setPic(pic);
			 CacheManager.storeWebSrcPic(preview.getPic(),R.dimen.newsfeed_item_w,R.dimen.newsfeed_item_h);
			 preview.setPic(CacheManager.getFilenameFromURL(preview.getPic() ) );
			 preview.setNewsStr(title);
			 result.add(preview);
			 }
		 Elements anotherTopElements= docDOM.getElementsByAttributeValue("class","b-themespec__main-news");
		 if(!anotherTopElements.isEmpty() )
			 {
			 Element top= anotherTopElements.first();
			 preview= new NewsPreview();
			 String href= O.web.HOST + top.getElementsByTag("a").first().attr("href");
			 String pic= top.getElementsByTag("img").first().attr("src");
			 String title= docDOM.getElementsByAttributeValue("class","b-themespec__main-news-title").first().text();
			 preview.setLink(href);
			 preview.setPic(pic);
			 CacheManager.storeWebSrcPic(preview.getPic(),R.dimen.newsfeed_item_w,R.dimen.newsfeed_item_h);
			 preview.setPic(CacheManager.getFilenameFromURL(preview.getPic() ) );
			 preview.setNewsStr(title);
			 result.add(preview);
			 }
		 Elements listElements= docDOM.getElementsByAttributeValue("class","b-list__item ");
		 for(Element element : listElements)
			 {
			 preview= new NewsPreview();
			 String href= O.web.HOST + element.getElementsByTag("a").first().attr("href");
			 String pic= element.getElementsByTag("img").first().attr("src");
			 String title= element.getElementsByAttributeValue("class","b-list__item-title").first().text();
			 preview.setLink(href);
			 preview.setPic(pic);
			 CacheManager.storeWebSrcPic(preview.getPic(),R.dimen.newsfeed_item_w,R.dimen.newsfeed_item_h);
			 preview.setPic(CacheManager.getFilenameFromURL(preview.getPic() ) );
			 preview.setNewsStr(title);
			 result.add(preview);
			 }
		 return result;
		 }
	 @Override
	 protected void onPreExecute()
		 {
		 dialog= new ProgressDialog(context);
		 dialog.setIndeterminate(true);
		 dialog.setMessage("Загрузка новостей...");
		 dialog.setCancelable(false);
		 dialog.show();
		 }
	 @Override
	 protected void onPostExecute(ArrayList<NewsPreview> result)
		 {
		 dialog.dismiss();
		 if(result!=null && !CacheManager.checkForNewsfeed(pageSrc) )
			 {
			 DaoSession session= App.session;
			 Category category= session.getCategoryDao().queryBuilder().where(CategoryDao.Properties.Link.eq(pageSrc) ).list().get(0);
			 for(NewsPreview preview : result)
				 {
				 category.getNewsPreviews().add(preview);
				 preview.setCategoryId(category.getId() );
				 session.getNewsPreviewDao().insert(preview);
				 }
			 session.getCategoryDao().update(category);
			 }
		 callback.useResult(result,pageSrc);
		 super.onPostExecute(result);
		 }
	 }
