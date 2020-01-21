package com.inn.foresight.core.subscriber.utils;

import javax.persistence.Query;

public class SubscriberInfoUtils {

	private SubscriberInfoUtils(){
		super();
	}

	public static void setPaginationInQuery(Integer llimit, Integer ulimit, Query query) {
		if (llimit != null && ulimit != null && llimit >= 0 && ulimit > 0) {
			query.setMaxResults(ulimit - llimit + 1);
			query.setFirstResult(llimit);
		}
	}
}
