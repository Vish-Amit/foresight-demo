package com.inn.foresight.core.releasenote.service;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.releasenote.model.ReleaseNote;
import com.inn.foresight.core.releasenote.utils.ReleaseNoteWrapper;

import java.io.InputStream;
import java.util.List;

public interface ReleaseNoteService extends IGenericService<Integer, ReleaseNote> {

    List<ReleaseNoteWrapper> getAllReleaseNote(String searchText);

	String uploadReleaseNote(InputStream inputStream, String name, String version, String comment, String fileName, long releaseDate);

	String deleteReleaseNoteByIdList(List<Integer> noteId);
}
