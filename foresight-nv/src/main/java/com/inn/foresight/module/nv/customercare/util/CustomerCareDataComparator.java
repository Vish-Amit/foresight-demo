package com.inn.foresight.module.nv.customercare.util;

import java.util.Comparator;

import com.inn.foresight.module.nv.customercare.wrapper.NVCustomerCareDataWrapper;

public class CustomerCareDataComparator implements Comparator<NVCustomerCareDataWrapper> {
	public int compare(NVCustomerCareDataWrapper s1, NVCustomerCareDataWrapper s2) {
		if (s1.getCapturedOn() != null && s2.getCapturedOn() != null) {
			return s2	.getCapturedOn()
						.compareTo(s1.getCapturedOn());
		} else {
			return -1;
		}
	}
}