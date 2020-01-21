package com.inn.foresight.core.mail.service;

import static com.inn.commons.lang.StringUtils.toArray;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.lang.MapUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.mail.Mail;
import com.inn.commons.mail.MailSender;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.mail.utils.wrapper.mail.Attachment;
import com.inn.foresight.core.report.utils.ReportUtils;

/**
 * The Class MailServiceProvider.
 *
 * @author innoeye
 */

@Service("MailServiceProvider")
public class MailServiceProvider {

    /** The logger. */
    private static Logger logger = LogManager.getLogger(MailServiceProvider.class);

    /** The mail sender. */
    @Autowired(required=false)
    private MailSender mailSender;

    /**
     * The Class BufferedDataSource.
     */
    public class BufferedDataSource implements DataSource {

        /** The data. */
        private byte[] _data;
        
        /** The name. */
        private java.lang.String _name;

        /**
         * Instantiates a new buffered data source.
         *
         * @param data the data
         * @param name the name
         */
        public BufferedDataSource(byte[] data, String name) {
            _data = data;
            _name = name;
        }

        /**
         * Gets the content type.
         *
         * @return the content type
         */
        @Override
        public String getContentType() {
            return "application/octet-stream";
        }

        /**
         * Gets the input stream.
         *
         * @return the input stream
         * @throws IOException Signals that an I/O exception has occurred.
         */
        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(_data);
        }

        /**
         * Gets the name.
         *
         * @return the name
         */
        @Override
        public String getName() {
            return _name;
        }

        /**
         * Returns an OutputStream from the DataSource.
         *
         * @return the output stream
         * @throws IOException Signals that an I/O exception has occurred.
         * @returns OutputStream Array of bytes converted into an OutputStream
         */
        @Override
        public OutputStream getOutputStream() throws IOException {
            OutputStream out = new ByteArrayOutputStream();
            out.write(_data);
            return out;
        }
    }

    //786 using
    /**
     * Send mail.
     *
     * @param from - Mail of sender, mandatory option
     * @param subject - Subject of mail, mandatory option
     * @param message - Message of mail, mandatory option
     * @param to - List of TO recipients
     * @param cc - List of CC recipients
     * @param bcc - List of BCC recipients
     * @param attachments - List of attachment
     * @param inlines - List of inline images
     * @return true if mail send successfully else false
     */
    public boolean sendMail(String from, String subject, String message, String[] to, String[] cc, String[] bcc,
            DataSource[] attachments, Map<String, String> inlines) {
        try {
            Validate.checkNotEmpty(from, "sender's mail should not be empty or null");
            Validate.checkNotEmpty(subject, "Subject should not be empty or null");
            Validate.checkNotEmpty(message, "Message should not be empty or null");

            Mail mail = mailSender.create();
            mail.setFrom(from);
            mail.setSubject(subject);
            mail.setText(message, true);
            mail.setSentDate(new Date());

            if (ArrayUtils.isNotEmpty(to)) {
                mail.setTo(toArray(to));
            }
            if (ArrayUtils.isNotEmpty(cc)) {
                mail.setCc(toArray(cc));
            }
            if (ArrayUtils.isNotEmpty(bcc)) {
                mail.setBcc(toArray(bcc));
            }
            if (ArrayUtils.isNotEmpty(attachments)) {
                for (DataSource attachment : attachments) {
                    if (StringUtils.isNotEmpty(attachment.getName()) && attachment != null) {
                        mail.addAttachment(attachment.getName(), attachment);
                    }
                }
            }
            if (MapUtils.isNotEmpty(inlines)) {
                for (Entry<String, String> inline : inlines.entrySet()) {
                    if (StringUtils.isNoneEmpty(inline.getKey(), inline.getValue())) {
                        mail.addInline(inline.getKey(), new File(inline.getValue()));
                    }
                }
            }

            mailSender.send(mail);
            return true;
        } catch (Exception e) {
            logger.error("error in sending email due to {}", ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    //786
    /**
     * Send mail.
     *
     * @param from - Mail of sender, mandatory option
     * @param subject - Subject of mail, mandatory option
     * @param message - Message of mail, mandatory option
     * @param to - List of TO recipients
     * @param cc - List of CC recipients
     * @param bcc - List of BCC recipients
     * @return true if mail send successfully else false
     */
    public boolean sendMail(String from, String subject, String message, String[] to, String[] cc, String[] bcc) {
        return sendMail(from, subject, message, to, cc, bcc, (DataSource[]) null, null);
    }

    //786
    /**
     * Send  mail with multiple recipient.
     *
     * @param sendTo the send to
     * @param cc the cc
     * @param bcc the bcc
     * @param subject the subject
     * @param message the message
     * @param senderMailId the sender mail id
     */
    public void sendMailWithMultipleRecipient(String[] sendTo, String[] cc, String[] bcc, String subject,
            String message, String senderMailId) {
        String sender = senderMailId;
        if (senderMailId == null) {
            sender = AESUtils.decrypt(ConfigUtils.getString(ConfigUtil.EMAIL_ID));
        }
        sendMail(sender, subject, message, sendTo, cc, bcc);
    }

    /**
     * Send  mail with multiple recipient and attachments.
     *
     * @param sendTo the send to
     * @param ccArr the cc arr
     * @param bcc the bcc
     * @param subject the subject
     * @param message the message
     * @param attachments the attachments
     * @param inlineImageMap the inline image map
     * @param fromEmail the from email
     * @return the string
     * @throws RestException the rest exception
     * @deprecated replaced by
     *             {@link #sendMail(String, String, String, String[], String[], String[], DataSource[], Map)}
     */
    @Deprecated
    public String sendMailWithMultipleRecipientAndAttachments(String[] sendTo, String[] ccArr, String[] bcc,
            String subject, String message, List<Attachment> attachments, Map<String, String> inlineImageMap,
            String fromEmail) {
        String sender = fromEmail;
        if (fromEmail == null) {
            sender = AESUtils.decrypt(ConfigUtils.getString(ConfigUtil.EMAIL_ID));
        }
        DataSource[] attachmentSources = new DataSource[attachments.size()];
        for (int i = 0; i < attachments.size(); i++) {
            Attachment attachment = attachments.get(i);
            ByteArrayDataSource source = new ByteArrayDataSource(attachment.getAttachmentFile(),
                    attachment.getType().getValue());
            if (StringUtils.isNotEmpty(attachment.getFileName())) {
            	source.setName(attachment.getFileName());
            }
            attachmentSources[i] = source;
        }

        Map<String, String> inlines = new HashMap<>();
        if (inlineImageMap != null && inlineImageMap.size() > 0) {
            Set<String> setImageID = inlineImageMap.keySet();
            logger.info("setImageID.size {}", setImageID.size());
            for (String contentId : setImageID) {
                String imageFilePath = ReportUtils.getAbsoluteFilePath(inlineImageMap.get(contentId));
                logger.info("Setting the image for email Path {} : {} ", contentId, imageFilePath);
                inlines.put(contentId, imageFilePath);
            }
        }
        logger.info("inline map : {}",inlines);
        boolean success = sendMail(sender, subject, message, sendTo, ccArr, bcc, attachmentSources, inlines);
        if (success) {
            return ForesightConstants.SUCCESS;
        } else {
            logger.error("Error in sending mail, please check logs");
            throw new RestException("Mail could not be sent");
        }
    }
}