package com.wind.cms;

import java.io.InputStream;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.cache.annotation.Cacheable;

public class PageGenerator {
	
	
	String templateRepository;
	public void setTemplateRepository(String templateRepository) {
		this.templateRepository = templateRepository;
	}
	
	
	/*
	 * Get Template From Remote Repository
	 */
	@Cacheable(value = "page")
	private List<Template> getTemplateListFromRepository() throws Exception
	{
		URL uri=new URL(templateRepository);
		InputStream stream=(uri.openStream());
		SAXReader saxReader = new SAXReader();
		try
		{
			Document document = saxReader.read(stream);
			Element element=document.getRootElement();
			List<Template> result=new ArrayList<Template>();
			Iterator<?> tempIt=element.elementIterator(PageConstants.TEMPLATETAG);
			while(tempIt.hasNext())
			{
				Element templateElement=(Element)tempIt.next();
				Iterator<?> it=templateElement.elementIterator();
				Template template=new Template();
				while(it.hasNext())
				{
					Element t=(Element)it.next();
					if(t.getName().equals(PageConstants.NAMETAG))
					{
						template.setName(t.getTextTrim());
					}
					else if(t.getName().equals(PageConstants.URLTAG))
					{
						template.setUrl(t.getTextTrim());
					}
					else if(t.getName().equals(PageConstants.VERSIONTAG))
					{
						template.setVersion(t.getTextTrim());
					}
					else if(t.getName().equals(PageConstants.IMAGETAG))
					{
						template.setImage(t.getTextTrim());
					}
					else if(t.getName().equals(PageConstants.AUTHORTAG))
					{
						template.setAuthor(t.getTextTrim());
					}
					else if(t.getName().equals(PageConstants.DESCRIPTIONTAG))
					{
						template.setDescription(t.getTextTrim());
					}
					else if(t.getName().equals(PageConstants.DATETAG))
					{
						template.setDate(t.getTextTrim());
					}
					else
					{
						throw new Exception("Unable to parse template file");
					}
					result.add(template);
				}
			}
			return result;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			try
			{
				stream.close();
			}
			catch(Exception exx)
			{
				
			}
		}
		
		
	}
	/*public String generateArticleEntryXMLFile(Map<String,String> params) 
	{
		Document document = DocumentHelper.createDocument();
		Element root=document.addElement(PageConstants.ARTICLEENTRYTAG);
		Set<Entry<String,String>> entrySet=params.entrySet();
		Iterator<Entry<String,String>> it=entrySet.iterator();
		while(it.hasNext())
		{
			Entry<String,String> temp=it.next();
			Element nElement=root.addElement(temp.getKey());
			nElement.setText(temp.getValue());
		}
		return document.asXML();
	}
	public Map<String,String> parseArticleEntryXMLString(String xml) throws Exception
	{
		Document document = DocumentHelper.parseText(xml);
		Element root=document.getRootElement();
		Iterator<?> tempIt=root.elementIterator();
		Map<String,String> result=new HashMap<String,String>();
		while(tempIt.hasNext())
		{
			Element e=(Element)tempIt.next();
			result.put(e.getName(), e.getText());
		}
		return result;
	}
	public Map<String,String> parseArticleEntryXMLFile(File f) throws Exception
	{
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(f);
		Element root=document.getRootElement();
		
		Iterator<?> tempIt=root.elementIterator();
		Map<String,String> result=new HashMap<String,String>();
		while(tempIt.hasNext())
		{
			Element e=(Element)tempIt.next();
			result.put(e.getName(), e.getText());
			System.out.println(e.getName()+" "+ e.getTextTrim());
		}
		return result;
	}*/
	
	public static void main(String args[])
	{
		Map<String,String> params=new HashMap<String,String>();
		params.put("title", "test");
		params.put("titlsade", "tsdest");
		PageGenerator p=new PageGenerator();
	
		p.setTemplateRepository("file:///c:/test.xml");
		try
		{
			p.getTemplateListFromRepository();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
