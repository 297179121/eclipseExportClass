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
	 * 递归遍历文件夹查找文件
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
	 * 生成文件
	 * @param IFile f
	 * @throws Exception
	 */
	public void generateFile(IFile f) throws Exception{
		
		IProject project = f.getProject();
		
		IJavaProject java = JavaCore.create( project );
		
		FileSystemView home = FileSystemView.getFileSystemView();
		
		//获取项目绝对路径
		String projectPath = project.getLocation().toFile().getAbsolutePath();
		
		//获取WebRoot的相对路径
		String classPath = java.getOutputLocation().toFile().getPath().replace(project.getFullPath().toFile().getPath(), "");
		
		//获取class文件的结构及相对路径
		String filePath = f.getLocation().toFile().getAbsolutePath().replace(projectPath+"\\src", "").replace(".java", ".class");
		
		String newPath = f.getLocation().toFile().getAbsolutePath().replace(projectPath+"\\src", "").replace(f.getName(), ""); 
		
		String sPath = projectPath + classPath + filePath;
		
		//获取操作系统的桌面路径
//		String tPath = home.getHomeDirectory().getAbsolutePath() + java.getOutputLocation().toFile().getPath() + filePath ;
		
		//获取需要新建目录的路径
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
	 * 过滤并拷贝出符合条件的class文件
	 * @param classFilePath
	 * @param tPath
	 * @throws Exception
	 */
	public void filerFile(String sPath, String tPath) throws Exception{
		
		File file = new File(sPath);
		
		//获取文件所在文件夹
		File root = file.getParentFile();
		
		//获取目录下的所有文件
        File[] files = root.listFiles();
        
        for( int i=0; i<files.length; i++ ){
        	File f = files[i];
        	if( f.getName().equals(file.getName()) || f.getName().startsWith(file.getName().replace(".class", "")+"$") ){
        		this.copyFile(f, tPath);
        	}
        }
	}
	
	/**
	 * 拷贝文件
	 * @param sFile	源文件
	 * @param tPath	目标文件
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
	 * 拷贝文件
	 * @param Stirng sPath	源文件路径
	 * @param Stirng tPath	目标文件路径
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
