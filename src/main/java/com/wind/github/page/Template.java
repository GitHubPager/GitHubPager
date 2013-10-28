package com.wind.github.page;

public class Template {
	public static String TEMPLATETAG="template";
	public static String NAMETAG="name";
	public static String URLTAG="url";
	public static String IMAGETAG="img";
	public static String AUTHORTAG="author";
	public static String DATETAG="date";
	public static String VERSIONTAG="version";
	public static String DESCRIPTIONTAG="description";
	private String name;
	private String url;
	private String image;
	private String description;
	private String author;
	private String date;
	private String version;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}
