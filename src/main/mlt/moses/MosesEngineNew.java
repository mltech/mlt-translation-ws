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

public class MosesEngineNew
{
	private BufferedReader mosesReader;
	private BufferedWriter mosesWriter;	
	
	public MosesEngineNew() throws MosesException
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

        String result = "";
        
        InputStream inputstream = mosesProcess.getInputStream();
        InputStreamReader mosesInp = new InputStreamReader(inputstream);
        mosesReader = new BufferedReader(mosesInp, 10000);

        OutputStream outputstream = mosesProcess.getOutputStream();
        OutputStreamWriter mosesOup = new OutputStreamWriter(outputstream);
        mosesWriter = new BufferedWriter(mosesOup, 10000);

// est ce que response sert a qqch?
        String response = "";
        try {
			while ((response = mosesReader.readLine()) != null)
			{
			    System.out.println(response);
			    result = result + response + "\n";
			    if (response.startsWith("Created input-output object"))
			        break;
			}
		} catch (IOException e) {
			throw new MosesException(e.getMessage());
		}
	}
	
	public String translate(String segment) throws MosesException
	{
        String translation;
        String translatedString = "";
        if (segment == null) return "";
        try {
        	System.err.println(segment);
        	mosesWriter.write("<+> " + segment + " </+>\n");
	        mosesWriter.flush();
// source with \n automatically splits the response
//
	        while ((translation = mosesReader.readLine()) != null)
	        {
	            if (translation.startsWith("BEST TRANSLATION: "))
	            {
	            	translation = translation.substring(18);
	            	translation = translation.replaceAll("\\[[^\\[]*\\] *\\[total.*", "");
	            	translation = translation.replaceAll("\\|UNK", "");
	            	
	                if (translation.endsWith("</+> "))
	                {
	                	translatedString += translation.replace("</+> ", "");
	                	break;
	                }
	                else
		            	translatedString += translation + "\n";
	            }
	        }
		} catch (IOException e) {
			throw new MosesException(e.getMessage());
		}

        return translatedString.substring(4);
	}
}
