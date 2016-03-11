package com.randf.crm;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.randf.core.utils.PropertyFile;

public class SendMail {	

	static String maildefaultProps = "mailDefaultEnv.properties";
	
	public static void sendMail(String BodyMsg)throws MessagingException
	{
				
		PropertyFile mailProps = new PropertyFile();
		Properties sessionProps = new Properties();		
	
		String mailTo = null;		
		String mailFrom = null;				
		
        try{
        	
        	mailProps.loadProps(maildefaultProps);
        	
        	sessionProps.put("mail.smtp.host", mailProps.getProperty("mail.smtp.host"));
        	//sessionProps.put("mail.smtp.port", mailProps.getProperty("mail.smtp.port"));
        	sessionProps.put("mail.smtp.starttls.enable", mailProps.getProperty("mail.smtp.starttls.enable"));
        	sessionProps.put("mail.smtp.auth", mailProps.getProperty("mail.smtp.auth"));
        	
        	mailTo =  mailProps.getProperty("mail.to");
        	mailFrom =  mailProps.getProperty("mail.from");
        	
        	final String username = mailProps.getProperty("mail.username");
        	final String pwd = mailProps.getProperty("mail.pwd");
        	
        	
        Authenticator auth =  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, pwd);
			}
		  };
		  
        Session session = Session.getDefaultInstance(sessionProps, auth);    	
    	
       MimeMessage message = new MimeMessage(session);
       message.setFrom(new InternetAddress(mailFrom));
       message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailTo));
       message.setSubject("VerifyAccountMigration - SP Execution Results");
       message.setText(BodyMsg);
       Transport.send(message);
       System.out.println("Sent message successfully !");
       
    	}catch (MessagingException mex) {
    		mex.printStackTrace();
    	}catch (Exception e){
    		e.printStackTrace();
    	}
	}

}
