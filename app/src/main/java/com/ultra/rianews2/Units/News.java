package com.ultra.rianews2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p></p>
 * <p><sub>(30.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

@Entity
public class News
	 {
	 @Id(autoincrement = true)
	 Long id;

	 String txt,date,theme,link;

		@Generated(hash = 265708018)
		public News(Long id, String txt, String date, String theme, String link) {
			this.id = id;
			this.txt = txt;
			this.date = date;
			this.theme = theme;
			this.link = link;
		}

		@Generated(hash = 1579685679)
		public News() {
		}

		public Long getId() {
			return this.id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTxt() {
			return this.txt;
		}

		public void setTxt(String txt) {
			this.txt = txt;
		}

		public String getDate() {
			return this.date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getTheme() {
			return this.theme;
		}

		public void setTheme(String theme) {
			this.theme = theme;
		}

		public String getLink() {
			return this.link;
		}

		public void setLink(String link) {
			this.link = link;
		}
	 }
