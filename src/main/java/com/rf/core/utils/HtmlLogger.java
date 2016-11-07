package com.rf.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.testng.annotations.Test;

/**
 * The Class HtmlLogger.
 */
public class HtmlLogger

{

	public void createHtmlLogFile() throws IOException {
		OutputStream htmlfile = new FileOutputStream(new File(
				"logs/rf-info.html"));
		PrintStream printhtml = new PrintStream(htmlfile);
		String imagePath = System.getProperty("user.dir")+ "\\src\\test\\resources\\staticdata\\RodanAndFields.png";
		System.out.println("Image path is "+imagePath);

		BufferedReader br = new BufferedReader(new FileReader("logs/rf-info.log"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			String htmlheader = "<html><head>";
			htmlheader += "<title>Execution Log - RodanAndFields</title>";
			htmlheader += "</head><body BGCOLOR=\"#FAFAFA\">"+
					"<LEFT><img src="+imagePath+"></img></LEFT> "+
					"<CENTER><FONT FACE=\"Algerian\" COLOR=\"#483C32\" +" +
					" SIZE=\"6\"><U>AUTOMATION TEST REPORT</U></FONT></CENTER><br/><br/>";
			String htmlfooter = "<p>Created by Shubham Mathur\n GSPANN Technologies Inc.</body></html>";
			sb.append(htmlheader);

			int count = 1;
			while (line != null) {
				line = line.replace("[main]", "");

				if (line.contains("TEST CASE NAME")){
					sb.append("<font color='#0000FF'>" + "<" + "br" + "/><b>"
							+ line + "</b></font>");
				}

				else if (line.contains("TEST HAS FAILED")){
					sb.append("<font color='#F70D1A'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}
				else if (line.contains("TEST IS SUCCESSFUL")){
					sb.append("<font color='#04B404'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}

				else if (line.contains("TEST IS SKIPPED")){
					sb.append("<font color='#848484'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}

				else if (line.contains("[RFL DATABASE ASSERTION FAILURE")){
					sb.append("<font color='#DF7401'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}

				else if (line.contains("[FUNCTIONAL FAILURE - ASSERTION ERROR")){
					sb.append("<font color='#DF7401'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}
				else if (line.contains("VERIFICATION FAILED")){
					sb.append("<font color='#DF7401'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}
				else if (line.contains("RETRYING TEST")){
					sb.append("<font color='#43BFC7'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}				

				else {
					if (count == 1)
						sb.append(line);
					else
						sb.append("<" + "br" + "/>" + line);

					++count;
				}

				line = br.readLine();
			}

			sb.append(htmlfooter);			
			printhtml.println(sb);
			printhtml.close();
			htmlfile.close();
		} finally {
			br.close();
		}
	}

}
