package com.inn.foresight.module.nv.report.wrapper.inbuilding;

import java.util.List;

import com.inn.foresight.core.infra.model.Floor;
import com.inn.foresight.core.infra.model.Wing;
import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class NVIBResultUnitWrapper {
	Integer id;
	Long count;
	String name;
	Double latitude;
	Double longitude;
	Double rsrp;
	Double rsrpSum;
	Long rsrpCount;
	Double rssi;
	Double sinr;
	Double sinrSum;
	Long sinrCount;
	Double dl;
	Double dlSum;
	Long dlCount;
	Double ul;
	Double ulSum;
	Long ulCount;
	Double snr;
	String operator;
	Integer workOrderId;
	Long hoInitiate;
	Long hoSuccess;
	Long hoFailure;

	Long callInitate;
	Long callSuccess;
	Long callFailure;
	Long callDrop;
	Long callSetupSuccess;
	Double pdschDlSum;
	Long pdschDlCount;
	Double puschUlSum;
	Long puschUlCount;
	Double mosSum;
	Long mosCount;

	Floor floor;
	Wing wing;
	List<NVIBResultUnitWrapper> list;

	public NVIBResultUnitWrapper(Integer id, Long count, String name, Double latitude, Double longitude, Double rsrp,
			Double rssi, Double sinr, Double dl, Double ul, Double snr, String operator, Integer workOrderId,
			Long hoInitiate, Long hoSuccess, Long hoFailure, Long callInitate, Floor floor, Wing wing) {
		super();
		this.id = id;
		this.count = count;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.rsrp = rsrp;
		this.rssi = rssi;
		this.sinr = sinr;
		this.dl = dl;
		this.ul = ul;
		this.snr = snr;
		this.operator = operator;
		this.workOrderId = workOrderId;
		this.hoInitiate = hoInitiate;
		this.hoSuccess = hoSuccess;
		this.hoFailure = hoFailure;
		this.callInitate = callInitate;
		this.floor = floor;
		this.wing = wing;
	}


	/** Constructor for getWingLevelWiseIBUnitResult. */
	public NVIBResultUnitWrapper(Integer id, String floorNumber, Long count, Double latitude, Double longitude,
			Double dlSum, Double ulSum, Double rsrpSum, Double sinrSum, Long dlCount, Long ulCount, Long rsrpCount,
			Long sinrCount, Double snr, Long hoFailure, Long hoInitiate, Long hoSuccess, Wing wing,
			List<NVIBResultUnitWrapper> list, String operator, Long callInitate, Long callSuccess, Long callFailure,
			Long callDrop, Long callSetupSuccess, Double pdschDlSum, Long pdschDlCount, Double puschUlSum,
			Long puschUlCount, Double mosSum, Long mosCount) {
		super();
		this.id = id;
		this.name = floorNumber;
		this.count = count;
		this.latitude = latitude;
		this.longitude = longitude;
		this.dlSum = dlSum;
		this.ulSum = ulSum;
		this.rsrpSum = rsrpSum;
		this.sinrSum = sinrSum;
		this.dlCount = dlCount;
		this.ulCount = ulCount;
		this.rsrpCount = rsrpCount;
		this.sinrCount = sinrCount;
		this.snr = snr;
		this.hoFailure = hoFailure;
		this.hoInitiate = hoInitiate;
		this.hoSuccess = hoSuccess;
		this.wing = wing;
		this.list = list;
		this.operator = operator;

		this.callInitate = callInitate;
		this.callSuccess = callSuccess;
		this.callFailure = callFailure;
		this.callDrop = callDrop;
		this.callSetupSuccess = callSetupSuccess;
		this.pdschDlSum = pdschDlSum;
		this.pdschDlCount = pdschDlCount;
		this.puschUlSum = puschUlSum;
		this.puschUlCount = puschUlCount;
		this.mosSum = mosSum;
		this.mosCount = mosCount;
	}

	/** Constructor for getWingLevelWiseIBUnitResult. */
	public NVIBResultUnitWrapper(Integer id, String name, Double rsrpSum, Double rssi, Double sinrSum, Double dlSum,
			Double ulSum, Long rsrpCount, Long rssiCount, Long sinrCount, Long dlCount, Long ulCount, String operator,
			Long handOverInitiate, Long handOverSuccess, Integer workorderId, Double latitude, Double longitude,
			Floor floor, Wing wing, Long callInitate, Long callSuccess, Long callFailure, Long callDrop,
			Long callSetupSuccess, Double pdschDlSum, Long pdschDlCount, Double puschUlSum, Long puschUlCount,
			Double mosSum, Long mosCount) {
		super();
		this.id = id;
		this.name = name;
		this.rsrpSum = rsrpSum;
		this.sinrSum = sinrSum;
		this.dlSum = dlSum;
		this.ulSum = ulSum;
		this.rsrpCount = rsrpCount;
		this.sinrCount = sinrCount;
		this.dlCount = dlCount;
		this.ulCount = ulCount;
		this.operator = operator;
		this.hoInitiate = handOverInitiate;
		this.hoSuccess = handOverSuccess;
		this.latitude = latitude;
		this.longitude = longitude;
		this.workOrderId = workorderId;
		this.floor = floor;
		this.wing = wing;

		this.callInitate = callInitate;
		this.callSuccess = callSuccess;
		this.callFailure = callFailure;
		this.callDrop = callDrop;
		this.callSetupSuccess = callSetupSuccess;
		this.pdschDlSum = pdschDlSum;
		this.pdschDlCount = pdschDlCount;
		this.puschUlSum = puschUlSum;
		this.puschUlCount = puschUlCount;
		this.mosSum = mosSum;
		this.mosCount = mosCount;
	}

	public NVIBResultUnitWrapper() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Double getRsrp() {
		return rsrp;
	}

	public void setRsrp(Double rsrp) {
		this.rsrp = rsrp;
	}

	public Double getRssi() {
		return rssi;
	}

	public void setRssi(Double rssi) {
		this.rssi = rssi;
	}

	public Double getSinr() {
		return sinr;
	}

	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}

	public Double getDl() {
		return dl;
	}

	public void setDl(Double dl) {
		this.dl = dl;
	}

	public Double getUl() {
		return ul;
	}

	public void setUl(Double ul) {
		this.ul = ul;
	}

	public Double getSnr() {
		return snr;
	}

	public void setSnr(Double snr) {
		this.snr = snr;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Integer workOrderId) {
		this.workOrderId = workOrderId;
	}

	public Long getHoSuccess() {
		return hoSuccess;
	}

	public void setHoSuccess(Long hoSuccess) {
		this.hoSuccess = hoSuccess;
	}

	public Long getHoInitiate() {
		return hoInitiate;
	}

	public void setHoInitiate(Long hoInitiate) {
		this.hoInitiate = hoInitiate;
	}

	public Long getHoFailure() {
		return hoFailure;
	}

	public void setHoFailure(Long hoFailure) {
		this.hoFailure = hoFailure;
	}

	public Long getCallInitate() {
		return callInitate;
	}

	public void setCallInitate(Long callInitate) {
		this.callInitate = callInitate;
	}

	public Long getCallSuccess() {
		return callSuccess;
	}

	public void setCallSuccess(Long callSuccess) {
		this.callSuccess = callSuccess;
	}

	public Long getCallFailure() {
		return callFailure;
	}

	public void setCallFailure(Long callFailure) {
		this.callFailure = callFailure;
	}

	public Long getCallDrop() {
		return callDrop;
	}

	public void setCallDrop(Long callDrop) {
		this.callDrop = callDrop;
	}

	public void setCallSetupSuccess(Long callSetupSuccess) {
		this.callSetupSuccess = callSetupSuccess;
	}

	public List<NVIBResultUnitWrapper> getList() {
		return list;
	}

	public void setList(List<NVIBResultUnitWrapper> list) {
		this.list = list;
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public Wing getWing() {
		return wing;
	}

	public void setWing(Wing wing) {
		this.wing = wing;
	}

	public Long getRsrpCount() {
		return rsrpCount;
	}

	public void setRsrpCount(Long rsrpCount) {
		this.rsrpCount = rsrpCount;
	}

	public Long getSinrCount() {
		return sinrCount;
	}

	public void setSinrCount(Long sinrCount) {
		this.sinrCount = sinrCount;
	}

	public Double getRsrpSum() {
		return rsrpSum;
	}

	public void setRsrpSum(Double rsrpSum) {
		this.rsrpSum = rsrpSum;
	}

	public Double getSinrSum() {
		return sinrSum;
	}

	public void setSinrSum(Double sinrSum) {
		this.sinrSum = sinrSum;
	}

	public Double getDlSum() {
		return dlSum;
	}

	public void setDlSum(Double dlSum) {
		this.dlSum = dlSum;
	}

	public Long getDlCount() {
		return dlCount;
	}

	public void setDlCount(Long dlCount) {
		this.dlCount = dlCount;
	}

	public Double getUlSum() {
		return ulSum;
	}

	public void setUlSum(Double ulSum) {
		this.ulSum = ulSum;
	}

	public Long getUlCount() {
		return ulCount;
	}

	public void setUlCount(Long ulCount) {
		this.ulCount = ulCount;
	}

	public Long getCallSetupSuccess() {
		return callSetupSuccess;
	}

	public Double getPdschDlSum() {
		return pdschDlSum;
	}

	public void setPdschDlSum(Double pdschDlSum) {
		this.pdschDlSum = pdschDlSum;
	}

	public Long getPdschDlCount() {
		return pdschDlCount;
	}

	public void setPdschDlCount(Long pdschDlCount) {
		this.pdschDlCount = pdschDlCount;
	}

	public Double getPuschUlSum() {
		return puschUlSum;
	}

	public void setPuschUlSum(Double puschUlSum) {
		this.puschUlSum = puschUlSum;
	}

	public Long getPuschUlCount() {
		return puschUlCount;
	}

	public void setPuschUlCount(Long puschUlCount) {
		this.puschUlCount = puschUlCount;
	}

	public Double getMosSum() {
		return mosSum;
	}

	public void setMosSum(Double mosSum) {
		this.mosSum = mosSum;
	}

	public Long getMosCount() {
		return mosCount;
	}

	public void setMosCount(Long mosCount) {
		this.mosCount = mosCount;
	}

	

}
