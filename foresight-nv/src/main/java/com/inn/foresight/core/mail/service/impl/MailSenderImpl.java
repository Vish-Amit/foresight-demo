package com.inn.foresight.core.mail.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.mail.service.IMailSender;
import com.inn.foresight.core.mail.service.MailServiceProvider;
import com.inn.foresight.core.mail.utils.wrapper.mail.Attachment;

/**
 * The Class MailSenderImpl.
 */
@Service("mailSenderService")
public class MailSenderImpl implements IMailSender {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(IMailSender.class);

	/** The mail service. */
	@Autowired
	private MailServiceProvider mailService;
	
	/**
	 * Send mail with multiple recipient and attachments.
	 *
	 * @param sendTo the send to
	 * @param cc the cc
	 * @param bcc the bcc
	 * @param subject the subject
	 * @param message the message
	 * @param attachments the attachments
	 * @param inlineImageMap the inline image map
	 * @param fromEmail the from email
	 * @return the string
	 * @throws RestException the rest exception
	 */
	 @Override
	public String sendMailWithMultipleRecipientAndAttachments(String[] sendTo, String[] cc, String[] bcc, String subject, String message, List<Attachment> attachments,
			Map<String, String> inlineImageMap, String fromEmail) {
		 return mailService.sendMailWithMultipleRecipientAndAttachments(sendTo, cc, bcc, subject, message, attachments, inlineImageMap, fromEmail);
	}
	 
	@Override
	public void sendMailWithMultipleRecipient(String[] sendTo, String[] cc, String[] bcc, String subject, String message, String senderMailId) {
		mailService.sendMailWithMultipleRecipient(sendTo, cc, bcc, subject, message, senderMailId);
	}

}