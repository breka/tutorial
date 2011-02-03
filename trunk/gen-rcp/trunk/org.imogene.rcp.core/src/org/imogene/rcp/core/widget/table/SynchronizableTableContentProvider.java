package org.imogene.rcp.core.widget.table;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.imogene.common.data.Synchronizable;


/**	 
 * Content provider for the JFace Table Viewer
 * @author Medes-IMPS
 */
public class SynchronizableTableContentProvider implements IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		@SuppressWarnings("unchecked")
		List<Synchronizable> entities = (List<Synchronizable>) inputElement;
		return entities.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}
}
