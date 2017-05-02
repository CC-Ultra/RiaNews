package com.ultra.rianews2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.SubMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.ultra.rianews2.Callbacks.AdapterCallback;
import com.ultra.rianews2.Callbacks.OnInitCategoriesCallback;
import com.ultra.rianews2.Callbacks.OnLoadNewsCallback;
import com.ultra.rianews2.Callbacks.OnLoadNewsfeedCallback;
import com.ultra.rianews2.NewsfeedAdapter;
import com.ultra.rianews2.Parsers.InitCategoriesParser;
import com.ultra.rianews2.Parsers.NewsParser;
import com.ultra.rianews2.Parsers.NewsfeedParser;
import com.ultra.rianews2.R;
import com.ultra.rianews2.Units.News;
import com.ultra.rianews2.Units.NewsPreview;
import com.ultra.rianews2.Utils.CacheManager;
import com.ultra.rianews2.Utils.O;
import java.util.ArrayList;
import java.util.TreeMap;

public class NewsfeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
		OnInitCategoriesCallback,OnLoadNewsfeedCallback,AdapterCallback,OnLoadNewsCallback
	 {
	 private NavigationView navigationView;
	 private TreeMap<String,String> categoriesMap;
	 private RecyclerView recyclerList;
	 private String selectedCategory="Главное";

	 private void initNewsfeed(String link)
		 {
		 NewsfeedParser parser= new NewsfeedParser(this,link,this);
		 parser.execute();
		 }
	 private void initDrawerMenu()
		 {
		 Menu menu = navigationView.getMenu();
		 SubMenu categories = menu.addSubMenu("Категории");
		 int i=0;
		 for(TreeMap.Entry<String,String> x : categoriesMap.entrySet() )
			 {
			 categories.add(0,i,0,x.getKey() );
			 i++;
			 }

		 MenuItem menuItem= menu.getItem(menu.size()-1);
		 menuItem.setTitle(menuItem.getTitle());
		 }
	 @Override
	 public void onClick(NewsPreview preview)
		 {
		 NewsParser parser= new NewsParser(this,preview.getLink(),this,selectedCategory);
		 parser.execute();
		 }
	 @Override
	 public void useParserResult(TreeMap<String,String> result)
		 {
		 categoriesMap=result;
		 if(categoriesMap==null)
			 {
			 if(!CacheManager.checkForCategories() )
				 {
				 finish();
				 Toast.makeText(this,"Категории не были получены",Toast.LENGTH_SHORT).show();
				 return;
				 }
			 else
			 	categoriesMap= CacheManager.getCategories();
			 }
		 initDrawerMenu();
		 initNewsfeed(O.web.HOME);
		 }
	 @Override
	 public void useResult(ArrayList<NewsPreview> result,String link)
		 {
		 if(result==null || result.size()==0)
			 {
			 if(CacheManager.checkForNewsfeed(link) )
				 result= CacheManager.getNewsfeed(link);
			 else
				 {
				 Toast.makeText(this,"В разделе пусто",Toast.LENGTH_SHORT).show();
				 return;
				 }
			 }
		 NewsfeedAdapter adapter= new NewsfeedAdapter(result,this,selectedCategory);
		 recyclerList.setLayoutManager(new LinearLayoutManager(this) );
		 recyclerList.setAdapter(adapter);
		 }
	 @Override
	 public void jumpToNewsActivity(News news,String link)
		 {
		 if(news==null)
			 {
			 if(CacheManager.checkForNews(link) )
				 news= CacheManager.getNews(link);
			 else
				 {
				 Toast.makeText(this,"Не удалось получить новость",Toast.LENGTH_SHORT).show();
				 return;
				 }
			 }
		 Intent jumper=new Intent(this,NewsActivity.class);
		 jumper.putExtra(O.extra.ID,news.getId() );
		 startActivity(jumper);
		 }

	 @Override
	 protected void onCreate(Bundle savedInstanceState)
		 {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.main_layout);

		 Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
		 DrawerLayout drawer= (DrawerLayout)findViewById(R.id.drawer_layout);
		 navigationView= (NavigationView)findViewById(R.id.nav_view);
		 recyclerList= (RecyclerView)findViewById(R.id.mainList);

		 setSupportActionBar(toolbar);
		 ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
		 drawer.addDrawerListener(toggle);
		 toggle.syncState();
		 navigationView.setNavigationItemSelectedListener(this);

		 InitCategoriesParser parser= new InitCategoriesParser(this,O.web.HOST,this);
		 parser.execute();
		 }
	 @Override
	 public void onBackPressed()
		{
		 DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
		 if(drawer.isDrawerOpen(GravityCompat.START))
			 drawer.closeDrawer(GravityCompat.START);
		 else
			 super.onBackPressed();
		 }
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu)
		{
		 getMenuInflater().inflate(R.menu.main,menu);
		 return true;
		 }
	 @SuppressWarnings("StatementWithEmptyBody")
	 @Override
	 public boolean onNavigationItemSelected(MenuItem item)
		 {
		 selectedCategory= item.getTitle().toString();
		 initNewsfeed(categoriesMap.get(item.getTitle() ) );
		 DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
		 drawer.closeDrawer(GravityCompat.START);
		 return true;
		 }
	 }
