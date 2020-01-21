package com.inn.foresight.core.releasenote.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.releasenote.model.ReleaseNote;
import com.inn.foresight.core.releasenote.utils.ReleaseNoteWrapper;

import java.util.List;

public interface ReleaseNoteDao extends IGenericDao<Integer, ReleaseNote> {
    List<ReleaseNoteWrapper> getAllReleaseNote();

	Boolean deleteReleaseNoteByIdList(List<Integer> noteId);

	List<ReleaseNoteWrapper> getSearchedReleaseNote(String searchText);

	boolean isVersionExist(String version);
}
