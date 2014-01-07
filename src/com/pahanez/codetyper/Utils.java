package com.pahanez.codetyper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import com.pahanez.codertyper.R;


public class Utils {
	public static String slurp(final InputStream is, final int bufferSize)
	{
	  final char[] buffer = new char[bufferSize];
	  final StringBuilder out = new StringBuilder();
	  try {
	    final Reader in = new InputStreamReader(is, "UTF-8");
	    try {
	      for (;;) {
	        int rsz = in.read(buffer, 0, buffer.length);
	        if (rsz < 0)
	          break;
	        out.append(buffer, 0, rsz);
	      }
	    }
	    finally {
	      in.close();
	    }
	  }
	  catch (UnsupportedEncodingException ex) {
	    /* ... */
	  }
	  catch (IOException ex) {
	      /* ... */
	  }
	  return out.toString();
	}
	
	public static boolean shouldStart(String name){
		String [] extras = CodeTyperApplication.getAppContext().getResources().getStringArray(R.array.extra_source_names);
		for (String extra_name : extras)
			if(name.equals(extra_name)) return false;
		return true;
	}

	public static void updateAvailables(String mSourceId) {
		String [] source_names = CodeTyperApplication.getAppContext().getResources().getStringArray(R.array.source_names);
			for(int i = 0; i < source_names.length; i++)
			if(source_names[i].equals(mSourceId)){
				Settings.getInstance().addLevelComplete(i+1);
				break;
			}
	}
	
	public static boolean isLastOne(String sourceId){
		String [] source_names = CodeTyperApplication.getAppContext().getResources().getStringArray(R.array.source_names);
		
		for(int i = 0; i < source_names.length; i++){
			if(source_names[i].equals(sourceId)){
					if(i == (source_names.length-1))
						return true;
				return false;
			}
		}
		return false;
	}
}
