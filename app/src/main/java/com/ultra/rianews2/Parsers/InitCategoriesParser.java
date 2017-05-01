package com.ultra.rianews2.Parsers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.ultra.rianews2.Utils.O;
import com.ultra.rianews2.Callbacks.OnInitCategoriesCallback;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;

/**
 * <p></p>
 * <p><sub>(30.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class InitCategoriesParser extends AsyncTask<Void,Void,TreeMap<String,String> >
	 {
	 private ProgressDialog dialog;
	 private Context context;
	 private OnInitCategoriesCallback callback;
	 private String pageSrc;
	 private Document docDOM;

	 public InitCategoriesParser(Context _context,OnInitCategoriesCallback _callback,String _pageSrc)
		 {
		 context=_context;
		 callback=_callback;
		 pageSrc=_pageSrc;
		 }

	 protected void fillBasicURLparams(HttpURLConnection urlConnn)
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
	 protected void initDOM() throws IOException
		 {
		 docDOM= Jsoup.parse(getHtmlString() );
		 }

	 @Override
	 protected TreeMap<String,String> doInBackground(Void... params)
		 {
		 TreeMap<String,String> result= new TreeMap<>();
		 try
			 {
			 initDOM();
			 }
		 catch(IOException e)
			 {
			 Toast.makeText(context,"Ошибка соединения",Toast.LENGTH_LONG);
			 return null;
			 }
		 Elements lists= docDOM.getElementsByAttributeValue("class","b-footer__column");
		 for(int i=0; i<2; i++)
			 {
			 Element list= lists.get(i);
			 for(Element element : list.getElementsByTag("a") )
				 {
				 String key= element.text();
				 String hrefAttr=element.attr("href");
				 String value;
				 if(hrefAttr.contains("http") )
				 	value=hrefAttr;
				 else
					 {
					 String hostWithoutLastSlash= O.web.HOST.substring(0,O.web.HOST.length()-1);
					 if(hrefAttr.equals("/") )
						 hrefAttr="/lenta/";
					 value= hostWithoutLastSlash + hrefAttr;
					 }
				 result.put(key,value);
				 }
			 }
		 return result;
		 }
	 @Override
	 protected void onPreExecute()
		 {
		 dialog= new ProgressDialog(context);
		 dialog.setIndeterminate(true);
		 dialog.setMessage("Инициализация...");
		 dialog.setCancelable(false);
		 dialog.show();
		 }
	 @Override
	 protected void onPostExecute(TreeMap<String,String> result)
		 {
		 dialog.dismiss();
		 callback.useParserResult(result);
		 super.onPostExecute(result);
		 }
	 }
