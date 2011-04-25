package mlt.moses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class MosesEngine // TODO: inherit from Command
{
	private BufferedReader mosesReader;
	private BufferedWriter mosesWriter;	
	
	public MosesEngine() throws MosesException
	{
		Properties properties = new Properties();
		try {
		    properties.load(new FileInputStream("conf/moses.properties"));
		} catch (IOException e) {
		}
		
        //ProcessBuilder mosespb = new ProcessBuilder("moses", "-f", "conf/moses.ini", "-t", "-d"); // , outFile);
		ProcessBuilder mosespb = new ProcessBuilder(properties.get("moses_executable").toString(), "-f", properties.get("moses_ini_file").toString()); // , outFile);
        mosespb.redirectErrorStream(true);

        Process mosesProcess;
		try {
			mosesProcess = mosespb.start();
		} catch (IOException e) {
			throw new MosesException("Unable to start process");
		}
        if (mosesProcess == null)
        	throw new MosesException("No process");

        InputStream inputstream = mosesProcess.getInputStream();
        InputStreamReader mosesInp = new InputStreamReader(inputstream);
        mosesReader = new BufferedReader(mosesInp, 10000);

        OutputStream outputstream = mosesProcess.getOutputStream();
        OutputStreamWriter mosesOup = new OutputStreamWriter(outputstream);
        mosesWriter = new BufferedWriter(mosesOup, 10000);
	}
	
	public String translate(String segment) throws MosesException
	{
        String translation;
        String translatedString = "";
        boolean translationLine = false;
        boolean isReady = false;
        if (segment == null) return "";
        System.out.println("segment: " + segment);
        try {
        	mosesWriter.write("<+> " + doSimpleChineseTokenization(segment) + " </+>\n");
	        mosesWriter.flush();
	        while ((translation = mosesReader.readLine()) != null)
	        {
	        	System.out.println("-> " + translation);
	        	if (translation.contains("<+>"))
	        	{
	        		translation = translation.replace("<+>", "");
	        		isReady = true;
	        		translationLine = true;
	        	}

	        	if (translationLine && isReady)
	            {
	                if (translation.contains("</+> "))
	                {
	                	translatedString += translation.replace("</+>", "");
	                	break;
	                }
	                else
		            	translatedString += translation + "\n";
	            }
	        	translationLine = ! translationLine;
	        }
		} catch (IOException e) {
			throw new MosesException(e.getMessage());
		}

        return translatedString.trim();
	}
	
	private String doSimpleChineseTokenization(String segment)
	{
		String ret = "";
		for (String c:segment.split(""))
			ret += c + " ";
		ret = ret.replaceAll("  ", " ");
		return ret.substring(0, ret.length() - 1);
	}
}
