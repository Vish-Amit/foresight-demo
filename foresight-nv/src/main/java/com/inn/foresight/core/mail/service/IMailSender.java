package com.inn.foresight.core.mail.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.mail.utils.wrapper.mail.Attachment;

/**
 * The Interface IMailSender.
 */
public interface IMailSender {


	/**
	 * Send  mail with multiple recipient and attachments.
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
	String sendMailWithMultipleRecipientAndAttachments(String[] sendTo,
			String[] cc, String[] bcc, String subject, String message,
			List<Attachment> attachments, Map<String, String> inlineImageMap, String fromEmail);

	void sendMailWithMultipleRecipient(String[] sendTo, String[] cc, String[] bcc, String subject, String message,
			String senderMailId);

}