package com.inn.foresight.core.subscriber.utils.wrapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.inn.foresight.core.subscriber.utils.SubscriberHistoryConstants;

public class SubscriberHistoryWrapper {

	private Long totalSubscriber;
	private String subscriberNo;
	private String searchBy;
	private String searchDateTime;
	private String executiveRole;

	public SubscriberHistoryWrapper(String subscriberNo, String searchBy, Date searchDateTime, Long totalSubscriber, String executiveRole) {
		super();
		this.totalSubscriber = totalSubscriber;
		this.subscriberNo = subscriberNo;
		this.searchBy = searchBy;
		this.searchDateTime = searchDateTime != null ? convertDateToString(searchDateTime, SubscriberHistoryConstants.DD_MMM_YYYY) : null;
		this.executiveRole=executiveRole;
	}

	public Long getTotalSubscriber() {
		return totalSubscriber;
	}

	public void setTotalSubscriber(Long totalSubscriber) {
		this.totalSubscriber = totalSubscriber;
	}

	public String getSubscriberNo() {
		return subscriberNo;
	}

	public void setSubscriberNo(String subscriberNo) {
		this.subscriberNo = subscriberNo;
	}

	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public String getSearchDateTime() {
		return searchDateTime;
	}

	public void setSearchDateTime(String searchDateTime) {
		this.searchDateTime = searchDateTime;
	}

	private String convertDateToString(Date convertDate, String format) {
		return new SimpleDateFormat(format).format(convertDate);
	}

	public String getExecutiveRole() {
		return executiveRole;
	}

	public void setExecutiveRole(String executiveRole) {
		this.executiveRole = executiveRole;
	}

	@Override
	public String toString() {
		return "SubscriberHistoryWrapper [totalSubscriber=" + totalSubscriber + ", subscriberNo=" + subscriberNo + ", searchBy=" + searchBy + ", searchDateTime=" + searchDateTime + ", executiveRole="
				+ executiveRole + "]";
	}

}
