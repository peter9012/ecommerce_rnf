/**
 * 
 */
package com.rf.core.utils;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Admin
 *
 */
public class EmailUtil {

	Folder inbox;
	Session session;
	Store store;
	Message messages[];
	/**
	 * @throws MessagingException 
	 * 
	 */
	public EmailUtil(String userEmailId,String emailPwd) throws MessagingException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		props.put("mail.imaps.ssl.trust", "*");
		/*  Create the session and get the store for read the mail. */
		session = Session.getDefaultInstance(props, null);
		store = session.getStore("imaps");
		store.connect("imap.gmail.com",userEmailId, emailPwd);
		/*  Mention the folder name which you want to read. */
		inbox = store.getFolder("Inbox");
		/*Open the inbox using store.*/
		inbox.open(Folder.READ_WRITE);
	}

	//	private Message _fetchAllUnReadMessages() throws MessagingException
	//	{
	//		messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
	//		/* Use a suitable FetchProfile    */
	//		FetchProfile fp = new FetchProfile();
	//		fp.add(FetchProfile.Item.ENVELOPE);
	//		fp.add(FetchProfile.Item.CONTENT_INFO);
	//		inbox.fetch(messages, fp);
	//		try
	//		{
	//			//_printAllMessages(messages);
	//			inbox.close(true);
	//			store.close();
	//		}
	//		catch (Exception ex)
	//		{
	//			System.out.println("Exception arise at the time of read mail");
	//			ex.printStackTrace();
	//		}
	//		return null;
	//	}

	public String getEmailFrom(String sFrom) throws Exception
	{
		messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));
		/* Use a suitable FetchProfile    */
		FetchProfile fp = new FetchProfile();
		fp.add(FetchProfile.Item.ENVELOPE);
		fp.add(FetchProfile.Item.CONTENT_INFO);
		inbox.fetch(messages, fp);
		String fromEmail="";
		String content="";
		int iMessagesCount= messages.length;
		if(iMessagesCount!=0)
		{
			for (int i = 0; i < iMessagesCount; i++)
			{
				Message message = messages[i];
				fromEmail= message.getFrom()[0].toString();
				if(fromEmail.equalsIgnoreCase(sFrom))
				{
					System.out.println("Match Found");
					String contentType = message.getContentType();
					content=message.getContent().toString(); 
					if(contentType.contains("multipart"))
					{
						Multipart multipart = (Multipart) message.getContent();

						for (int j = 0; j < multipart.getCount(); j++) {
							BodyPart bodyPart = multipart.getBodyPart(j);
							String disposition = bodyPart.getDisposition();
							if (disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) { // BodyPart.ATTACHMENT doesn't work for gmail
								//System.out.println("Mail have some attachment");
								DataHandler handler = bodyPart.getDataHandler();
								System.out.println("file name : " + handler.getName());                                 
							}
							else { 
								//System.out.println("Body: "+bodyPart.getContent());
								content= bodyPart.getContent().toString();
							}
						}

					}
				}
				message.setFlag(Flags.Flag.SEEN	, true);
			}
		}
		else{
			throw new Exception("No Message Found !!!");
		}

		return content;
	}

	public String getResetLink(String content)
	{
		Document doc = Jsoup.parse(content);
		Element linkTag = doc.select("a").first();
		String link = linkTag.attr("href");
		return link;
	}

}
