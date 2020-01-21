package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class SectorSummaryWrapper.
 */
@RestWrapper
public class SectorSummaryWrapper {

	/** The alpha. */
	String alpha;
	
	/** The beta. */
	String beta;
	
	/** The gamma. */
	String gamma;
	
	/** The index. */
	Integer index;
	
	/** The parameter. */
	String parameter;
	
	/** The index second. */
	Integer indexSecond;
	
	/** The is alpha. */
	private Boolean isAlpha;
	
	/** The is beta. */
	private Boolean isBeta;
	
	/** The is gamma. */
	private Boolean isGamma;
	
	/** The alpha second. */
	String alphaSecond;
	
	/** The beta second. */
	String betaSecond;
	
	/** The gamma second. */
	String gammaSecond;
	
	/** The is second alpha. */
	private Boolean isSecondAlpha ;
	
	/** The is second beta. */
	private Boolean isSecondBeta ;
	
	/** The is second gamma. */
	private Boolean isSecondGamma ;
	
	private String addAlpha;
	
	/** The add beta. */
	private String addBeta;
	
	/** The add gamma. */
	private String addGamma;
	
	private String addAlphaSecond;
	
	/** The add beta second. */
    private String addBetaSecond;
	
	/** The add gamma second. */
	private String addGammaSecond;
	
    private Boolean isAddAlpha;
   
	private Boolean isAddBeta;
	
	private Boolean isAddGamma;
	
	private Boolean isAddSecondAlpha;
	
	private Boolean isAddSecondBeta;
	
	private Boolean isAddSecondGamma;
	
	/**
	 * Gets the alpha.
	 *
	 * @return the alpha
	 */
	public String getAlpha() {
		return alpha;
	}

	/**
	 * Sets the alpha.
	 *
	 * @param alpha the new alpha
	 */
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	/**
	 * Gets the beta.
	 *
	 * @return the beta
	 */
	public String getBeta() {
		return beta;
	}

	/**
	 * Sets the beta.
	 *
	 * @param beta the new beta
	 */
	public void setBeta(String beta) {
		this.beta = beta;
	}

	/**
	 * Gets the gamma.
	 *
	 * @return the gamma
	 */
	public String getGamma() {
		return gamma;
	}

	/**
	 * Sets the gamma.
	 *
	 * @param gamma the new gamma
	 */
	public void setGamma(String gamma) {
		this.gamma = gamma;
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public Integer getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	public void setIndex(Integer index) {
		this.index = index;
	}

	/**
	 * Gets the parameter.
	 *
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Sets the parameter.
	 *
	 * @param parameter the new parameter
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Gets the checks if is alpha.
	 *
	 * @return the checks if is alpha
	 */
	public Boolean getIsAlpha() {
		return isAlpha;
	}

	/**
	 * Sets the checks if is alpha.
	 *
	 * @param isAlpha the new checks if is alpha
	 */
	public void setIsAlpha(Boolean isAlpha) {
		this.isAlpha = isAlpha;
	}

	/**
	 * Gets the checks if is beta.
	 *
	 * @return the checks if is beta
	 */
	public Boolean getIsBeta() {
		return isBeta;
	}

	/**
	 * Sets the checks if is beta.
	 *
	 * @param isBeta the new checks if is beta
	 */
	public void setIsBeta(Boolean isBeta) {
		this.isBeta = isBeta;
	}

	/**
	 * Gets the checks if is gamma.
	 *
	 * @return the checks if is gamma
	 */
	public Boolean getIsGamma() {
		return isGamma;
	}

	/**
	 * Sets the checks if is gamma.
	 *
	 * @param isGamma the new checks if is gamma
	 */
	public void setIsGamma(Boolean isGamma) {
		this.isGamma = isGamma;
	}

	/**
	 * Gets the index second.
	 *
	 * @return the index second
	 */
	public Integer getIndexSecond() {
		return indexSecond;
	}

	/**
	 * Sets the index second.
	 *
	 * @param indexSecond the new index second
	 */
	public void setIndexSecond(Integer indexSecond) {
		this.indexSecond = indexSecond;
	}

	/**
	 * Gets the alpha second.
	 *
	 * @return the alpha second
	 */
	public String getAlphaSecond() {
		return alphaSecond;
	}

	/**
	 * Sets the alpha second.
	 *
	 * @param alphaSecond the new alpha second
	 */
	public void setAlphaSecond(String alphaSecond) {
		this.alphaSecond = alphaSecond;
	}

	/**
	 * Gets the beta second.
	 *
	 * @return the beta second
	 */
	public String getBetaSecond() {
		return betaSecond;
	}

	/**
	 * Sets the beta second.
	 *
	 * @param betaSecond the new beta second
	 */
	public void setBetaSecond(String betaSecond) {
		this.betaSecond = betaSecond;
	}

	/**
	 * Gets the gamma second.
	 *
	 * @return the gamma second
	 */
	public String getGammaSecond() {
		return gammaSecond;
	}

	/**
	 * Sets the gamma second.
	 *
	 * @param gammaSecond the new gamma second
	 */
	public void setGammaSecond(String gammaSecond) {
		this.gammaSecond = gammaSecond;
	}

	/**
	 * Gets the checks if is second alpha.
	 *
	 * @return the checks if is second alpha
	 */
	public Boolean getIsSecondAlpha() {
		return isSecondAlpha;
	}

	/**
	 * Sets the checks if is second alpha.
	 *
	 * @param isSecondAlpha the new checks if is second alpha
	 */
	public void setIsSecondAlpha(Boolean isSecondAlpha) {
		this.isSecondAlpha = isSecondAlpha;
	}

	/**
	 * Gets the checks if is second beta.
	 *
	 * @return the checks if is second beta
	 */
	public Boolean getIsSecondBeta() {
		return isSecondBeta;
	}

	/**
	 * Sets the checks if is second beta.
	 *
	 * @param isSecondBeta the new checks if is second beta
	 */
	public void setIsSecondBeta(Boolean isSecondBeta) {
		this.isSecondBeta = isSecondBeta;
	}

	/**
	 * Gets the checks if is second gamma.
	 *
	 * @return the checks if is second gamma
	 */
	public Boolean getIsSecondGamma() {
		return isSecondGamma;
	}

	/**
	 * Sets the checks if is second gamma.
	 *
	 * @param isSecondGamma the new checks if is second gamma
	 */
	public void setIsSecondGamma(Boolean isSecondGamma) {
		this.isSecondGamma = isSecondGamma;
	}

	public String getAddAlpha() {
		return addAlpha;
	}

	public void setAddAlpha(String addAlpha) {
		this.addAlpha = addAlpha;
	}

	public String getAddBeta() {
		return addBeta;
	}

	public void setAddBeta(String addBeta) {
		this.addBeta = addBeta;
	}

	public String getAddGamma() {
		return addGamma;
	}

	public void setAddGamma(String addGamma) {
		this.addGamma = addGamma;
	}

	public String getAddAlphaSecond() {
		return addAlphaSecond;
	}

	public void setAddAlphaSecond(String addAlphaSecond) {
		this.addAlphaSecond = addAlphaSecond;
	}

	public String getAddBetaSecond() {
		return addBetaSecond;
	}

	public void setAddBetaSecond(String addBetaSecond) {
		this.addBetaSecond = addBetaSecond;
	}

	public String getAddGammaSecond() {
		return addGammaSecond;
	}

	public void setAddGammaSecond(String addGammaSecond) {
		this.addGammaSecond = addGammaSecond;
	}

	public Boolean getIsAddAlpha() {
		return isAddAlpha;
	}

	public void setIsAddAlpha(Boolean isAddAlpha) {
		this.isAddAlpha = isAddAlpha;
	}

	public Boolean getIsAddBeta() {
		return isAddBeta;
	}

	public void setIsAddBeta(Boolean isAddBeta) {
		this.isAddBeta = isAddBeta;
	}

	public Boolean getIsAddGamma() {
		return isAddGamma;
	}

	public void setIsAddGamma(Boolean isAddGamma) {
		this.isAddGamma = isAddGamma;
	}

	public Boolean getIsAddSecondAlpha() {
		return isAddSecondAlpha;
	}

	public void setIsAddSecondAlpha(Boolean isAddSecondAlpha) {
		this.isAddSecondAlpha = isAddSecondAlpha;
	}

	public Boolean getIsAddSecondBeta() {
		return isAddSecondBeta;
	}

	public void setIsAddSecondBeta(Boolean isAddSecondBeta) {
		this.isAddSecondBeta = isAddSecondBeta;
	}

	public Boolean getIsAddSecondGamma() {
		return isAddSecondGamma;
	}

	public void setIsAddSecondGamma(Boolean isAddSecondGamma) {
		this.isAddSecondGamma = isAddSecondGamma;
	}

	@Override
	public String toString() {
		return "SectorSummaryWrapper [alpha=" + alpha + ", beta=" + beta + ", gamma=" + gamma + ", index=" + index
				+ ", parameter=" + parameter + ", indexSecond=" + indexSecond + ", isAlpha=" + isAlpha + ", isBeta="
				+ isBeta + ", isGamma=" + isGamma + ", alphaSecond=" + alphaSecond + ", betaSecond=" + betaSecond
				+ ", gammaSecond=" + gammaSecond + ", isSecondAlpha=" + isSecondAlpha + ", isSecondBeta=" + isSecondBeta
				+ ", isSecondGamma=" + isSecondGamma + ", addAlpha=" + addAlpha + ", addBeta=" + addBeta + ", addGamma="
				+ addGamma + ", addAlphaSecond=" + addAlphaSecond + ", addBetaSecond=" + addBetaSecond
				+ ", addGammaSecond=" + addGammaSecond + ", isAddAlpha=" + isAddAlpha + ", isAddBeta=" + isAddBeta
				+ ", isAddGamma=" + isAddGamma + ", isAddSecondAlpha=" + isAddSecondAlpha + ", isAddSecondBeta="
				+ isAddSecondBeta + ", isAddSecondGamma=" + isAddSecondGamma + "]";
	}

}
