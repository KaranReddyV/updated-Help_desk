package com.ojas.ticket.transfer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


public class SendMail {

	@Autowired
	private JavaMailSender javaMailSender;

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	public void sendMail(String to, String subject, String msg) throws MessagingException { // creating
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
		String content = "<h2>This ticket is raised by Ojas It Ticketing Application</h2>";
		mimeMessage.setContent(content, "text/html");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(msg, true);
		javaMailSender.send(mimeMessage);

	}
}
