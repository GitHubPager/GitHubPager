package com.wind.web;

import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.wind.github.PageManager;
import com.wind.github.page.ArticleEntry;
import com.wind.github.page.ArticleSet;
import com.wind.github.page.Settings;
import com.wind.github.page.Template;

public class PanelController extends MultiActionController{
	PageManager pageManager;
	
	String listViewPage;
	String initAccountViewPage;
	String initAccountURL;
	String setupURL;
	String setupViewPage;
	String addPostViewPage;
	String editPostViewPage;
	String editSettingViewPage;
	String verifyMailUrl;
	String logoutPage;
	String manageRepositoryViewPage;
	String manageRepositoryURL;
	public ModelAndView list(HttpServletRequest req, HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		List<Repository> repoArray=pageManager.getUserRepositories(accessToken);
		if(repoArray.size()==0||!pageManager.isAccountReadyForPage(u, repoArray))
		{
			res.sendRedirect(initAccountURL);
			return null;
		}
		ModelAndView view=new ModelAndView();
		view.setViewName(listViewPage);
		view.addObject("repository", repoArray);
		view.addObject("userInfo", u);
		return view;
    }
	public ModelAndView commitInitAccount(HttpServletRequest req, 
    HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		if(!pageManager.checkVerifedEmail(accessToken))
		{
			res.sendRedirect(verifyMailUrl);
			return null;
		}
		pageManager.initAccountPage(u, accessToken);
		pageManager.bonusAuthor(accessToken);
		res.sendRedirect(req.getRequestURI());
    	return null;
    }
	public ModelAndView initAccount(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
	{
		
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);	
		ModelAndView view=new ModelAndView();
		view.setViewName(initAccountViewPage);
		view.addObject("userInfo", u);
		return view;
	}
	
	public ModelAndView manageRepository(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		Repository repo=pageManager.getStubRepository(u, repoName);
		if(pageManager.isRepositoryPageCMSInit(repo, accessToken))
		{
			int pid=1;
			String page=req.getParameter("page");
			if(page!=null&&!page.isEmpty()) pid=Integer.valueOf(page);
			ArticleSet aset=pageManager.getArticleSet(repo, accessToken);
			int setSize=aset.getIds().size();
			List<Integer> ids=aset.getIds();
			List<ArticleEntry> entrys=new ArrayList<ArticleEntry>();
			int cid=0;
			if((pid-1)*WebConstants.POSTPERPAGEINMANAGEREPOSITORYPAGE<setSize)
			{
				cid=(pid-1)*WebConstants.POSTPERPAGEINMANAGEREPOSITORYPAGE;
			}
			int added=0;
			while(true)
			{
				if(cid<setSize&&added<WebConstants.POSTPERPAGEINMANAGEREPOSITORYPAGE)
				{
					ArticleEntry entry=pageManager.getArticleEntry(repo, ids.get(cid), accessToken);
					if(entry!=null)
					{
					//logger.info("load:"+entry.getId());
					entrys.add(entry);
					}
					
					added++;
					cid++;
				}
				else
				{
					break;
				}
			}
			
			ModelAndView view=new ModelAndView();
			view.setViewName(manageRepositoryViewPage);
			view.addObject("repository", repoName);
			view.addObject("userInfo", u);
			view.addObject("entrys",entrys);
			view.addObject("currentPage",pid);
			view.addObject("totalPage",aset.getIds().size()/WebConstants.POSTPERPAGEINMANAGEREPOSITORYPAGE+1);
			return view;
		}
		else
		{
			res.sendRedirect(setupURL+"&repositoryName="+repoName);
			return null;
		}
		
		
    }
	public ModelAndView logout(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		s.invalidate();
		ModelAndView view=new ModelAndView();
		view.setViewName(logoutPage);
		return view;
    }
	public ModelAndView setup(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		List<Template> templateList=pageManager.getTemplateListFromRepository();
		ModelAndView view=new ModelAndView();
		view.addObject("repository", repoName);
		view.addObject("userInfo",u);
		view.addObject(templateList);
		view.setViewName(setupViewPage);
		return view;
    }
	public ModelAndView commitSetup(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
	    String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		String template=req.getParameter("template");
		String title=req.getParameter("title");
		String description=req.getParameter("description");
		String domain=req.getParameter("domain");
		Settings setting=new Settings();
		setting.setDescription(description);
		setting.setDomain(domain);
		setting.setTitle(title);
		setting.setRepository(repoName);
		setting.setTemplate(template);
		Repository repo=pageManager.getStubRepository(u, repoName);
		pageManager.setupRepositoryPageCMS(repo, setting, accessToken);
		res.sendRedirect(req.getRequestURI());
		return null;
    }
	public ModelAndView addPost(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		ModelAndView view=new ModelAndView();
		view.addObject("repository", repoName);
		view.addObject("userInfo",u);
		view.setViewName(addPostViewPage);
		return view;
    }
	public ModelAndView deletePost(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		Repository repo=pageManager.getStubRepository(u, repoName);
		String entryId=req.getParameter("entryId");
		ArticleEntry entry=new ArticleEntry();
		entry.setId(Integer.valueOf(entryId));
		pageManager.removeArticleEntry(repo, entry, accessToken);
		res.sendRedirect(this.manageRepositoryURL+"&repositoryName="+repoName);
		return null;
    }
	public ModelAndView editPost(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
     {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		Repository repo=pageManager.getStubRepository(u, repoName);
		String entryId=req.getParameter("entryId");
		
		ArticleEntry entry=pageManager.getArticleEntry(repo, Long.valueOf(entryId), accessToken);
		ModelAndView view=new ModelAndView();
		view.addObject("repository", repoName);
		view.addObject("userInfo",u);
		view.addObject("entry",entry);
		view.setViewName(editPostViewPage);
		return view;
		
     }
	
	
	public ModelAndView commitEditPost(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
     {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		Repository repo=pageManager.getStubRepository(u, repoName);
		String entryId=req.getParameter("entryId");
		String title=req.getParameter("title");
		String content=req.getParameter("content");
		ArticleEntry entry=new ArticleEntry();
		entry.setId(Integer.valueOf(entryId));
		entry.setContent(content);
		entry.setAuthor(u.getName());
		entry.setTitle(title);
		//entry.setDate("Pending");
		pageManager.editArticleEntry(repo, entry, accessToken);
		res.sendRedirect(this.manageRepositoryURL+"&repositoryName="+repoName);
		return null;
     }
	public ModelAndView commitAddPost(HttpServletRequest req, 
            HttpServletResponse res) throws Exception
    {
		HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		Repository repo=pageManager.getStubRepository(u, repoName);
		String title=req.getParameter("title");
		String content=req.getParameter("content");
		ArticleEntry entry=new ArticleEntry();
		entry.setContent(content);
		entry.setAuthor(u.getName());
		entry.setTitle(title);
		SimpleDateFormat dateformat1=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		  
		entry.setDate(dateformat1.format(new Date()));
		pageManager.addNewArticleEntry(repo, entry, accessToken);
		res.sendRedirect(this.manageRepositoryURL+"&repositoryName="+repoName);
		return null;
    }
	public ModelAndView editSetting(HttpServletRequest req, 
    HttpServletResponse res) throws Exception
    {
    	HttpSession s=req.getSession();
		String accessToken=(String)s.getAttribute(WebConstants.ACCESSTOKEN);
	
		User u=getUserInfoViaSession(s,accessToken);
		String repoName=req.getParameter("repositoryName");
		Repository repo=pageManager.getStubRepository(u, repoName);
		Settings setting=pageManager.getSettings(repo, accessToken);
		
		ModelAndView view=new ModelAndView();
		view.addObject("repository", repo);
		view.addObject("userInfo",u);
		view.addObject("setting",setting);
		view.setViewName(editSettingViewPage);
		return view;
    }
	
	
	
	public void setPageManager(PageManager pageManager) {
		this.pageManager = pageManager;
	}



	public void setlistViewPage(String listViewPage) {
		this.listViewPage = listViewPage;
	}
	
	public void setListViewPage(String listViewPage) {
		this.listViewPage = listViewPage;
	}
	public void setInitAccountURL(String initAccountURL) {
		this.initAccountURL = initAccountURL;
	}
	public void setSetupURL(String setupURL) {
		this.setupURL = setupURL;
	}
	public void setSetupViewPage(String setupViewPage) {
		this.setupViewPage = setupViewPage;
	}
	private User getUserInfoViaSession(HttpSession s,String accessToken) throws Exception
	{
		User u=(User)(s.getAttribute(WebConstants.USERINFOCODE));
		if(u==null)
		{
			u=pageManager.getBasicUserInfo(accessToken);
			s.setAttribute(WebConstants.USERINFOCODE, u);
		}
		return u;
	}
	
	
	public void setAddPostViewPage(String addPostViewPage) { 
		this.addPostViewPage = addPostViewPage;
	}
	public void setVerifyMailUrl(String verifyMailUrl) {
		this.verifyMailUrl = verifyMailUrl;
	}
	public void setInitAccountViewPage(String initAccountViewPage) {
		this.initAccountViewPage = initAccountViewPage;
	}
	public void setLogoutPage(String logoutPage) {
		this.logoutPage = logoutPage;
	}
	public void setManageRepositoryViewPage(String manageRepositoryViewPage) {
		this.manageRepositoryViewPage = manageRepositoryViewPage;
	}
	public void setEditSettingViewPage(String editSettingViewPage) {
		this.editSettingViewPage = editSettingViewPage;
	}
	public void setEditPostViewPage(String editPostViewPage) {
		this.editPostViewPage = editPostViewPage;
	}
	public void setManageRepositoryURL(String manageRepositoryURL) {
		this.manageRepositoryURL = manageRepositoryURL;
	}

}
