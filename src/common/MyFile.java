package common;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.runtime.IPath;

public class MyFile extends File {

	public MyFile(IPath path, Workspace container){
		super(path, container);
	}
}
