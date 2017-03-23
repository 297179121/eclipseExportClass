package common;

import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.runtime.IPath;

public class MyFolder extends Folder {

	public MyFolder(IPath path, Workspace container){
		super(path, container);
	}
}
