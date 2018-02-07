package com.demo.springmvc.controller;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.demo.springmvc.entity.EmailInfo;

@Controller
public class EmailController {
	@Autowired
	ServletContext context;
	@Autowired
	JavaMailSender mailSender;

	@RequestMapping(value = "input", method = RequestMethod.GET)
	public String showForm(ModelMap model) {
		model.addAttribute("mail", new EmailInfo());
		return "AttachEmailInput";
	}

	@RequestMapping(value = "send", method = RequestMethod.POST)
	public String sendWithAttach(ModelMap model,
			@ModelAttribute("mail") EmailInfo mailInfo) {
		try {
			final JavaMailSenderImpl ms = (JavaMailSenderImpl) mailSender;
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			//get only username to show
			String frEmail = ms.getUsername();
			mailInfo.setFrom(frEmail);
			
			//do not need to setFrom so set in servlet-gmail.xml file 
			//set name of account email if want to show name instead account
			helper.setFrom(new InternetAddress(null, "Company"));			
			helper.setTo(mailInfo.getTo());
			//helper.setReplyTo(mailInfo.getFrom()); //if any
			helper.setSubject(mailInfo.getSubject());
			helper.setText(mailInfo.getBody(), true);			
			mailSender.send(message);
		} catch (Exception ex) {
			model.addAttribute("error", ex.getMessage());
			return "AttachEmailInput";
		}
		return "AttachEmailSuccess";
	}

}