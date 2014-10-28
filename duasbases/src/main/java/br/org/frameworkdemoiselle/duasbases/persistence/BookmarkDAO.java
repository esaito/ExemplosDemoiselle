package br.org.frameworkdemoiselle.duasbases.persistence;

import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.org.frameworkdemoiselle.duasbases.domain.Bookmark;

@PersistenceController
public class BookmarkDAO extends GenericBookmarkDAO<Bookmark, Long> {

	private static final long serialVersionUID = 1L;

}
