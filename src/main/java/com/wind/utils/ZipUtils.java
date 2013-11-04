package com.wind.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
	public static void unzip(File zipFile, File outputFolder){
		 
	     byte[] buffer = new byte[1024];
	     ZipInputStream zis=null;
	     try{	 
	    
	    	//get the zip file content
	    	zis = new ZipInputStream(new FileInputStream(zipFile));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();
	 
	    	while(ze!=null)
	    	{
	    		String fileName = ze.getName();
	            File newFile = new File(outputFolder.getAbsolutePath() + File.separator + fileName);
	  
	            System.out.println("file unzip : "+ newFile.getAbsoluteFile());
	  
	             //create all non exists folders
	             //else you will hit FileNotFoundException for compressed folder
	             new File(newFile.getParent()).mkdir();
	             if(!ze.isDirectory())
	             {
		             FileOutputStream fos = new FileOutputStream(newFile);             
		  
		             int len;
		             while ((len = zis.read(buffer)) > 0) {
		        		fos.write(buffer, 0, len);
		             }
		             fos.flush();
		             fos.close();
	             }
	             ze = zis.getNextEntry();
	    	}
	    }catch(Exception ex){
	       ex.printStackTrace(); 
	    }
	     finally
	     {
	    	 try
	    	 {
	    		 zis.closeEntry();
	 	    	 zis.close();
	    	 }
	    	 catch(Exception e)
	    	 {
	    		 
	    	 }
	     }
	}
	
}
