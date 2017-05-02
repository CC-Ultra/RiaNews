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
	 private Long id;

	 private String txt,date,theme,newsTxt,pic,link;

		@Generated(hash = 1465876385)
		public News(Long id, String txt, String date, String theme, String newsTxt,
				String pic, String link) {
			this.id = id;
			this.txt = txt;
			this.date = date;
			this.theme = theme;
			this.newsTxt = newsTxt;
			this.pic = pic;
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

		public String getPic() {
			return this.pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public String getNewsTxt() {
			return this.newsTxt;
		}

		public void setNewsTxt(String newsTxt) {
			this.newsTxt = newsTxt;
		}

		public String getLink() {
			return this.link;
		}

		public void setLink(String link) {
			this.link = link;
		}
	 }
