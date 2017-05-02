package com.ultra.rianews2.Parsers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.ultra.rianews2.App;
import com.ultra.rianews2.Callbacks.OnLoadNewsCallback;
import com.ultra.rianews2.Callbacks.OnLoadNewsfeedCallback;
import com.ultra.rianews2.R;
import com.ultra.rianews2.Units.*;
import com.ultra.rianews2.Utils.CacheManager;
import com.ultra.rianews2.Utils.O;
import com.ultra.rianews2.Utils.Toaster;
import org.greenrobot.greendao.query.QueryBuilder;
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

public class NewsParser extends AsyncTask<Void,Void,News>
	 {
	 private ProgressDialog dialog;
	 private Context context;
	 private OnLoadNewsCallback callback;
	 private String pageSrc;
	 private Document docDOM;
	 private String selectedCategory;

	 public NewsParser(Context _context,String _pageSrc,OnLoadNewsCallback _callback,String _selectedCategory)
		 {
		 selectedCategory=_selectedCategory;
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
	 protected News doInBackground(Void... params)
		 {
		 News result= new News();
		 try
			 {
			 initDOM();
			 }
		 catch(IOException e)
			 {
			 Toaster.makeHomeToast(context,"Ошибка соединения");
			 return null;
			 }
		 Element titleElement= docDOM.getElementsByAttributeValue("class","b-article__title").first();
		 String title= titleElement.text();
		 StringBuilder date= new StringBuilder();
		 Elements dateElements= docDOM.getElementsByAttributeValue("class","b-article__info-date").first().getAllElements();
		 date.append(dateElements.get(0).text().substring(5) );
		 date.append(" ");
		 date.append(dateElements.get(1).text() );
		 Element img= docDOM.getElementsByAttributeValue("class","l-photoView__open").first();
		 String picStr= img.attr("data-photoview_src");
		 StringBuilder txt= new StringBuilder();
		 Elements txtElements= docDOM.getElementsByAttributeValue("class","b-article__body js-mediator-article").get(0).getElementsByTag("p");
		 for(Element txtElement : txtElements)
			 {
			 txt.append(txtElement.text() );
			 txt.append("\n");
			 }
		 result.setDate(date.toString() );
		 result.setLink(pageSrc);
		 result.setNewsTxt(title);
		 result.setTxt(txt.toString() );
		 result.setPic(picStr);
		 result.setTheme(selectedCategory);
		 CacheManager.storeWebSrcPic(result.getPic(),R.dimen.newspic_w,R.dimen.newspic_h);
		 result.setPic(CacheManager.getFilenameFromURL(result.getPic() ) );
		 return result;
		 }
	 @Override
	 protected void onPreExecute()
		 {
		 dialog= new ProgressDialog(context);
		 dialog.setIndeterminate(true);
		 dialog.setMessage("Загрузка новости...");
		 dialog.setCancelable(false);
		 dialog.show();
		 }
	 @Override
	 protected void onPostExecute(News result)
		 {
		 dialog.dismiss();
		 if(result!=null)
			 {
			 if(!CacheManager.checkForNews(pageSrc) )
				 App.session.getNewsDao().insert(result);
			 else
			 	result= App.session.getNewsDao().queryBuilder().where(NewsDao.Properties.Link.eq(pageSrc) ).list().get(0);
			 }
		 callback.jumpToNewsActivity(result,pageSrc);
		 super.onPostExecute(result);
		 }
	 }
