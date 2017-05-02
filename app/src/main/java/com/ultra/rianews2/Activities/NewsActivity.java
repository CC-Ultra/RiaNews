package com.ultra.rianews2.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ultra.rianews2.App;
import com.ultra.rianews2.R;
import com.ultra.rianews2.Units.News;
import com.ultra.rianews2.Utils.CacheManager;
import com.ultra.rianews2.Utils.O;

/**
 * <p></p>
 * <p><sub>(02.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class NewsActivity extends AppCompatActivity
	 {
	 @Override
	 protected void onCreate(@Nullable Bundle savedInstanceState)
		 {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.news_layout);

		 long newsId= getIntent().getLongExtra(O.extra.ID,0);
		 News news= App.session.getNewsDao().load(newsId);
		 if(news==null)
			 {
			 Toast.makeText(this,"news==null\nid="+ newsId,Toast.LENGTH_SHORT).show();
			 finish();
			 return;
			 }

		 TextView txt= (TextView)findViewById(R.id.txt);
		 TextView newsTxt= (TextView)findViewById(R.id.news);
		 TextView theme= (TextView)findViewById(R.id.theme);
		 TextView date= (TextView)findViewById(R.id.date);
		 ImageView img= (ImageView)findViewById(R.id.img);
		 ImageView gradient= (ImageView)findViewById(R.id.gradient);

//		 gradient.setImageDrawable(ScrimUtil.makeCubicGradientScrimDrawable() );
		 txt.setText(news.getTxt() );
		 date.setText(news.getDate() );
		 theme.setText(news.getTheme() );
		 img.setImageURI(Uri.parse(CacheManager.getStoredPicURI(news.getPic() ) ) );
		 String shortNewsStr=news.getNewsTxt();
		 if(shortNewsStr.length()>30)
			 shortNewsStr= shortNewsStr.substring(0,30) +"...";
		 newsTxt.setText(shortNewsStr);
		 }
	 }
