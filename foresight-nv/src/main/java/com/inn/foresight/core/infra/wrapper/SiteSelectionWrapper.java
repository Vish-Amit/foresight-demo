package com.inn.foresight.core.infra.wrapper;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class SiteSelectionWrapper.
 */
@RestWrapper
public class SiteSelectionWrapper {
	
	/** The macro onair. */
	private Map macroOnair;
	
	private Map macroNonradiating;

	private Map macroDecommissioned;

	
	/** The macro planned. */
	private Map macroPlanned;
	
	/** The das planned. */
	private Map dasPlanned;
	
	/** The das onair. */
	private Map dasOnair;
	
	/**  Small Cell *. */
	private Map scopeOutdoorSmallCell;
	
	/** The scope indoor small cell. */
	private Map scopeIndoorSmallCell;
	
	/** The planned outdoor small cell. */
	private Map plannedOutdoorSmallCell;
	
	/** The planned indoor small cell. */
	private Map plannedIndoorSmallCell;
	
	/** The onair outdoor small cell. */
	private Map onairOutdoorSmallCell;
	
	/** The onair indoor small cell. */
	private Map onairIndoorSmallCell;
	
	/**  Wifi keys *. */
	private Map plannedOutdoorWifi;
	
	/** The planned indoor wifi. */
	private Map plannedIndoorWifi;
	
	/** The onair outdoor wifi. */
	private Map onairOutdoorWifi;
	
	/** The onair indoor wifi. */
	private Map onairIndoorWifi;
	
	/**  DAS Keys *. */
	private Map dasIndoorONAIR;
	
	/** The das indoor PLANNED. */
	private Map dasIndoorPLANNED;
	
	/** The das outdoor ONAIR. */
	private Map dasOutdoorONAIR;
	
	/** The das outdoor PLANNED. */
	private Map dasOutdoorPLANNED;
	
	/** For off-air. */
	private Map dasIndoorOFFAIR;
	
	/** The offair indoor small cell. */
	private Map offairIndoorSmallCell;
	
	/** The offair outdoor small cell. */
	private Map offairOutdoorSmallCell;
	
	/** The wifi indoor OFFAIR. */
	private Map wifiIndoorOFFAIR;

	/** Keys for WIFI. */
	private Map onairWifi;
	
	/** The planned wifi. */
	private Map plannedWifi;
	
	/** The off air wifi. */
	private Map offAirWifi;

	/** The building priority. */
	private List<String> buildingPriority;
	
	/** The building type. */
	private List<String> buildingType;
	
	private List<String> neTypeList;

	private List<String> neStatusList;
	
	private Map<String,List<String>> geographyLevels;
	
	private Map macroOffair;

	private Map shooterOnAir;
	
	private Map picoOnAir;
	
	private Map plannedShooter;
	
	private Map plannedPico;
	
	public Map getPlannedShooter() {
		return plannedShooter;
	}

	public void setPlannedShooter(Map plannedShooter) {
		this.plannedShooter = plannedShooter;
	}

	public Map getPlannedPico() {
		return plannedPico;
	}

	public void setPlannedPico(Map plannedPico) {
		this.plannedPico = plannedPico;
	}

	public Map getShooterOnAir() {
		return shooterOnAir;
	}

	public void setShooterOnAir(Map shooterOnAir) {
		this.shooterOnAir = shooterOnAir;
	}

	public Map getPicoOnAir() {
		return picoOnAir;
	}

	public void setPicoOnAir(Map picoOnAir) {
		this.picoOnAir = picoOnAir;
	}

	/**
	 * Gets the macro onair.
	 *
	 * @return the macro onair
	 */
	public Map getMacroOnair() {
		return macroOnair;
	}

	/**
	 * Sets the macro onair.
	 *
	 * @param macroOnair the new macro onair
	 */
	public void setMacroOnair(Map macroOnair) {
		this.macroOnair = macroOnair;
	}

	/**
	 * Gets the macro planned.
	 *
	 * @return the macro planned
	 */
	public Map getMacroPlanned() {
		return macroPlanned;
	}

	/**
	 * Sets the macro planned.
	 *
	 * @param macroPlanned the new macro planned
	 */
	public void setMacroPlanned(Map macroPlanned) {
		this.macroPlanned = macroPlanned;
	}

	/**
	 * Gets the das planned.
	 *
	 * @return the das planned
	 */
	public Map getDasPlanned() {
		return dasPlanned;
	}

	/**
	 * Sets the das planned.
	 *
	 * @param dasPlanned the new das planned
	 */
	public void setDasPlanned(Map dasPlanned) {
		this.dasPlanned = dasPlanned;
	}

	/**
	 * Gets the das onair.
	 *
	 * @return the das onair
	 */
	public Map getDasOnair() {
		return dasOnair;
	}

	/**
	 * Sets the das onair.
	 *
	 * @param dasOnair the new das onair
	 */
	public void setDasOnair(Map dasOnair) {
		this.dasOnair = dasOnair;
	}

	/**
	 * Gets the planned outdoor small cell.
	 *
	 * @return the planned outdoor small cell
	 */
	public Map getPlannedOutdoorSmallCell() {
		return plannedOutdoorSmallCell;
	}

	/**
	 * Sets the planned outdoor small cell.
	 *
	 * @param plannedOutdoorSmallCell the new planned outdoor small cell
	 */
	public void setPlannedOutdoorSmallCell(Map plannedOutdoorSmallCell) {
		this.plannedOutdoorSmallCell = plannedOutdoorSmallCell;
	}

	/**
	 * Gets the planned indoor small cell.
	 *
	 * @return the planned indoor small cell
	 */
	public Map getPlannedIndoorSmallCell() {
		return plannedIndoorSmallCell;
	}

	/**
	 * Sets the planned indoor small cell.
	 *
	 * @param plannedIndoorSmallCell the new planned indoor small cell
	 */
	public void setPlannedIndoorSmallCell(Map plannedIndoorSmallCell) {
		this.plannedIndoorSmallCell = plannedIndoorSmallCell;
	}

	/**
	 * Gets the onair outdoor small cell.
	 *
	 * @return the onair outdoor small cell
	 */
	public Map getOnairOutdoorSmallCell() {
		return onairOutdoorSmallCell;
	}

	/**
	 * Sets the onair outdoor small cell.
	 *
	 * @param onairOutdoorSmallCell the new onair outdoor small cell
	 */
	public void setOnairOutdoorSmallCell(Map onairOutdoorSmallCell) {
		this.onairOutdoorSmallCell = onairOutdoorSmallCell;
	}

	/**
	 * Gets the onair indoor small cell.
	 *
	 * @return the onair indoor small cell
	 */
	public Map getOnairIndoorSmallCell() {
		return onairIndoorSmallCell;
	}

	/**
	 * Sets the onair indoor small cell.
	 *
	 * @param onairIndoorSmallCell the new onair indoor small cell
	 */
	public void setOnairIndoorSmallCell(Map onairIndoorSmallCell) {
		this.onairIndoorSmallCell = onairIndoorSmallCell;
	}

	/**
	 * Gets the planned outdoor wifi.
	 *
	 * @return the planned outdoor wifi
	 */
	public Map getPlannedOutdoorWifi() {
		return plannedOutdoorWifi;
	}

	/**
	 * Sets the planned outdoor wifi.
	 *
	 * @param plannedOutdoorWifi the new planned outdoor wifi
	 */
	public void setPlannedOutdoorWifi(Map plannedOutdoorWifi) {
		this.plannedOutdoorWifi = plannedOutdoorWifi;
	}

	/**
	 * Gets the planned indoor wifi.
	 *
	 * @return the planned indoor wifi
	 */
	public Map getPlannedIndoorWifi() {
		return plannedIndoorWifi;
	}

	/**
	 * Sets the planned indoor wifi.
	 *
	 * @param plannedIndoorWifi the new planned indoor wifi
	 */
	public void setPlannedIndoorWifi(Map plannedIndoorWifi) {
		this.plannedIndoorWifi = plannedIndoorWifi;
	}

	/**
	 * Gets the onair outdoor wifi.
	 *
	 * @return the onair outdoor wifi
	 */
	public Map getOnairOutdoorWifi() {
		return onairOutdoorWifi;
	}

	/**
	 * Sets the onair outdoor wifi.
	 *
	 * @param onairOutdoorWifi the new onair outdoor wifi
	 */
	public void setOnairOutdoorWifi(Map onairOutdoorWifi) {
		this.onairOutdoorWifi = onairOutdoorWifi;
	}

	/**
	 * Gets the onair indoor wifi.
	 *
	 * @return the onair indoor wifi
	 */
	public Map getOnairIndoorWifi() {
		return onairIndoorWifi;
	}

	/**
	 * Sets the onair indoor wifi.
	 *
	 * @param onairIndoorWifi the new onair indoor wifi
	 */
	public void setOnairIndoorWifi(Map onairIndoorWifi) {
		this.onairIndoorWifi = onairIndoorWifi;
	}

	/**
	 * Gets the das indoor ONAIR.
	 *
	 * @return the das indoor ONAIR
	 */
	public Map getDasIndoorONAIR() {
		return dasIndoorONAIR;
	}

	/**
	 * Sets the das indoor ONAIR.
	 *
	 * @param dasIndoorONAIR the new das indoor ONAIR
	 */
	public void setDasIndoorONAIR(Map dasIndoorONAIR) {
		this.dasIndoorONAIR = dasIndoorONAIR;
	}

	/**
	 * Gets the das indoor PLANNED.
	 *
	 * @return the das indoor PLANNED
	 */
	public Map getDasIndoorPLANNED() {
		return dasIndoorPLANNED;
	}

	/**
	 * Sets the das indoor PLANNED.
	 *
	 * @param dasIndoorPLANNED the new das indoor PLANNED
	 */
	public void setDasIndoorPLANNED(Map dasIndoorPLANNED) {
		this.dasIndoorPLANNED = dasIndoorPLANNED;
	}

	/**
	 * Gets the das outdoor ONAIR.
	 *
	 * @return the das outdoor ONAIR
	 */
	public Map getDasOutdoorONAIR() {
		return dasOutdoorONAIR;
	}

	/**
	 * Sets the das outdoor ONAIR.
	 *
	 * @param dasOutdoorONAIR the new das outdoor ONAIR
	 */
	public void setDasOutdoorONAIR(Map dasOutdoorONAIR) {
		this.dasOutdoorONAIR = dasOutdoorONAIR;
	}

	/**
	 * Gets the das outdoor PLANNED.
	 *
	 * @return the das outdoor PLANNED
	 */
	public Map getDasOutdoorPLANNED() {
		return dasOutdoorPLANNED;
	}

	/**
	 * Sets the das outdoor PLANNED.
	 *
	 * @param dasOutdoorPLANNED the new das outdoor PLANNED
	 */
	public void setDasOutdoorPLANNED(Map dasOutdoorPLANNED) {
		this.dasOutdoorPLANNED = dasOutdoorPLANNED;
	}

	/**
	 * Gets the das indoor OFFAIR.
	 *
	 * @return the das indoor OFFAIR
	 */
	public Map getDasIndoorOFFAIR() {
		return dasIndoorOFFAIR;
	}

	/**
	 * Sets the das indoor OFFAIR.
	 *
	 * @param dasIndoreOFFAIR the new das indoor OFFAIR
	 */
	public void setDasIndoorOFFAIR(Map dasIndoreOFFAIR) {
		this.dasIndoorOFFAIR = dasIndoreOFFAIR;
	}

	/**
	 * Gets the offair indoor small cell.
	 *
	 * @return the offair indoor small cell
	 */
	public Map getOffairIndoorSmallCell() {
		return offairIndoorSmallCell;
	}

	/**
	 * Sets the offair indoor small cell.
	 *
	 * @param smallCellIndoreOFFAIR the new offair indoor small cell
	 */
	public void setOffairIndoorSmallCell(Map smallCellIndoreOFFAIR) {
		this.offairIndoorSmallCell = smallCellIndoreOFFAIR;
	}

	/**
	 * Gets the wifi indoor OFFAIR.
	 *
	 * @return the wifi indoor OFFAIR
	 */
	public Map getWifiIndoorOFFAIR() {
		return wifiIndoorOFFAIR;
	}

	/**
	 * Sets the wifi indoor OFFAIR.
	 *
	 * @param wifiIndoreOFFAIR the new wifi indoor OFFAIR
	 */
	public void setWifiIndoorOFFAIR(Map wifiIndoreOFFAIR) {
		this.wifiIndoorOFFAIR = wifiIndoreOFFAIR;
	}

	/**
	 * Gets the building priority.
	 *
	 * @return the building priority
	 */
	public List<String> getBuildingPriority() {
		return buildingPriority;
	}

	/**
	 * Sets the building priority.
	 *
	 * @param buildingPriority the new building priority
	 */
	public void setBuildingPriority(List<String> buildingPriority) {
		this.buildingPriority = buildingPriority;
	}

	/**
	 * Gets the building type.
	 *
	 * @return the building type
	 */
	public List<String> getBuildingType() {
		return buildingType;
	}

	/**
	 * Sets the building type.
	 *
	 * @param buildingType the new building type
	 */
	public void setBuildingType(List<String> buildingType) {
		this.buildingType = buildingType;
	}

	/**
	 * Gets the onair wifi.
	 *
	 * @return the onair wifi
	 */
	public Map getOnairWifi() {
		return onairWifi;
	}

	/**
	 * Sets the onair wifi.
	 *
	 * @param onairWifi the new onair wifi
	 */
	public void setOnairWifi(Map onairWifi) {
		this.onairWifi = onairWifi;
	}

	/**
	 * Gets the planned wifi.
	 *
	 * @return the planned wifi
	 */
	public Map getPlannedWifi() {
		return plannedWifi;
	}

	/**
	 * Sets the planned wifi.
	 *
	 * @param plannedWifi the new planned wifi
	 */
	public void setPlannedWifi(Map plannedWifi) {
		this.plannedWifi = plannedWifi;
	}

	/**
	 * Gets the off air wifi.
	 *
	 * @return the off air wifi
	 */
	public Map getOffAirWifi() {
		return offAirWifi;
	}

	/**
	 * Sets the off air wifi.
	 *
	 * @param offAirWifi the new off air wifi
	 */
	public void setOffAirWifi(Map offAirWifi) {
		this.offAirWifi = offAirWifi;
	}

	/**
	 * Gets the scope outdoor small cell.
	 *
	 * @return the scope outdoor small cell
	 */
	public Map getScopeOutdoorSmallCell() {
		return scopeOutdoorSmallCell;
	}

	/**
	 * Sets the scope outdoor small cell.
	 *
	 * @param scopeOutdoorSmallCell the new scope outdoor small cell
	 */
	public void setScopeOutdoorSmallCell(Map scopeOutdoorSmallCell) {
		this.scopeOutdoorSmallCell = scopeOutdoorSmallCell;
	}

	/**
	 * Gets the scope indoor small cell.
	 *
	 * @return the scope indoor small cell
	 */
	public Map getScopeIndoorSmallCell() {
		return scopeIndoorSmallCell;
	}

	/**
	 * Sets the scope indoor small cell.
	 *
	 * @param scopeIndoorSmallCell the new scope indoor small cell
	 */
	public void setScopeIndoorSmallCell(Map scopeIndoorSmallCell) {
		this.scopeIndoorSmallCell = scopeIndoorSmallCell;
	}

	/**
	 * Gets the offair outdoor small cell.
	 *
	 * @return the offair outdoor small cell
	 */
	public Map getOffairOutdoorSmallCell() {
		return offairOutdoorSmallCell;
	}

	/**
	 * Sets the offair outdoor small cell.
	 *
	 * @param smallCellOutdoorOFFAIR the new offair outdoor small cell
	 */
	public void setOffairOutdoorSmallCell(Map smallCellOutdoorOFFAIR) {
		this.offairOutdoorSmallCell = smallCellOutdoorOFFAIR;
	}

	public List<String> getNeTypeList() {
		return neTypeList;
	}

	public void setNeTypeList(List<String> neTypeList) {
		this.neTypeList = neTypeList;
	}

	public List<String> getNeStatusList() {
		return neStatusList;
	}

	public void setNeStatusList(List<String> neStatusList) {
		this.neStatusList = neStatusList;
	}

	public Map<String, List<String>> getGeographyLevels() {
		return geographyLevels;
	}

	public void setGeographyLevels(Map<String, List<String>> geographyLevels) {
		this.geographyLevels = geographyLevels;
	}

	public Map getMacroNonradiating() {
		return macroNonradiating;
	}

	public void setMacroNonradiating(Map macroNonradiating) {
    this.macroNonradiating = macroNonradiating;
	}

	public Map getMacroDecommissioned() {
		return macroDecommissioned;
	}

	public void setMacroDecommissioned(Map macroDecommissioned) {
		this.macroDecommissioned = macroDecommissioned;
	}

	public Map getMacroOffair() {
		return macroOffair;
	}

	public void setMacroOffair(Map macroOffair) {
		this.macroOffair = macroOffair;
	}

	@Override
	public String toString() {
		return "SiteSelectionWrapper [macroOnair=" + macroOnair + ", macroNonradiating=" + macroNonradiating
				+ ", macroDecommissioned=" + macroDecommissioned + ", macroPlanned=" + macroPlanned + ", dasPlanned="
				+ dasPlanned + ", dasOnair=" + dasOnair + ", scopeOutdoorSmallCell=" + scopeOutdoorSmallCell
				+ ", scopeIndoorSmallCell=" + scopeIndoorSmallCell + ", plannedOutdoorSmallCell="
				+ plannedOutdoorSmallCell + ", plannedIndoorSmallCell=" + plannedIndoorSmallCell
				+ ", onairOutdoorSmallCell=" + onairOutdoorSmallCell + ", onairIndoorSmallCell=" + onairIndoorSmallCell
				+ ", plannedOutdoorWifi=" + plannedOutdoorWifi + ", plannedIndoorWifi=" + plannedIndoorWifi
				+ ", onairOutdoorWifi=" + onairOutdoorWifi + ", onairIndoorWifi=" + onairIndoorWifi
				+ ", dasIndoorONAIR=" + dasIndoorONAIR + ", dasIndoorPLANNED=" + dasIndoorPLANNED + ", dasOutdoorONAIR="
				+ dasOutdoorONAIR + ", dasOutdoorPLANNED=" + dasOutdoorPLANNED + ", dasIndoorOFFAIR=" + dasIndoorOFFAIR
				+ ", offairIndoorSmallCell=" + offairIndoorSmallCell + ", offairOutdoorSmallCell="
				+ offairOutdoorSmallCell + ", wifiIndoorOFFAIR=" + wifiIndoorOFFAIR + ", onairWifi=" + onairWifi
				+ ", plannedWifi=" + plannedWifi + ", offAirWifi=" + offAirWifi + ", buildingPriority="
				+ buildingPriority + ", buildingType=" + buildingType + ", neTypeList=" + neTypeList + ", neStatusList="
				+ neStatusList + ", geographyLevels=" + geographyLevels + ", macroOffair=" + macroOffair
				+ ", shooterOnAir=" + shooterOnAir + ", picoOnAir=" + picoOnAir + ", plannedShooter=" + plannedShooter
				+ ", plannedPico=" + plannedPico + "]";
	}

}