package com.inn.foresight.module.nv.nps.model;


/** The Class NetPromoterWrapper. */
public class NetPromoterWrapper {
	
	
	/**
	 * Instantiates a new net promoter wrapper.
	 *
	 * @param geographyId the geography id
	 * @param geographyType the geography type
	 * @param processdate the processdate
	 * @param callType the call type
	 * @param technology the technology
	 * @param customerType the customer type
	 * @param eventType the event type
	 * @param customerCount the customer count
	 * @param kpi the kpi
	 * @param kpiSum the kpi sum
	 * @param ratingSum the rating sum
	 */
	public NetPromoterWrapper(Integer geographyId, String geographyType, String processdate, String callType,
			String technology, String customerType, String eventType, Long customerCount, String kpi, Double kpiSum,
			Double ratingSum) {
		super();
		this.geographyId = geographyId;
		this.geographyType = geographyType;
		this.processdate = processdate;
		this.callType = callType;
		this.technology = technology;
		this.customerType = customerType;
		this.eventType = eventType;
		this.customerCount = customerCount;
		this.kpi = kpi;
		this.kpiSum = kpiSum;
		this.ratingSum = ratingSum;
	}
	
	
	/**
	 * Instantiates a new net promoter wrapper.
	 *
	 * @param geographyType the geography type
	 * @param processdate the processdate
	 * @param weekno the weekno
	 * @param operator the operator
	 * @param technology the technology
	 */
	public NetPromoterWrapper(String geographyType, String processdate, Integer weekno, String operator, String technology) {
		this.geographyType = geographyType;
		this.processdate = processdate;
		this.operator = operator;
		this.technology = technology;
	}
	
	/**
	 * Instantiates a new net promoter wrapper.
	 *
	 * @param customerType the customer type
	 * @param operator the operator
	 * @param processdate the processdate
	 * @param customercount the customercount
	 */
	public NetPromoterWrapper(String customerType, String operator, String processdate, Long customercount) {
		this.customerType = customerType;
		this.operator = operator;
		this.processdate = processdate;
		this.customerCount = customercount;
	}
	
	public NetPromoterWrapper( String customerType,String processdate ,Long customerCount, Double kpiSum) {
		this.customerType = customerType;
		this.processdate = processdate;
		this.customerCount = customerCount;
		this.kpiSum = kpiSum;
	}

	
	
	
	/**
	 * Instantiates a new net promoter wrapper.
	 *
	 * @param customerType the customer type
	 * @param customerCount the customer count
	 */
	public NetPromoterWrapper(String customerType , Long customerCount) {
		this.customerType = customerType;
		this.customerCount = customerCount;
	}
	
	/**
	 * Instantiates a new net promoter wrapper.
	 *
	 * @param customerType the customer type
	 * @param eventType the event type
	 * @param customerCount the customer count
	 */
	public NetPromoterWrapper(String customerType, String eventType , Long customerCount) {
		this.customerType = customerType;
		this.eventType = eventType;
		this.customerCount = customerCount;
	}
	
	/**
	 * Instantiates a new net promoter wrapper.
	 *
	 * @param customerCount the customer count
	 * @param customerType the customer type
	 * @param processdate the processdate
	 * @param operator the operator
	 */
	public NetPromoterWrapper( Long customerCount, String customerType, String processdate, String operator) {
		this.customerCount = customerCount;
		this.customerType = customerType;
		this.processdate = processdate;
		this.operator = operator;
	}
	
	public NetPromoterWrapper(Double average,Long customerCount, String customerType, String processdate) {
	
		this.average = average;
		this.customerCount = customerCount;
		this.customerType = customerType;
		this.processdate = processdate;
		
	}
	
	public NetPromoterWrapper(Double average,Long customerCount, String customerType, Integer weekno) {
		
		this.average = average;
		this.customerCount = customerCount;
		this.customerType = customerType;
		this.weekno = weekno;
		
	}
	
	public NetPromoterWrapper(Double average,Long customerCount, String customerType) {
		
		this.average = average;
		this.customerCount = customerCount;
		this.customerType = customerType;
	}
	
	public NetPromoterWrapper(Long customerCount, String customerType, String processdate) {
		
		this.customerCount = customerCount;
		this.customerType = customerType;
		this.processdate = processdate;
		
	}
	
	
	public NetPromoterWrapper( String customerType,Integer weekno ,Long customerCount, Double kpiSum) {
		this.customerType = customerType;
		this.weekno = weekno;
		this.customerCount = customerCount;
		this.kpiSum = kpiSum;
	}
	/** The geography id. */
	private Integer geographyId;
	 
	
	/** The geography type. */
	private String geographyType;

	
	/** The processdate. */
	private String processdate;

	
	/** The call type. */
	private String callType;

	/** The technology. */
	private String technology;

	/** The customer type. */
	private String customerType;
	
	/** The event type. */
	private String eventType;
	
	/** The customer count. */
	private Long customerCount;
	
	/** The kpi. */
	private String kpi;
	
	/** The operator. */
	private String operator;
	
	/** The kpi sum. */
	private Double kpiSum;
	
	/** The rating sum. */
	private Double ratingSum;
	
	/** The weekno. */
	private Integer weekno;
	
	private Double average;

	public Double getAverage() {
		return average;
	}


	public void setAverage(Double average) {
		this.average = average;
	}


	/**
	 * Gets the geography id.
	 *
	 * @return the geography id
	 */
	public Integer getGeographyId() {
		return geographyId;
	}

	/**
	 * Sets the geography id.
	 *
	 * @param geographyId the new geography id
	 */
	public void setGeographyId(Integer geographyId) {
		this.geographyId = geographyId;
	}

	/**
	 * Gets the geography type.
	 *
	 * @return the geography type
	 */
	public String getGeographyType() {
		return geographyType;
	}

	/**
	 * Sets the geography type.
	 *
	 * @param geographyType the new geography type
	 */
	public void setGeographyType(String geographyType) {
		this.geographyType = geographyType;
	}

	/**
	 * Gets the process date.
	 *
	 * @return the process date
	 */
	public String getProcessDate() {
		return processdate;
	}

	/**
	 * Sets the process date.
	 *
	 * @param processdate the new process date
	 */
	public void setProcessDate(String processdate) {
		this.processdate = processdate;
	}

	/**
	 * Gets the call type.
	 *
	 * @return the call type
	 */
	public String getCallType() {
		return callType;
	}

	/**
	 * Sets the call type.
	 *
	 * @param callType the new call type
	 */
	public void setCallType(String callType) {
		this.callType = callType;
	}

	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the customer type.
	 *
	 * @return the customer type
	 */
	public String getCustomerType() {
		return customerType;
	}

	/**
	 * Sets the customer type.
	 *
	 * @param customerType the new customer type
	 */
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	/**
	 * Gets the event type.
	 *
	 * @return the event type
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * Sets the event type.
	 *
	 * @param eventType the new event type
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * Gets the customer count.
	 *
	 * @return the customer count
	 */
	public Long getCustomerCount() {
		return customerCount;
	}

	/**
	 * Sets the customer count.
	 *
	 * @param customerCount the new customer count
	 */
	public void setCustomerCount(Long customerCount) {
		this.customerCount = customerCount;
	}

	/**
	 * Gets the kpi.
	 *
	 * @return the kpi
	 */
	public String getKpi() {
		return kpi;
	}

	/**
	 * Sets the kpi.
	 *
	 * @param kpi the new kpi
	 */
	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	/**
	 * Gets the kpi sum.
	 *
	 * @return the kpi sum
	 */
	public Double getKpiSum() {
		return kpiSum;
	}

	/**
	 * Sets the kpi sum.
	 *
	 * @param kpiSum the new kpi sum
	 */
	public void setKpiSum(Double kpiSum) {
		this.kpiSum = kpiSum;
	}
	
	/**
	 * Gets the rating sum.
	 *
	 * @return the rating sum
	 */
	public Double getRatingSum() {
		return ratingSum;
	}

	/**
	 * Sets the rating sum.
	 *
	 * @param ratingSum the new rating sum
	 */
	public void setRatingSum(Double ratingSum) {
		this.ratingSum = ratingSum;
	}
	
	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	/**
	 * Gets the week no.
	 *
	 * @return the week no
	 */
	public Integer getweekNo() {
		return weekno;
	}

	/**
	 * Sets the week no.
	 *
	 * @param weekno the new week no
	 */
	public void setweekNo(Integer weekno) {
		this.weekno = weekno;
	}


	@Override
	public String toString() {
		return "NetPromoterWrapper [geographyId=" + geographyId + ", geographyType=" + geographyType + ", processdate="
				+ processdate + ", callType=" + callType + ", technology=" + technology + ", customerType="
				+ customerType + ", eventType=" + eventType + ", customerCount=" + customerCount + ", kpi=" + kpi
				+ ", operator=" + operator + ", kpiSum=" + kpiSum + ", ratingSum=" + ratingSum + ", weekno=" + weekno
				+ ", average=" + average + "]";
	}


	
}
