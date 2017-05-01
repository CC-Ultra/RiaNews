package com.ultra.rianews2;

import android.app.Application;
import android.content.Context;
import com.ultra.rianews2.Units.DaoMaster;
import com.ultra.rianews2.Units.DaoSession;
import org.greenrobot.greendao.database.Database;

/**
 * <p></p>
 * <p><sub>(01.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class App extends Application
	 {
	 public static DaoSession session;
	 public static Context context;

	 @Override
	 public void onCreate()
		 {
		 super.onCreate();
		 DaoMaster.DevOpenHelper helper= new DaoMaster.DevOpenHelper(this,"RiaNews.db");
		 Database db= helper.getWritableDb();
		 session= new DaoMaster(db).newSession();
		 context=this;
		 }
	 }
