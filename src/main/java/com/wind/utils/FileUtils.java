package com.wind.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
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
