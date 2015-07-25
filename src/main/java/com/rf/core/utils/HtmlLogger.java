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

	@Test(testName="")
	public void createHtmlLogFile() throws IOException {
		OutputStream htmlfile = new FileOutputStream(new File(
				"logs/rf-info.html"));
		PrintStream printhtml = new PrintStream(htmlfile);


		BufferedReader br = new BufferedReader(new FileReader("logs/rf-info.log"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			String htmlheader = "<html><head>";
			htmlheader += "<title>Execution Log - RodanAndFields</title>";
			htmlheader += "</head><body BGCOLOR=\"#E5E4E2\"><CENTER><FONT FACE=\"TIMES NEW ROMAN\" COLOR=\"#2B65EC\" +" +
					"SIZE=\"7\"><U>RODAN AND FIELDS</U></FONT></CENTER><br/><br/> "+
					"<LEFT><img src=\"src/test/resources/staticdata/RodanAndFields.jpg\"></img></LEFT> "+
					"<CENTER><FONT FACE=\"Algerian\" COLOR=\"#483C32\" +" +
					" SIZE=\"7\"><U>AUTOMATION TEST REPORT</U></FONT></CENTER><br/><br/>";
			String htmlfooter = "</body></html>";
			sb.append(htmlheader);


			int count = 1;
			while (line != null) {
				line = line.replace("[main]", "");

				if (line.contains("TEST CASE NAME")){
					sb.append("<font color='#151B54'>" + "<" + "br" + "/><b>"
							+ line + "</b></font>");
				}

				else if (line.contains("TEST HAS FAILED")){
					sb.append("<font color='#F70D1A'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}
				else if (line.contains("TEST IS SUCCESSFUL")){
					sb.append("<font color='#54C571'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}

				else if (line.contains("TEST IS SKIPPED")){
					sb.append("<font color='#C71586'>" + "<" + "br" + "/><b>"
							+ line + "</b></font><br/>");
				}

				else if (line.contains("[DATABASE ASSERTION FAILURE - RodanFieldsLive")){
					sb.append("<font color='#C71586'>" + "<" + "br" + "/><b>"
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
