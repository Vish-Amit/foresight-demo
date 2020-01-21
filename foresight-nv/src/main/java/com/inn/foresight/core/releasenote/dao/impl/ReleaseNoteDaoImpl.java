package com.inn.foresight.core.releasenote.dao.impl;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.releasenote.dao.ReleaseNoteDao;
import com.inn.foresight.core.releasenote.model.ReleaseNote;
import com.inn.foresight.core.releasenote.utils.ReleaseNoteWrapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Repository("ReleaseNoteDaoImpl")
public class ReleaseNoteDaoImpl extends HibernateGenericDao<Integer, ReleaseNote>
        implements ReleaseNoteDao {

    /** The logger. */
    private Logger logger = LogManager.getLogger(ReleaseNoteDaoImpl.class);
    public ReleaseNoteDaoImpl() {
        super(ReleaseNote.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ReleaseNote create(@Valid ReleaseNote ReleaseNote) {
        return super.create(ReleaseNote);
    }

    @Override
    public ReleaseNote update(@Valid ReleaseNote ReleaseNote) {

        return super.update(ReleaseNote);
    }

    @Override
    public void delete(@Valid ReleaseNote ReleaseNote) {
        super.delete(ReleaseNote);
    }

    @Override
    public ReleaseNote findByPk(Integer id) {
        logger.info("Find record by Primary Key :" + id);
        return super.findByPk(id);
    }

    @Override
    public List<ReleaseNote> findAll() {
        return getEntityManager().createNamedQuery("getAllRecord", ReleaseNote.class).getResultList();
    }

    @Override
    public List<ReleaseNoteWrapper> getAllReleaseNote() {
        logger.info("Inside dao ReleaseNoteDaoImpl and method getAllReleaseNote");
        List<ReleaseNoteWrapper> noteList = new ArrayList<>();
        try {
            Query query = getEntityManager().createNamedQuery("getAllReleaseNote");
            noteList = query.getResultList();
        }catch (Exception e){
            logger.error("Error in geting All releasenote data error message {}", ExceptionUtils.getStackTrace(e));
        }
        return noteList;
    }

	@Override
	public Boolean deleteReleaseNoteByIdList(List<Integer> noteId) {
		logger.info("Inside dao ReleaseNoteDaoImpl and method deleteReleaseNoteByIdList {}", noteId);
        try {
            Query query = getEntityManager().createNamedQuery("deleteReleaseNote");
            query.setParameter("idList", noteId);
            query.executeUpdate();
            return true;
        }catch (Exception e){
            logger.error("Error in deleting  releasenote data error message {}", ExceptionUtils.getStackTrace(e));
        }
		return null;
	}

	@Override
	public List<ReleaseNoteWrapper> getSearchedReleaseNote(String searchText) {
		logger.info("Inside dao getSearchedReleaseNote with searchText {}", searchText);
		List<ReleaseNoteWrapper> searchList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("searchReleaseNoteByNameOrVersion")
					.setParameter("searchText", searchText.toLowerCase());
			searchList = query.getResultList();
		}catch (Exception e){
			logger.info("Error in dao method getSearchedReleaseNote: {}", e.getMessage());
		}
		return searchList;
	}

	@Override
	public boolean isVersionExist(String version) {
		logger.info("Inside dao isVersionExist with version {}", version);
		try {
			Query query = getEntityManager().createNamedQuery("getReleaseNoteByVersion")
					.setParameter("version", version);
			if(query.getResultList().isEmpty()) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			logger.info("Error in dao method isVersionExist: {}", e.getMessage());
		}
		return false;
	}
	
}
