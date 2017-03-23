package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class DataUtil {
	
	public DataUtil(){}
	
	/**
	 * �ݹ�����ļ��в����ļ�
	 * @param project
	 * @param iresourceArry
	 * @throws Exception
	 */
	public IFile ILoop(  IResource[] iresourceArry ) throws Exception{
		IFile file = null;
		for( int j=0; j<iresourceArry.length; j++){
			IResource res = iresourceArry[j];
			IPath rPath = res.getFullPath();
			if( res.getType() == IResource.FILE ){
				file = new MyFile(res.getFullPath(),(Workspace) ResourcesPlugin.getWorkspace());
				this.generateFile( file );
			}else if( res.getType() == IResource.FOLDER ){
				IFolder folder = new MyFolder(res.getFullPath(),(Workspace) ResourcesPlugin.getWorkspace());
				file = new DataUtil().ILoop( folder.members() );
			}
		}
		return file;
	}
	
	
	/**
	 * �����ļ�
	 * @param IFile f
	 * @throws Exception
	 */
	public void generateFile(IFile f) throws Exception{
		
		IProject project = f.getProject();
		
		IJavaProject java = JavaCore.create( project );
		
		FileSystemView home = FileSystemView.getFileSystemView();
		
		//��ȡ��Ŀ����·��
		String projectPath = project.getLocation().toFile().getAbsolutePath();
		
		//��ȡWebRoot�����·��
		String classPath = java.getOutputLocation().toFile().getPath().replace(project.getFullPath().toFile().getPath(), "");
		
		//��ȡclass�ļ��Ľṹ�����·��
		String filePath = f.getLocation().toFile().getAbsolutePath().replace(projectPath+"\\src", "").replace(".java", ".class");
		
		String newPath = f.getLocation().toFile().getAbsolutePath().replace(projectPath+"\\src", "").replace(f.getName(), ""); 
		
		String sPath = projectPath + classPath + filePath;
		
		//��ȡ����ϵͳ������·��
//		String tPath = home.getHomeDirectory().getAbsolutePath() + java.getOutputLocation().toFile().getPath() + filePath ;
		
		//��ȡ��Ҫ�½�Ŀ¼��·��
		String mPath = home.getHomeDirectory().getAbsolutePath() + java.getOutputLocation().toFile().getPath() + newPath ; 
		
		if( !f.getLocation().toFile().getAbsolutePath().contains( projectPath+"\\src" ) ){
			sPath = filePath;
//			tPath = home.getHomeDirectory().getAbsolutePath() + f.getFullPath().toFile().getPath() ;
			mPath = home.getHomeDirectory().getAbsolutePath() + f.getFullPath().toFile().getPath().replace(f.getName(), "") ;
			this.copyFile(sPath, mPath);
		}else{
			this.filerFile(sPath, mPath);
		}
	}
	
	/**
	 * ���˲�����������������class�ļ�
	 * @param classFilePath
	 * @param tPath
	 * @throws Exception
	 */
	public void filerFile(String sPath, String tPath) throws Exception{
		
		File file = new File(sPath);
		
		//��ȡ�ļ������ļ���
		File root = file.getParentFile();
		
		//��ȡĿ¼�µ������ļ�
        File[] files = root.listFiles();
        
        for( int i=0; i<files.length; i++ ){
        	File f = files[i];
        	if( f.getName().equals(file.getName()) || f.getName().startsWith(file.getName().replace(".class", "")+"$") ){
        		this.copyFile(f, tPath);
        	}
        }
	}
	
	/**
	 * �����ļ�
	 * @param sFile	Դ�ļ�
	 * @param tPath	Ŀ���ļ�
	 * @throws Exception
	 */
	public void copyFile(File sFile, String tPath) throws Exception{
		
        new java.io.File(tPath).mkdirs();
        FileInputStream fis = new FileInputStream(sFile.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(tPath+"\\"+sFile.getName());
        byte[] buffer = new byte[4096];
        int read;
        
        while( (read=fis.read(buffer))!=-1 ){
        	fos.write(buffer, 0, read);
        }
        
        if( fis!=null ){
        	fis.close();
        }
        
        if( fos!=null ){
        	fos.close();
        }
        
	}
	
	
	/**
	 * �����ļ�
	 * @param Stirng sPath	Դ�ļ�·��
	 * @param Stirng tPath	Ŀ���ļ�·��
	 * @throws Exception
	 */
	public void copyFile(String sPath, String tPath) throws Exception{
		
		File sFile = new File(sPath);
		
        new java.io.File(tPath).mkdirs();
        
        FileInputStream fis = new FileInputStream(sPath);
        FileOutputStream fos = new FileOutputStream(tPath+"\\"+sFile.getName());
        byte[] buffer = new byte[4096];
        int read;
        
        while( (read=fis.read(buffer))!=-1 ){
        	fos.write(buffer, 0, read);
        }
        
        if( fis!=null ){
        	fis.close();
        }
        
        if( fos!=null ){
        	fos.close();
        }
        
	}
	
}
