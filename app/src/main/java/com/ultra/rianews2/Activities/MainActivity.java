package com.ultra.rianews2.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.ultra.rianews2.Callbacks.OnInitCategoriesCallback;
import com.ultra.rianews2.Parsers.InitCategoriesParser;
import com.ultra.rianews2.R;
import com.ultra.rianews2.Utils.O;

import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnInitCategoriesCallback
	 {
	 private NavigationView navigationView;
	 private TreeMap<String,String> categoriesMap;

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
	 public void useParserResult(TreeMap<String,String> result)
		 {
		 categoriesMap=result;
		 if(categoriesMap==null)
			 finish();
		 else
			 initDrawerMenu();
		 }

	 @Override
	 protected void onCreate(Bundle savedInstanceState)
		 {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.main_layout);

		 Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
		 DrawerLayout drawer= (DrawerLayout)findViewById(R.id.drawer_layout);
		 navigationView= (NavigationView)findViewById(R.id.nav_view);

		 setSupportActionBar(toolbar);
		 ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
		 drawer.addDrawerListener(toggle);
		 toggle.syncState();
		 navigationView.setNavigationItemSelectedListener(this);

		 InitCategoriesParser parser= new InitCategoriesParser(this,this,O.web.HOST);
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
		 FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
		 transaction.commit();
		 Toast.makeText(this,""+ categoriesMap.get(item.getTitle() ),Toast.LENGTH_SHORT).show();
		 DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
		 drawer.closeDrawer(GravityCompat.START);
		 return true;
		 }
	 }
