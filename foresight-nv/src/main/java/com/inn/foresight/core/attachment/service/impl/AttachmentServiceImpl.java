package com.inn.foresight.core.attachment.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.io.IOUtils;
import com.inn.foresight.core.attachment.dao.IAttachmentDao;
import com.inn.foresight.core.attachment.service.IAttachmentService;
import com.inn.foresight.core.generic.utils.ConfigUtil;

@Service("AttachmentServiceImpl")
public class AttachmentServiceImpl implements IAttachmentService {
	private static Logger logger = LogManager.getLogger(AttachmentServiceImpl.class);

	@Autowired
	IAttachmentDao iAttachmentDao;

	@Override
	public void saveAttachmentInHDFS(InputStream inputStream, String path) throws IOException {
		iAttachmentDao.saveAttachment(inputStream, path);
	}

	@Override
	public void saveAttachment(InputStream inputStream, String path) throws IOException {
		byte[] recordBytes = IOUtils.toByteArray(inputStream);

		logger.info("Saving file as : " + path);
		File targetFile = new File(ConfigUtil.getConfigProp(ConfigUtil.ATTACHMENT_FILE_PATH) + path);

		FileUtils.writeByteArrayToFile(targetFile, recordBytes);
	}

	@Override
	public byte[] downloadFromHDFS(String filePath) throws IOException {
		return iAttachmentDao.downloadFromHDFS(filePath);
	}
	
	
	@Override
	public String isFileExistInHDFS(String filePath) throws IOException {
		return iAttachmentDao.isFileExistInHDFS(filePath);
	}
}
