package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/**
 * @author innoeye
 */

@JpaWrapper
public class NetworkElementCellDetailWrapper {
	private String neId;
	private String neFrequency;
	private Vendor vendor;
	private String pneId;
	private String neName;
	
	
	public NetworkElementCellDetailWrapper(String neId, String neFrequency,Vendor vendor) {
		super();
		this.neId = neId;
		this.neFrequency = neFrequency;
		this.vendor = vendor;
	}

   public NetworkElementCellDetailWrapper(String neId, String pneId) {
        super();
        this.neId = neId;
        this.pneId = pneId;
    }

   public NetworkElementCellDetailWrapper(String neId, String pneId, String neName) {
       super();
       this.neId = neId;
       this.pneId = pneId;
       this.neName = neName;
   }
   
	public String getNeId() {
		return neId;
	}

	public void setNeId(String neId) {
		this.neId = neId;
	}

	public String getNeFrequency() {
		return neFrequency;
	}
	
	public void setNeFrequency(String neFrequency) {
		this.neFrequency = neFrequency;
	}
	
	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

    public String getPneId() {
        return pneId;
    }

    public void setPneId(String pneId) {
        this.pneId = pneId;
    }

	public String getNeName() {
		return neName;
	}

	public void setNeName(String neName) {
		this.neName = neName;
	}
	 
}
