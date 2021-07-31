package com.nuchange.informatics.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="url")
public class UrlEntity {
	
	// define fields
	
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id")
		private int id;
		
		@Column(name="url")
		private String url;
		
		@Column(name="usage_count")
		private int usageCount;
		
		// define constructors

		public UrlEntity() {
				
		}

		public UrlEntity(String url, int usageCount) {
			this.url = url;
			this.usageCount = usageCount;
		}
		
		// define getter/setter

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getUsageCount() {
			return usageCount;
		}

		public void setUsageCount(int usageCount) {
			this.usageCount = usageCount;
		}

		// define tostring
		
		@Override
		public String toString() {
			return "URL [id=" + id + ", url=" + url + ", usageCount=" + usageCount + "]";
		}

}
