package com.inn.foresight.core.mail.service.impl;

import org.springframework.stereotype.Service;

import com.inn.foresight.core.mail.service.ISendQueueMailService;

/**
 * The Class EmailNotificationServiceImpl.
 * @author innoeye
 */
@Service("SendQueueMailServiceImpl")
public class SendQueueMailServiceImpl implements ISendQueueMailService{
	
	/**
	 * 
	 * Send queue mail.
	 */
	@Override
	 public void sendQueueMail(){
		/*try {
			List<EmailNotification> mailsToSend = sentnotificationsDao.getQueueDataToMail(NotificationStatus.QUEUE);
			logger.info("@sendQueueMail mail sending started at: {} for list size: {}", new Date(), mailsToSend.size());
			if(mailsToSend!=null && !mailsToSend.isEmpty()){
				List<Long> notificationIds=new ArrayList<Long>();
				for(EmailNotification obj:mailsToSend){
					notificationIds.add(obj.getNotificationId());
				}
				Collection<Future<?>> futures = new LinkedList<Future<?>>();
				for(final EmailNotification sentNotification : mailsToSend){
					futures.add(sendQueueMailExecutor.submit(new Runnable() {
						@Override
						public void run() {
							notificationService.sendQueueMailAsync(sentNotification);
						}
					}));
				}
				// wait for task to complete
				for (Future<?> future : futures) {
					future.get();
				}
			}
		} catch (Exception e) {
			logger.error("@sendQueueMail getting Exception as:{}", Utils.getStackTrace(e));
		}*/
	}


}
