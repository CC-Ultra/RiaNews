package com.ultra.rianews2.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.ultra.rianews2.App;
import com.ultra.rianews2.Units.*;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * <p></p>
 * <p><sub>(01.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class CacheManager
	 {
	 public static void storeCategories(TreeMap<String,String> categoriesMap)
		 {
		 for(TreeMap.Entry<String,String> x : categoriesMap.entrySet() )
			 {
			 Category category= new Category();
			 category.setLink(x.getValue() );
			 category.setTitle(x.getKey() );
			 App.session.getCategoryDao().insert(category);
			 }
		 }
	 public static boolean checkForCategories()
		 {
		 ArrayList<Category> categories= new ArrayList<>(App.session.getCategoryDao().queryBuilder().list() );
		 return categories.size()!=0;
		 }
	 public static TreeMap<String,String> getCategories()
		 {
		 TreeMap<String,String> result= new TreeMap<>();
		 ArrayList<Category> categories= new ArrayList<>(App.session.getCategoryDao().queryBuilder().list() );
		 for(Category category : categories)
			 result.put(category.getTitle(),category.getLink() );
		 return result;
		 }
	 public static boolean checkForNewsfeed(String link)
		 {
		 ArrayList<Category> categories= new ArrayList<>(App.session.getCategoryDao().queryBuilder().where(CategoryDao.Properties.Link.eq(link)).list() );
		 ArrayList<NewsPreview> newsfeed=null;
		 if(categories.size()!=0)
			 newsfeed= new ArrayList(categories.get(0).getNewsPreviews() );
		 return newsfeed!=null && newsfeed.size()!=0;
		 }
	 public static boolean checkForNews(String link)
		 {
		 ArrayList<News> news= new ArrayList<>(App.session.getNewsDao().queryBuilder().where(NewsDao.Properties.Link.eq(link)).list() );
		 return news.size()!=0;
		 }
	 public static ArrayList<NewsPreview> getNewsfeed(String link)
		 {
		 ArrayList<NewsPreview> result= new ArrayList<>();
		 Category category= App.session.getCategoryDao().queryBuilder().where(CategoryDao.Properties.Link.eq(link)).list().get(0);
		 result.addAll(category.getNewsPreviews() );
		 return result;
		 }
	 public static News getNews(String link)
		 {
		 ArrayList<News> news= new ArrayList<>(App.session.getNewsDao().queryBuilder().where(NewsDao.Properties.Link.eq(link)).list() );
		 return news.get(0);
		 }

	 /**
	  * Отсекает всю строку до последнего {@code '/'} включительно, оставляя только имя
	  * @return пустая строка, если на вход пришла пустая или {@code null}, или вырезанное из url имя файла
	  */
	 public static String getFilenameFromURL(String urlStr)
		 {
		 if(urlStr==null || urlStr.length()==0)
			 {
			 Log.d(O.TAG,"getFilenameFromURL: передано пустое имя файла для извлечения");
			 return "";
			 }
		 String result;
		 int startIndex= urlStr.lastIndexOf('/')+1;
		 result= urlStr.substring(startIndex);
		 return result;
		 }

	 /**
	  * Отсекает всю строку до последней {@code '.'} включительно, оставляя только расширение
	  * @return пустая строка, если на вход пришла пустая или {@code null}, или вырезанное из имени файла расширение
	  */
	 private static String getFileExtension(String filename)
		 {
		 if(filename==null || filename.length()==0)
			 {
			 Log.d(O.TAG,"getFileExtension: передано пустое имя файла для извлечения");
			 return "";
			 }
		 String result;
		 int startIndex= filename.lastIndexOf('.')+1;
		 result= filename.substring(startIndex).toLowerCase();
		 return result;
		 }

	 /**
	  * получаю папку, делаю из нее полный путь к файлу
	  * @return строка, которую можно использовать для {@link ImageView#setImageURI}
	  */
	 public static String getStoredPicURI(String filename)
		 {
		 if(filename==null || filename.length()==0)
			 {
			 Log.d(O.TAG,"getStoredPicURI: передано пустое имя файла для извлечения");
			 return "";
			 }
		 String result;
		 File dir;
		 if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
			 dir= App.context.getExternalCacheDir();
		 else
			 dir= App.context.getCacheDir();
		 if(dir==null)
			 {
			 Log.d(O.TAG,"getStoredPicURI: путь к папке кэша не найден");
			 return "";
			 }
		 result= dir.getAbsolutePath() +"/"+ filename;
		 if( !(new File(result) ).exists() )
			 {
			 Log.d(O.TAG,"Файл "+ filename +" не найден");
			 result="";
			 }
		 return result;
		 }

	 /**
	  * Создаю объект {@link Picasso}, говорю ему грузить картинку (и грузится оно, очевидно, асинхронно), устанавливаю размеры
	  * и центрирование, и в конечном итоге делаю {@link RequestCreator#get()}. Тут можно подождать, пусть грузится, все равно
	  * работа ведется асинхронно. Потом получаю имя файла, расположение папки, полный путь и создаю {@link OutputStream}.
	  * В зависимости от расширения с помощью этого потока делаю {@link Bitmap#compress} и закрываю поток, сохраняя файл.
	  * @param picSrc ссылка на картинку
	  * @param widthResDimen ширина сохраняемой
	  * @param heightResDimen высота сохраняемой
	  */
	 public static void storeWebSrcPic(String picSrc, int widthResDimen, int heightResDimen)
		 {
		 if(picSrc==null || picSrc.length()==0)
			 {
			 Log.d(O.TAG,"Нечего сохранять, пустая ссылка");
			 return;
			 }
		 Picasso pablo= Picasso.with(App.context);
		 RequestCreator picControl= pablo.load(picSrc);
		 picControl.resizeDimen(widthResDimen,heightResDimen);
		 picControl.centerInside();
		 Bitmap bitmap;
		 try
			 {
			 bitmap= picControl.get();
			 }
		 catch(IOException pabloIO)
			 {
			 Log.d(O.TAG,"storeWebSrcPic: Picasso.get exception");
			 return;
			 }
		 File dir;
		 String filepath="";
		 String filename= getFilenameFromURL(picSrc);
		 OutputStream fileOut;
		 try
			 {
			 if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) )
				 dir= App.context.getExternalCacheDir();
			 else
				 dir= App.context.getCacheDir();
			 if(dir==null)
				 {
				 Log.d(O.TAG,"storeWebSrcPic: путь к папке кэша не найден");
				 return;
				 }
			 filepath= dir.getAbsolutePath() +"/"+ filename;
			 fileOut= new FileOutputStream(filepath);
			 }
		 catch(FileNotFoundException fileErr)
			 {
			 Log.d(O.TAG,"FileNotFoundException для "+ filepath);
			 return;
			 }
		 catch(Exception unknown)
			 {
			 Log.d(O.TAG,"onBitmapLoaded: неведомая ошибка\n");
			 unknown.printStackTrace();
			 return;
			 }
		 switch(getFileExtension(filename) )
			 {
			 case "png":
				 bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOut);
				 break;
			 case "jpg":
				 bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
				 break;
			 case "webp":
				 bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fileOut);
			 }
		 try
			 {
			 fileOut.close();
			 }
		 catch(IOException closeErr)
			 {
			 Log.d(O.TAG,"Не могу закрыть файл "+ filename +" после записи. Файл не сохранен");
			 }
		 }
	 }
