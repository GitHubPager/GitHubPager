package com.wind.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class FileUtils {
	public static int TEMP_DIR_ATTEMPTS=128;
	public static File downloadFileFromInternet(String remoteUrl) throws Exception
	{
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try
		{
		 URL url = new URL(remoteUrl);
         File tempFile=File.createTempFile("temp", "zip");

         inputStream = 
             new BufferedInputStream(url.openStream()); 
         outputStream = 
             new BufferedOutputStream( 
                   new FileOutputStream(tempFile)); 

         int read = 0;
         while((read = inputStream.read()) != -1) { 
             outputStream.write(read); 
         } 
         
         inputStream.close(); 
         outputStream.flush(); 
         outputStream.close();
         return tempFile;
		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			try
			{
				 inputStream.close();
		         outputStream.close();
			}
			catch(Exception ex)
			{
				
			}
		}
	}
	public static File createTempDir() {
		  File baseDir = new File(System.getProperty("java.io.tmpdir"));
		  String baseName = System.currentTimeMillis() + "-";

		  for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
		    File tempDir = new File(baseDir, baseName + counter);
		    if (tempDir.mkdir()) 
		    {
		      return tempDir;
		    }
		  }
		  throw new IllegalStateException("Failed to create directory within "
		      + TEMP_DIR_ATTEMPTS + " attempts (tried "
		      + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
		}
	public static String dumpInputStreamIntoString(InputStream f,String encoding) throws Exception
	{
		ByteArrayOutputStream byteStream=null;
		BufferedInputStream fileStream=null;
		byteStream=new ByteArrayOutputStream();
		fileStream=new BufferedInputStream((f));
		int data=-1;
		while((data=fileStream.read())!=-1)
		{
			byteStream.write(data);
		}
		byte[]raw= byteStream.toByteArray();
		return new String(raw,encoding);
		
	}
	public static String dumpFileIntoString(File f,String encoding)
	{
		ByteArrayOutputStream byteStream=null;
		BufferedInputStream fileStream=null;
		try
		{
			byteStream=new ByteArrayOutputStream();
			fileStream=new BufferedInputStream(new FileInputStream(f));
			int data=-1;
			while((data=fileStream.read())!=-1)
			{
				byteStream.write(data);
			}
			byte[]raw= byteStream.toByteArray();
			return new String(raw,encoding);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				fileStream.close();
			}
			catch(IOException ex)
			{
				
			}
		}
	}
	public static byte[] dumpFileIntoByteArray(File f) throws IOException
	{
		ByteArrayOutputStream byteStream=null;
        BufferedInputStream fileStream=null;
        try
        {
                byteStream=new ByteArrayOutputStream();
                fileStream=new BufferedInputStream(new FileInputStream(f));
                int data=-1;
                while((data=fileStream.read())!=-1)
                {
                        byteStream.write(data);
                }
        }
        catch(IOException e)
        {
                throw e;
        }
        finally
        {
                try
                {
                        fileStream.close();
                }
                catch(Exception ex)
                {
                        
                }
        }
        byte[] data=byteStream.toByteArray();
        return data;
	}
}
