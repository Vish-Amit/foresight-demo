package com.inn.foresight.core.mail.utils.wrapper.mail;

/**
 * The Class Attachment.
 */
public class Attachment {

	/** The file path. */
	private String filePath;
	
	/** The file name. */
	private String fileName;
	
	/** The type. */
	private AttachmentType type;
	
	/** The attachment file. */
	private byte[] attachmentFile;

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Sets the file path.
	 *
	 * @param filePath the new file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public AttachmentType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(AttachmentType type) {
		this.type = type;
	}

	/**
	 * Gets the attachment file.
	 *
	 * @return the attachment file
	 */
	public byte[] getAttachmentFile() {
		return attachmentFile;
	}

	/**
	 * Sets the attachment file.
	 *
	 * @param attachmentFile the new attachment file
	 */
	public void setAttachmentFile(byte[] attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	/**
	 * Instantiates a new attachment.
	 */
	public Attachment() {
		super();
	}

	/**
	 * The Enum AttachmentType.
	 */
	public enum AttachmentType {
		
		/** The ms excel. */
		MS_EXCEL("application/vnd.ms-excel");

		/** The value. */
		private String value;

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Sets the value.
		 *
		 * @param value the new value
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * Instantiates a new attachment type.
		 *
		 * @param value the value
		 */
		private AttachmentType(String value) {
			this.value = value;
		}

	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Attachment [filePath=" + filePath + ", fileName=" + fileName
				+ ", type=" + type + "]";
	}

}
