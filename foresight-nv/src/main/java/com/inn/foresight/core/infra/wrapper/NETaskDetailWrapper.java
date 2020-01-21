package com.inn.foresight.core.infra.wrapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
@RestWrapper
@JpaWrapper
public class NETaskDetailWrapper {


    private String siteId;                 // 1A ,1b ,1c
	private String riuSerialNo;            // 1A 
    private String riuModelNo;             // 1A
    private Double latitude;               // 1A 
    private Double longitude;              // 1A
    private List<RRHDetailWrapper> rrhList;// 1A
	private Long startDate;                 
	private Long endDate;              
	private Long triggerTime;              //1B 
	private String taskStatus;             // 1A ,1B ,1C
	private String taskName;               // 1A ,1B ,1C
	private String completionStatus;       
	private Date actualEndDate;         
	private Integer taskDay;               
	private String neFrequency;           // 1A ,1B ,1C
	private String assignBy;
	private String assignTo;
	private Map<String,String> configurationMap;
	
	
	
	public Map<String, String> getConfigurationMap() {
		return configurationMap;
	}

	public void setConfigurationMap(Map<String, String> configurationMap) {
		this.configurationMap = configurationMap;
	}

	public NETaskDetailWrapper(String neFrequency,String taskName,String completionStatus,String taskStatus,Date actualEndDate,Integer taskDay) {
		this.neFrequency = neFrequency;
		this.taskName = taskName;
		this.completionStatus = completionStatus;
		this.taskStatus = taskStatus;
		this.actualEndDate = actualEndDate;
		this.taskDay = taskDay;
	}
	
	public NETaskDetailWrapper() {
		// TODO Auto-generated constructor stub
	}

	public String getAssignBy() {
		return assignBy;
	}

	public void setAssignBy(String assignBy) {
		this.assignBy = assignBy;
	}

	public String getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(String assignTo) {
		this.assignTo = assignTo;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getRiuSerialNo() {
		return riuSerialNo;
	}

	public void setRiuSerialNo(String riuSerialno) {
		this.riuSerialNo = riuSerialno;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Long getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(Long triggerTime) {
		this.triggerTime = triggerTime;
	}
	public String getCompletionStatus() {
		return completionStatus;
	}
	public void setCompletionStatus(String completionStatus) {
		this.completionStatus = completionStatus;
	}
	public Date getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public Integer getTaskDay() {
		return taskDay;
	}
	public void setTaskDay(Integer taskDay) {
		this.taskDay = taskDay;
	}
	public String getNeFrequency() {
		return neFrequency;
	}
	public void setNeFrequency(String neFrequency) {
		this.neFrequency = neFrequency;
	}

    public String getRiuModelNo() {
        return riuModelNo;
    }

    public void setRiuModelNo(String riuModelNo) {
        this.riuModelNo = riuModelNo;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<RRHDetailWrapper> getRrhList() {
        return rrhList;
    }

    public void setRrhList(List<RRHDetailWrapper> rrhList) {
        this.rrhList = rrhList;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NETaskDetailWrapper [siteId=");
		builder.append(siteId);
		builder.append(", riuSerialno=");
		builder.append(riuSerialNo);
		builder.append(", riuModelNo=");
		builder.append(riuModelNo);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", rrhList=");
		builder.append(rrhList);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", triggerTime=");
		builder.append(triggerTime);
		builder.append(", taskStatus=");
		builder.append(taskStatus);
		builder.append(", taskName=");
		builder.append(taskName);
		builder.append(", completionStatus=");
		builder.append(completionStatus);
		builder.append(", actualEndDate=");
		builder.append(actualEndDate);
		builder.append(", taskDay=");
		builder.append(taskDay);
		builder.append(", neFrequency=");
		builder.append(neFrequency);
		builder.append(", assignBy=");
		builder.append(assignBy);
		builder.append(", assignTo=");
		builder.append(assignTo);
		builder.append("]");
		return builder.toString();
	}

    
}
