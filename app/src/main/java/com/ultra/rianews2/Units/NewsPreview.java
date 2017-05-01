package com.ultra.rianews2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * <p></p>
 * <p><sub>(30.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

@Entity
public class NewsPreview
	 {
	 @Id(autoincrement = true)
	 Long id;

	 private String pic,newsStr,link;
	 long categoryId,newsId;

	 @ToOne(joinProperty = "newsId")
	 News news;

		/** Used to resolve relations */
		@Generated(hash = 2040040024)
		private transient DaoSession daoSession;

		/** Used for active entity operations. */
		@Generated(hash = 1973595740)
		private transient NewsPreviewDao myDao;

		@Generated(hash = 1059550001)
		public NewsPreview(Long id, String pic, String newsStr, String link,
				long categoryId, long newsId) {
			this.id = id;
			this.pic = pic;
			this.newsStr = newsStr;
			this.link = link;
			this.categoryId = categoryId;
			this.newsId = newsId;
		}

		@Generated(hash = 1139497285)
		public NewsPreview() {
		}

		public Long getId() {
			return this.id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getPic() {
			return this.pic;
		}

		public void setPic(String pic) {
			this.pic = pic;
		}

		public String getNewsStr() {
			return this.newsStr;
		}

		public void setNewsStr(String newsStr) {
			this.newsStr = newsStr;
		}

		public String getLink() {
			return this.link;
		}

		public void setLink(String link) {
			this.link = link;
		}

		public long getCategoryId() {
			return this.categoryId;
		}

		public void setCategoryId(long categoryId) {
			this.categoryId = categoryId;
		}

		public long getNewsId() {
			return this.newsId;
		}

		public void setNewsId(long newsId) {
			this.newsId = newsId;
		}

		@Generated(hash = 94487604)
		private transient Long news__resolvedKey;

		/** To-one relationship, resolved on first access. */
		@Generated(hash = 350549006)
		public News getNews() {
			long __key = this.newsId;
			if (news__resolvedKey == null || !news__resolvedKey.equals(__key)) {
				final DaoSession daoSession = this.daoSession;
				if (daoSession == null) {
					throw new DaoException("Entity is detached from DAO context");
				}
				NewsDao targetDao = daoSession.getNewsDao();
				News newsNew = targetDao.load(__key);
				synchronized (this) {
					news = newsNew;
					news__resolvedKey = __key;
				}
			}
			return news;
		}

		/** called by internal mechanisms, do not call yourself. */
		@Generated(hash = 2011663054)
		public void setNews(@NotNull News news) {
			if (news == null) {
				throw new DaoException(
						"To-one property 'newsId' has not-null constraint; cannot set to-one to null");
			}
			synchronized (this) {
				this.news = news;
				newsId = news.getId();
				news__resolvedKey = newsId;
			}
		}

		/**
		 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
		 * Entity must attached to an entity context.
		 */
		@Generated(hash = 128553479)
		public void delete() {
			if (myDao == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			myDao.delete(this);
		}

		/**
		 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
		 * Entity must attached to an entity context.
		 */
		@Generated(hash = 1942392019)
		public void refresh() {
			if (myDao == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			myDao.refresh(this);
		}

		/**
		 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
		 * Entity must attached to an entity context.
		 */
		@Generated(hash = 713229351)
		public void update() {
			if (myDao == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			myDao.update(this);
		}

		/** called by internal mechanisms, do not call yourself. */
		@Generated(hash = 482619265)
		public void __setDaoSession(DaoSession daoSession) {
			this.daoSession = daoSession;
			myDao = daoSession != null ? daoSession.getNewsPreviewDao() : null;
		}
	 }
