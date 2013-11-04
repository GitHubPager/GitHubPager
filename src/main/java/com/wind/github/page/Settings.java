package com.wind.github.page;

import java.util.List;

public class Settings {
	public String title;
	public String description;
	public String domain;
	public String subdomain;
	public int articlePerPage=5;
	public List<Menu> menus;
	public String twitterID;
	public String facebookID;
	public String googleAnalyzerId;
	public String diqusId;
	public String footer;
	public String repository;
	public String template;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getSubdomain() {
		return subdomain;
	}
	public void setSubdomain(String subdomain) {
		this.subdomain = subdomain;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	public String getTwitterID() {
		return twitterID;
	}
	public void setTwitterID(String twitterID) {
		this.twitterID = twitterID;
	}
	public String getFacebookID() {
		return facebookID;
	}
	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}
	public String getGoogleAnalyzerId() {
		return googleAnalyzerId;
	}
	public void setGoogleAnalyzerId(String googleAnalyzerId) {
		this.googleAnalyzerId = googleAnalyzerId;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
	}
	public int getArticlePerPage() {
		return articlePerPage;
	}
	public void setArticlePerPage(int articlePerPage) {
		this.articlePerPage = articlePerPage;
	}
	public String getDiqusId() {
		return diqusId;
	}
	public void setDiqusId(String diqusId) {
		this.diqusId = diqusId;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
}
