package com.ultra.rianews2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * <p></p>
 * <p><sub>(30.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

@Entity
public class Category
	 {
	 @Id(autoincrement = true)
	 Long id;

	 private String title,link;
	 @ToMany(referencedJoinProperty = "categoryId")
	 private List<NewsPreview> newsPreviews;

		/** Used to resolve relations */
		@Generated(hash = 2040040024)
		private transient DaoSession daoSession;

		/** Used for active entity operations. */
		@Generated(hash = 40161530)
		private transient CategoryDao myDao;
		@Generated(hash = 1088275079)
		public Category(Long id, String title, String link) {
			this.id = id;
			this.title = title;
			this.link = link;
		}
		@Generated(hash = 1150634039)
		public Category() {
		}
		public Long getId() {
			return this.id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getTitle() {
			return this.title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		/**
		 * To-many relationship, resolved on first access (and after reset).
		 * Changes to to-many relations are not persisted, make changes to the target entity.
		 */
		@Generated(hash = 1260201725)
		public List<NewsPreview> getNewsPreviews() {
			if (newsPreviews == null) {
				final DaoSession daoSession = this.daoSession;
				if (daoSession == null) {
					throw new DaoException("Entity is detached from DAO context");
				}
				NewsPreviewDao targetDao = daoSession.getNewsPreviewDao();
				List<NewsPreview> newsPreviewsNew = targetDao._queryCategory_NewsPreviews(id);
				synchronized (this) {
					if (newsPreviews == null) {
						newsPreviews = newsPreviewsNew;
					}
				}
			}
			return newsPreviews;
		}
		/** Resets a to-many relationship, making the next get call to query for a fresh result. */
		@Generated(hash = 531358326)
		public synchronized void resetNewsPreviews() {
			newsPreviews = null;
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
		public String getLink() {
			return this.link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		/** called by internal mechanisms, do not call yourself. */
		@Generated(hash = 503476761)
		public void __setDaoSession(DaoSession daoSession) {
			this.daoSession = daoSession;
			myDao = daoSession != null ? daoSession.getCategoryDao() : null;
		}
	 }
