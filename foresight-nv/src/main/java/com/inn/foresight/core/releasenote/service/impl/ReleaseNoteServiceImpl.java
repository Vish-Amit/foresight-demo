package com.inn.foresight.core.releasenote.service.impl;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.releasenote.dao.ReleaseNoteDao;
import com.inn.foresight.core.releasenote.model.ReleaseNote;
import com.inn.foresight.core.releasenote.service.ReleaseNoteService;
import com.inn.foresight.core.releasenote.utils.ReleaseNoteWrapper;
import com.inn.product.security.spring.userdetails.CustomerInfo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static com.inn.foresight.core.generic.utils.ForesightConstants.FORWARD_SLASH;
import static com.inn.foresight.core.generic.utils.ForesightConstants.PROTECTED_PATH;

@Service("ReleaseNoteServiceImpl")
public class ReleaseNoteServiceImpl  extends AbstractService<Integer, ReleaseNote>
        implements ReleaseNoteService {
    /** The logger. */
    private Logger logger = LogManager.getLogger(ReleaseNoteServiceImpl.class);

    @Autowired
    private ReleaseNoteDao releaseNoteDao;

    @Override
    public List<ReleaseNoteWrapper> getAllReleaseNote(String searchText) {
        logger.info("Inside Service ReleaseNoteServiceImpl and in method getAllReleaseNote");
        if(searchText != null && !searchText.isEmpty()){
        	return releaseNoteDao.getSearchedReleaseNote(searchText);
        }else {
        	return releaseNoteDao.getAllReleaseNote();
        }
    }

    @Override
    public String uploadReleaseNote(InputStream inputStream, String name,String version,String comment,String fileName, long releaseDate) {
        logger.info("inside @method uploadReleaseNote with : {}",inputStream);
        try {
            if(name != null && version != null && fileName != null) {
                JSONObject obj = new JSONObject();
            	if(releaseNoteDao.isVersionExist(version)) {
                CustomerInfo customerInfo = new CustomerInfo();
                ReleaseNote releaseNote = new ReleaseNote();
                String filePath = Utils.saveFile(inputStream, PROTECTED_PATH + FORWARD_SLASH
                        + "releaseNote" + FORWARD_SLASH, fileName);
                if(comment != null) {
                    releaseNote.setComment(comment);
                }else {
                	releaseNote.setComment("");
                }
                releaseNote.setName(name);
                releaseNote.setDownloadPath("releaseNote" + FORWARD_SLASH +  fileName);
                releaseNote.setVersion(version);
                releaseNote.setCreatedby(customerInfo.getUserId());
                releaseNote.setModifiedby(null);
                releaseNote.setDeleted(false);
                releaseNote.setCreatedtime(new Date());
                releaseNote.setReleasedate(new Date(releaseDate));
                logger.info("releasenote wrapper to save in db {}",releaseNote);
                releaseNoteDao.create(releaseNote);
                obj.put(ForesightConstants.FILEPATH, filePath.replace(PROTECTED_PATH, ForesightConstants.BLANK_STRING));
            	}else {
            		obj.put(ForesightConstants.ERRORMSG, "Version already exist.");
            	}
            	return obj.toString();
            }
        } catch (Exception e) {
            logger.error("Exception while uploadReleaseNote {}", ExceptionUtils.getStackTrace(e));
            throw new RestException(e);
        }
        return null;
    }

	@Override
	@Transactional
	public String deleteReleaseNoteByIdList(List<Integer> noteId) {
		logger.info("Inside Service: ReleaseNoteServiceImpl and method: deleteReleaseNoteByIdList");
		try {
			if(releaseNoteDao.deleteReleaseNoteByIdList(noteId)) {
				return ForesightConstants.SUCCESS_JSON;
			}
		}catch(Exception e) {
			logger.error("Exception while deleteReleaseNoteByIdList{}", ExceptionUtils.getStackTrace(e));
            throw new RestException(e);
		}
		return ForesightConstants.FAILURE_JSON; 
	}
}
