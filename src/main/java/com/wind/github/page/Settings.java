package com.wind.github.page;

import java.util.List;

public class Settings {
	public String title;
	public String description;
	public String domain;
	public String subdomain;
	public int articlePerPage=5;
	public boolean showMenu=false;
	public List<Menu> menus;
	public boolean showSidebar=false;
	public boolean showTwitter=false;
	public String twitterID;
	public boolean showFaceBook=false;
	public String facebookID;
	public String googleAnalyzerId;
	public boolean showComment=false;
	public String footer;
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
	public boolean isShowMenu() {
		return showMenu;
	}
	public void setShowMenu(boolean showMenu) {
		this.showMenu = showMenu;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	public boolean isShowSidebar() {
		return showSidebar;
	}
	public void setShowSidebar(boolean showSidebar) {
		this.showSidebar = showSidebar;
	}
	public boolean isShowTwitter() {
		return showTwitter;
	}
	public void setShowTwitter(boolean showTwitter) {
		this.showTwitter = showTwitter;
	}
	public String getTwitterID() {
		return twitterID;
	}
	public void setTwitterID(String twitterID) {
		this.twitterID = twitterID;
	}
	public boolean isShowFaceBook() {
		return showFaceBook;
	}
	public void setShowFaceBook(boolean showFaceBook) {
		this.showFaceBook = showFaceBook;
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
	public boolean isShowComment() {
		return showComment;
	}
	public void setShowComment(boolean showComment) {
		this.showComment = showComment;
	}
	public int getArticlePerPage() {
		return articlePerPage;
	}
	public void setArticlePerPage(int articlePerPage) {
		this.articlePerPage = articlePerPage;
	}
}
