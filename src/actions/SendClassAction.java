package actions;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.Folder;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.internal.ui.synchronize.SyncInfoModelElement;
import org.eclipse.team.internal.ui.synchronize.SynchronizeModelElement;
import org.eclipse.team.internal.ui.synchronize.UnchangedResourceModelElement;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import common.DataUtil;
import common.MyFile;
import common.MyFolder;

public class SendClassAction implements IObjectActionDelegate {

	private Shell shell;
	
	IStructuredSelection selection = null;
	
	/**
	 * Constructor for Action1.
	 */
	public SendClassAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		try{
			Object[] obj = this.selection.toArray();
			
			IFile f = null;
			
			IFolder folder = null;
			
			for(int i=0; i<obj.length; i++){
				if( obj[i] instanceof UnchangedResourceModelElement ){
					UnchangedResourceModelElement sycnModel = (UnchangedResourceModelElement)obj[i];
					folder = new MyFolder(sycnModel.getResource().getFullPath(),(Workspace) ResourcesPlugin.getWorkspace());
					f = new DataUtil().ILoop( folder.members());
				}else if( obj[i] instanceof Folder ){
					folder = (Folder)obj[i];
					f = new DataUtil().ILoop( folder.members());
				}else if( obj[i] instanceof SyncInfoModelElement ){
					SyncInfoModelElement sync = (SyncInfoModelElement)obj[i];
					f = new MyFile(sync.getResource().getFullPath(),(Workspace) ResourcesPlugin.getWorkspace());	//ResourcesPlugin.getWorkspace()��ȡ�����ռ�����
				}else if( obj[i] instanceof File ){
					f = (File) obj[i];
				}
				
				if( f!=null ){
					new DataUtil().generateFile(f);
				}
					
				
			}
			
			if( f!=null ){
				MessageDialog.openInformation( shell, "�����ɹ�", "�����ļ��Ѵ��������" );
			}else{
				throw new Exception();
			}
			
		}catch( Exception e ){
			MessageDialog.openInformation( shell, "����ʧ��", "����ϵ���������Ա��" );
			e.printStackTrace();
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = (IStructuredSelection) selection;
	}

}
