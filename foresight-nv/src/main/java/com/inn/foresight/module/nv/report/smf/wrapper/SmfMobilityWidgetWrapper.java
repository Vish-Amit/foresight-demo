package com.inn.foresight.module.nv.report.smf.wrapper;

import java.util.List;

public class SmfMobilityWidgetWrapper {
	
	Integer workorderId;
	List<Integer> workorderIds;
	List<MobilityLocationWrapper> floorPlan;
	
	public Integer getWorkorderId() {
		return workorderId;
	}
	public void setWorkorderId(Integer workorderId) {
		this.workorderId = workorderId;
	}
	public List<MobilityLocationWrapper> getFloorPlan() {
		return floorPlan;
	}
	public void setFloorPlan(List<MobilityLocationWrapper> floorPlan) {
		this.floorPlan = floorPlan;
	}
	
	
	public List<Integer> getWorkorderIds() {
		return workorderIds;
	}
	public void setWorkorderIds(List<Integer> workorderIds) {
		this.workorderIds = workorderIds;
	}
	
	@Override
	public String toString() {
		return "SmfMobilityWidgetWrapper [workorderId=" + workorderId + ", floorPlan=" + floorPlan + "]";
	}
	
	

}
