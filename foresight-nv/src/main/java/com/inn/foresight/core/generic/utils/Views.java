package com.inn.foresight.core.generic.utils;

/**
 * The Class Views.
 */
public class Views  {

	 private Views() {
		    throw new IllegalStateException("Utility class");
		  }


	public static abstract interface AoiView {
	}

	/**
	 * The Interface BasicView.
	 */
	public static abstract interface BasicView extends Views.PublicView {
	}

	/**
	 * The Interface ExtendedBAsicView.
	 */
	public static abstract interface ExtendedBAsicView extends Views.BasicView {
	}

	/**
	 * The Interface ExtendedPublicView.
	 */
	public static abstract interface ExtendedPublicView extends Views.BasicView {
	}

	/**
	 * The Interface NoView.
	 */
	public static abstract interface NoView {
	}

	/**
	 * The Interface PublicView.
	 */
	public static abstract interface PublicView {
	}

	/**
	 * The Interface TabView.
	 */
	public static abstract interface TabView {
	}

	/**
	 * The Interface TabViewMyTask.
	 */
	public static abstract interface TabViewMyTask {
	}


	public static interface InnerView { }

	public static interface NcrView{ }
	/**
	 * The Interface ExtendedPublicView.
	 */
	public static interface ForView{ }
	public static interface SerView{ }
	public static interface MastWOTabView{ }
	public static interface UbrLosImageView{ }
	public static interface SMCView{ }
	public static interface CertView{ }
	public static interface SiteSurveyView{ }

	public static interface SmallCellIndoRfiTabView{ }



	/**
	 * The Interface BasicEntity.
	 */
	public static interface BasicEntity extends PublicView { }

	public static interface ATTReturnTabView {}
	
	public static interface ATTMRTabView {}
	
	public static interface ATTMRSpecTabView {}
	
	public static interface IssueMaterialTabView {}
	
	public static interface IssuanceTabView {}
	
	public static interface DeliveryTabView {}
	
	public static interface MaterialTabView {}
	

	/**
	 * The Interface ExtendedBAsicView.
	 */
	

	public static interface TabViewCellOutdoorRFI{}
	
	public static abstract interface UIView {
	}
	public static abstract interface ImageTabView {
	}
	public static interface ticketListView{ }
	
	public static interface ProgramListView {}
	
	public static interface StackHolderView {}
	
	public static interface DocumentView extends BasicEntity{ }
	
	public static interface DocTypeView{}
	public static abstract interface TabLoginUserView {
	}
	public static abstract interface TabAntennaView {
	}
	public static interface InventoryView  extends  BasicView{
	}
	
	public static interface CollaborateView{ }
	
	 public static interface EwopListView { }
		
	public static interface EwopFromView extends EwopListView{ }
	
	public static interface FtrView { }
	
	public static interface GISView extends PublicView{ }
	
	public static interface NotificationView { }

	public static interface WCCBatchDetailView{ }
	public static interface WCCBatchView{ }
	public static interface WCCBatchSiteMappingView{ }
	public static interface SFNowView{ }
	public static interface ListView{ }	
	public static interface TabBasicView extends BasicEntity , TabView{ }

}
