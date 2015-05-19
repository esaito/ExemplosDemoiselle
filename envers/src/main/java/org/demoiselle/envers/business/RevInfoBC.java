
package org.demoiselle.envers.business;

import br.gov.frameworkdemoiselle.stereotype.BusinessController;
import br.gov.frameworkdemoiselle.template.DelegateCrud;
import org.demoiselle.envers.domain.*;
import java.util.*;
import javax.faces.model.*;
import org.demoiselle.envers.persistence.RevInfoDAO;

// To remove unused imports press: Ctrl+Shift+o

@BusinessController
public class RevInfoBC extends DelegateCrud<RevInfo, Long, RevInfoDAO> {
	private static final long serialVersionUID = 1L;
	
	
}
