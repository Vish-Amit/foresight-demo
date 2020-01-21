package com.inn.foresight.core.infra.wrapper;

import java.util.Date;

import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class SiteEmsDetailsWrapper.
 */
@RestWrapper

public class SiteEmsDetailsWrapper {

	/** The lsmr host name. */
	private String lsmrHostName;

	/** The lsmr ipv 6 address. */
	private String lsmrIpv6Address;

	/** The enode B id. */
	private String enodeBId;

	/** The enode state. */
	private String enodeState;

	/** The admin state. */
	private String adminState;

	/** The ems live date. */
	private String emsLiveDate;

	/** The oam vlan. */
	private String oamVlan;

	/** The lsmr id. */
	private String lsmrId;

	/** The oam ipv 6 address. */
	private String oamIpv6Address;

	/** The oam ipv 6 subnet mask. */
	private String oamIpv6SubnetMask;

	/** The oam gateway ipv 6 address. */
	private String oamGatewayIpv6Address;

	/** The oam gateway ipv 6 subnet mask. */
	private String oamGatewayIpv6SubnetMask;

	/** The signal vlan. */
	private String signalVlan;

	/** The signal mme group id. */
	private String signalMmeGroupId;

	/** The signal ipv 6 address. */
	private String signalIpv6Address;

	/** The signal ipv 6 subnet mask. */
	private String signalIpv6SubnetMask;

	/** The signal gateway ipv 6 address. */
	private String signalGatewayIpv6Address;

	/** The signal gateway ipv 6 subnet mask. */
	private String signalGatewayIpv6SubnetMask;

	/** The bearer vlan. */
	private String bearerVlan;

	/** The bearer SAEGW pri G id. */
	private String bearerSAEGWPriGId;

	/** The bearer ipv 6 address. */
	private String bearerIpv6Address;

	/** The bearer ipv 6 subnet mask. */
	private String bearerIpv6SubnetMask;

	/** The bearer gateway ipv 6 address. */
	private String bearerGatewayIpv6Address;

	/** The bearer gateway ipv 6 subnet mask. */
	private String bearerGatewayIpv6SubnetMask;

	private Integer enbId;

	private Date liveEmsTime;

	/**
	 * Instantiates a new site ems details wrapper.
	 */
	public SiteEmsDetailsWrapper() {
		super();
	}

	/**
	 * Instantiates a new site ems details wrapper.
	 *
	 * @param lsmrHostName    the lsmr host name
	 * @param lsmrIpv6Address the lsmr ipv 6 address
	 * @param adminState      the admin state
	 * @param emsLiveDate     the ems live date
	 */
	// n.emsServer.hostName,n.emsServer.ip,n.adminState,ms.liveEmsTime
	public SiteEmsDetailsWrapper(String lsmrHostName, String lsmrIpv6Address, String adminState, Date emsLiveDate) {
		super();
		this.lsmrHostName = lsmrHostName;
		this.lsmrIpv6Address = lsmrIpv6Address;
		this.adminState = adminState;
		if (emsLiveDate != null) {
			this.emsLiveDate = Utils.getStringDateByFormat(emsLiveDate, InfraConstants.DATE_MONTH_YEAR);
		}
	}

	public SiteEmsDetailsWrapper(String lsmrHostName, String lsmrIpv6Address) {
		super();
		this.lsmrHostName = lsmrHostName;
		this.lsmrIpv6Address = lsmrIpv6Address;
	}

	public SiteEmsDetailsWrapper(String lsmrHostName, String lsmrIpv6Address, String enodeBId, String enodeState,
			String adminState, Date liveEmsTime, String oamVlan, String lsmrId, String oamIpv6Address,
			String oamIpv6SubnetMask, String oamGatewayIpv6Address, String oamGatewayIpv6SubnetMask, String signalVlan,
			String signalMmeGroupId, String signalIpv6Address, String signalIpv6SubnetMask,
			String signalGatewayIpv6Address, String signalGatewayIpv6SubnetMask, String bearerVlan,
			String bearerSAEGWPriGId, String bearerIpv6Address, String bearerIpv6SubnetMask,
			String bearerGatewayIpv6Address, String bearerGatewayIpv6SubnetMask) {
		super();
		this.lsmrHostName = lsmrHostName;
		this.lsmrIpv6Address = lsmrIpv6Address;
		this.enodeBId = enodeBId;
		this.enodeState = enodeState;
		this.adminState = adminState;
		if (liveEmsTime != null) {
			this.emsLiveDate = InfraUtils.getSiteTaskDateForSectorProperty(liveEmsTime, true);
		}
		this.oamVlan = oamVlan;
		this.lsmrId = lsmrId;
		this.oamIpv6Address = oamIpv6Address;
		this.oamIpv6SubnetMask = oamIpv6SubnetMask;
		this.oamGatewayIpv6Address = oamGatewayIpv6Address;
		this.oamGatewayIpv6SubnetMask = oamGatewayIpv6SubnetMask;
		this.signalVlan = signalVlan;
		this.signalMmeGroupId = signalMmeGroupId;
		this.signalIpv6Address = signalIpv6Address;
		this.signalIpv6SubnetMask = signalIpv6SubnetMask;
		this.signalGatewayIpv6Address = signalGatewayIpv6Address;
		this.signalGatewayIpv6SubnetMask = signalGatewayIpv6SubnetMask;
		this.bearerVlan = bearerVlan;
		this.bearerSAEGWPriGId = bearerSAEGWPriGId;
		this.bearerIpv6Address = bearerIpv6Address;
		this.bearerIpv6SubnetMask = bearerIpv6SubnetMask;
		this.bearerGatewayIpv6Address = bearerGatewayIpv6Address;
		this.bearerGatewayIpv6SubnetMask = bearerGatewayIpv6SubnetMask;
	}

	/**
	 * Gets the lsmr host name.
	 *
	 * @return the lsmr host name
	 */
	public String getLsmrHostName() {
		return lsmrHostName;
	}

	/**
	 * Sets the lsmr host name.
	 *
	 * @param lsmrHostName the new lsmr host name
	 */
	public void setLsmrHostName(String lsmrHostName) {
		this.lsmrHostName = lsmrHostName;
	}

	/**
	 * Gets the lsmr ipv 6 address.
	 *
	 * @return the lsmr ipv 6 address
	 */
	public String getLsmrIpv6Address() {
		return lsmrIpv6Address;
	}

	/**
	 * Sets the lsmr ipv 6 address.
	 *
	 * @param lsmrIpv6Address the new lsmr ipv 6 address
	 */
	public void setLsmrIpv6Address(String lsmrIpv6Address) {
		this.lsmrIpv6Address = lsmrIpv6Address;
	}

	/**
	 * Gets the enode B id.
	 *
	 * @return the enode B id
	 */
	public String getEnodeBId() {
		return enodeBId;
	}

	/**
	 * Sets the enode B id.
	 *
	 * @param enodeBId the new enode B id
	 */
	public void setEnodeBId(String enodeBId) {
		this.enodeBId = enodeBId;
	}

	/**
	 * Gets the enode state.
	 *
	 * @return the enode state
	 */
	public String getEnodeState() {
		return enodeState;
	}

	/**
	 * Sets the enode state.
	 *
	 * @param enodeState the new enode state
	 */
	public void setEnodeState(String enodeState) {
		this.enodeState = enodeState;
	}

	/**
	 * Gets the admin state.
	 *
	 * @return the admin state
	 */
	public String getAdminState() {
		return adminState;
	}

	/**
	 * Sets the admin state.
	 *
	 * @param adminState the new admin state
	 */
	public void setAdminState(String adminState) {
		this.adminState = adminState;
	}

	/**
	 * Gets the ems live date.
	 *
	 * @return the ems live date
	 */
	public String getEmsLiveDate() {
		return emsLiveDate;
	}

	/**
	 * Sets the ems live date.
	 *
	 * @param emsLiveDate the new ems live date
	 */
	public void setEmsLiveDate(String emsLiveDate) {
		this.emsLiveDate = emsLiveDate;
	}

	/**
	 * Gets the oam vlan.
	 *
	 * @return the oam vlan
	 */
	public String getOamVlan() {
		return oamVlan;
	}

	/**
	 * Sets the oam vlan.
	 *
	 * @param oamVlan the new oam vlan
	 */
	public void setOamVlan(String oamVlan) {
		this.oamVlan = oamVlan;
	}

	/**
	 * Gets the lsmr id.
	 *
	 * @return the lsmr id
	 */
	public String getLsmrId() {
		return lsmrId;
	}

	/**
	 * Sets the lsmr id.
	 *
	 * @param lsmrId the new lsmr id
	 */
	public void setLsmrId(String lsmrId) {
		this.lsmrId = lsmrId;
	}

	/**
	 * Gets the oam ipv 6 address.
	 *
	 * @return the oam ipv 6 address
	 */
	public String getOamIpv6Address() {
		return oamIpv6Address;
	}

	/**
	 * Sets the oam ipv 6 address.
	 *
	 * @param oamIpv6Address the new oam ipv 6 address
	 */
	public void setOamIpv6Address(String oamIpv6Address) {
		this.oamIpv6Address = oamIpv6Address;
	}

	/**
	 * Gets the oam ipv 6 subnet mask.
	 *
	 * @return the oam ipv 6 subnet mask
	 */
	public String getOamIpv6SubnetMask() {
		return oamIpv6SubnetMask;
	}

	/**
	 * Sets the oam ipv 6 subnet mask.
	 *
	 * @param oamIpv6SubnetMask the new oam ipv 6 subnet mask
	 */
	public void setOamIpv6SubnetMask(String oamIpv6SubnetMask) {
		this.oamIpv6SubnetMask = oamIpv6SubnetMask;
	}

	/**
	 * Gets the oam gateway ipv 6 address.
	 *
	 * @return the oam gateway ipv 6 address
	 */
	public String getOamGatewayIpv6Address() {
		return oamGatewayIpv6Address;
	}

	/**
	 * Sets the oam gateway ipv 6 address.
	 *
	 * @param oamGatewayIpv6Address the new oam gateway ipv 6 address
	 */
	public void setOamGatewayIpv6Address(String oamGatewayIpv6Address) {
		this.oamGatewayIpv6Address = oamGatewayIpv6Address;
	}

	/**
	 * Gets the oam gateway ipv 6 subnet mask.
	 *
	 * @return the oam gateway ipv 6 subnet mask
	 */
	public String getOamGatewayIpv6SubnetMask() {
		return oamGatewayIpv6SubnetMask;
	}

	/**
	 * Sets the oam gateway ipv 6 subnet mask.
	 *
	 * @param oamGatewayIpv6SubnetMask the new oam gateway ipv 6 subnet mask
	 */
	public void setOamGatewayIpv6SubnetMask(String oamGatewayIpv6SubnetMask) {
		this.oamGatewayIpv6SubnetMask = oamGatewayIpv6SubnetMask;
	}

	/**
	 * Gets the signal vlan.
	 *
	 * @return the signal vlan
	 */
	public String getSignalVlan() {
		return signalVlan;
	}

	/**
	 * Sets the signal vlan.
	 *
	 * @param signalVlan the new signal vlan
	 */
	public void setSignalVlan(String signalVlan) {
		this.signalVlan = signalVlan;
	}

	/**
	 * Gets the signal mme group id.
	 *
	 * @return the signal mme group id
	 */
	public String getSignalMmeGroupId() {
		return signalMmeGroupId;
	}

	/**
	 * Sets the signal mme group id.
	 *
	 * @param signalMmeGroupId the new signal mme group id
	 */
	public void setSignalMmeGroupId(String signalMmeGroupId) {
		this.signalMmeGroupId = signalMmeGroupId;
	}

	/**
	 * Gets the signal ipv 6 address.
	 *
	 * @return the signal ipv 6 address
	 */
	public String getSignalIpv6Address() {
		return signalIpv6Address;
	}

	/**
	 * Sets the signal ipv 6 address.
	 *
	 * @param signalIpv6Address the new signal ipv 6 address
	 */
	public void setSignalIpv6Address(String signalIpv6Address) {
		this.signalIpv6Address = signalIpv6Address;
	}

	/**
	 * Gets the signal ipv 6 subnet mask.
	 *
	 * @return the signal ipv 6 subnet mask
	 */
	public String getSignalIpv6SubnetMask() {
		return signalIpv6SubnetMask;
	}

	/**
	 * Sets the signal ipv 6 subnet mask.
	 *
	 * @param signalIpv6SubnetMask the new signal ipv 6 subnet mask
	 */
	public void setSignalIpv6SubnetMask(String signalIpv6SubnetMask) {
		this.signalIpv6SubnetMask = signalIpv6SubnetMask;
	}

	/**
	 * Gets the signal gateway ipv 6 address.
	 *
	 * @return the signal gateway ipv 6 address
	 */
	public String getSignalGatewayIpv6Address() {
		return signalGatewayIpv6Address;
	}

	/**
	 * Sets the signal gateway ipv 6 address.
	 *
	 * @param signalGatewayIpv6Address the new signal gateway ipv 6 address
	 */
	public void setSignalGatewayIpv6Address(String signalGatewayIpv6Address) {
		this.signalGatewayIpv6Address = signalGatewayIpv6Address;
	}

	/**
	 * Gets the signal gateway ipv 6 subnet mask.
	 *
	 * @return the signal gateway ipv 6 subnet mask
	 */
	public String getSignalGatewayIpv6SubnetMask() {
		return signalGatewayIpv6SubnetMask;
	}

	/**
	 * Sets the signal gateway ipv 6 subnet mask.
	 *
	 * @param signalGatewayIpv6SubnetMask the new signal gateway ipv 6 subnet mask
	 */
	public void setSignalGatewayIpv6SubnetMask(String signalGatewayIpv6SubnetMask) {
		this.signalGatewayIpv6SubnetMask = signalGatewayIpv6SubnetMask;
	}

	/**
	 * Gets the bearer vlan.
	 *
	 * @return the bearer vlan
	 */
	public String getBearerVlan() {
		return bearerVlan;
	}

	/**
	 * Sets the bearer vlan.
	 *
	 * @param bearerVlan the new bearer vlan
	 */
	public void setBearerVlan(String bearerVlan) {
		this.bearerVlan = bearerVlan;
	}

	/**
	 * Gets the bearer SAEGW pri G id.
	 *
	 * @return the bearer SAEGW pri G id
	 */
	public String getBearerSAEGWPriGId() {
		return bearerSAEGWPriGId;
	}

	/**
	 * Sets the bearer SAEGW pri G id.
	 *
	 * @param bearerSAEGWPriGId the new bearer SAEGW pri G id
	 */
	public void setBearerSAEGWPriGId(String bearerSAEGWPriGId) {
		this.bearerSAEGWPriGId = bearerSAEGWPriGId;
	}

	/**
	 * Gets the bearer ipv 6 address.
	 *
	 * @return the bearer ipv 6 address
	 */
	public String getBearerIpv6Address() {
		return bearerIpv6Address;
	}

	/**
	 * Sets the bearer ipv 6 address.
	 *
	 * @param bearerIpv6Address the new bearer ipv 6 address
	 */
	public void setBearerIpv6Address(String bearerIpv6Address) {
		this.bearerIpv6Address = bearerIpv6Address;
	}

	/**
	 * Gets the bearer ipv 6 subnet mask.
	 *
	 * @return the bearer ipv 6 subnet mask
	 */
	public String getBearerIpv6SubnetMask() {
		return bearerIpv6SubnetMask;
	}

	/**
	 * Sets the bearer ipv 6 subnet mask.
	 *
	 * @param bearerIpv6SubnetMask the new bearer ipv 6 subnet mask
	 */
	public void setBearerIpv6SubnetMask(String bearerIpv6SubnetMask) {
		this.bearerIpv6SubnetMask = bearerIpv6SubnetMask;
	}

	/**
	 * Gets the bearer gateway ipv 6 address.
	 *
	 * @return the bearer gateway ipv 6 address
	 */
	public String getBearerGatewayIpv6Address() {
		return bearerGatewayIpv6Address;
	}

	/**
	 * Sets the bearer gateway ipv 6 address.
	 *
	 * @param bearerGatewayIpv6Address the new bearer gateway ipv 6 address
	 */
	public void setBearerGatewayIpv6Address(String bearerGatewayIpv6Address) {
		this.bearerGatewayIpv6Address = bearerGatewayIpv6Address;
	}

	/**
	 * Gets the bearer gateway ipv 6 subnet mask.
	 *
	 * @return the bearer gateway ipv 6 subnet mask
	 */
	public String getBearerGatewayIpv6SubnetMask() {
		return bearerGatewayIpv6SubnetMask;
	}

	/**
	 * Sets the bearer gateway ipv 6 subnet mask.
	 *
	 * @param bearerGatewayIpv6SubnetMask the new bearer gateway ipv 6 subnet mask
	 */
	public void setBearerGatewayIpv6SubnetMask(String bearerGatewayIpv6SubnetMask) {
		this.bearerGatewayIpv6SubnetMask = bearerGatewayIpv6SubnetMask;
	}

	public Integer getEnbId() {
		return enbId;
	}

	public void setEnbId(Integer enbId) {
		this.enbId = enbId;
	}

	public Date getLiveEmsTime() {
		return liveEmsTime;
	}

	public void setLiveEmsTime(Date liveEmsTime) {
		this.liveEmsTime = liveEmsTime;
	}

	@Override
	public String toString() {
		return "SiteEmsDetailsWrapper [lsmrHostName=" + lsmrHostName + ", lsmrIpv6Address=" + lsmrIpv6Address
				+ ", enodeBId=" + enodeBId + ", enodeState=" + enodeState + ", adminState=" + adminState
				+ ", emsLiveDate=" + emsLiveDate + ", oamVlan=" + oamVlan + ", lsmrId=" + lsmrId + ", oamIpv6Address="
				+ oamIpv6Address + ", oamIpv6SubnetMask=" + oamIpv6SubnetMask + ", oamGatewayIpv6Address="
				+ oamGatewayIpv6Address + ", oamGatewayIpv6SubnetMask=" + oamGatewayIpv6SubnetMask + ", signalVlan="
				+ signalVlan + ", signalMmeGroupId=" + signalMmeGroupId + ", signalIpv6Address=" + signalIpv6Address
				+ ", signalIpv6SubnetMask=" + signalIpv6SubnetMask + ", signalGatewayIpv6Address="
				+ signalGatewayIpv6Address + ", signalGatewayIpv6SubnetMask=" + signalGatewayIpv6SubnetMask
				+ ", bearerVlan=" + bearerVlan + ", bearerSAEGWPriGId=" + bearerSAEGWPriGId + ", bearerIpv6Address="
				+ bearerIpv6Address + ", bearerIpv6SubnetMask=" + bearerIpv6SubnetMask + ", bearerGatewayIpv6Address="
				+ bearerGatewayIpv6Address + ", bearerGatewayIpv6SubnetMask=" + bearerGatewayIpv6SubnetMask + ", enbId="
				+ enbId + ", liveEmsTime=" + liveEmsTime + "]";
	}

}
