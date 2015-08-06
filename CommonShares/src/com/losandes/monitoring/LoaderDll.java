package com.losandes.monitoring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.losandes.utils.VariableManager;

public class LoaderDll {
	
	private final String lib = "/monitoring/sigar/";
	private final String OS = System.getProperty("os.name").toLowerCase();
	private final String AR = System.getProperty("os.arch").toLowerCase();
	private static LoaderDll instance;
	
	public static synchronized LoaderDll getInstance(){
		if(instance==null)instance=new LoaderDll();
		return instance;
	}
	
	private LoaderDll() {	
	}
	public void loadLibrary() {
		System.out.println("Reload sigar library");
		if(isWindows())
			if(isAMD64())load("sigar-amd64-winnt.dll");
			else load("sigar-x86-winnt.dll");			
		else if(isUnix())
			if(isAMD64())load("libsigar-amd64-linux.so");
			else if(isX86())load("libsigar-x86-linux.so");	
			else if(isIA())load("libsigar-ia64-linux.so");
			else if(isPpc64())load("libsigar-ppc64-linux.so");
			else if(isPpc())load("libsigar-ppc-linux.so");
		else if(isSolaris())
			if(isX86())load("libsigar-x86-solaris.so");
			else if(isAMD64())load("libsigar-amd64-solaris.so");
			else if(isSparc64())load("libsigar-sparc64-solaris.so");
			else if(isSparc())load("libsigar-sparc-solaris.so");
	    else if(isMac())
			if(isAMD64())load("libsigar-universal64-macosx.dylib");
			else load("libsigar-universal-macosx.dylib");	
	    else if(isFreeBSD())
	    	if(isAMD64())load("libsigar-amd64-freebsd-6.so");
	    	else if(isX86())load("libsigar-x86-freebsd-6.so");
	    else if(isHpUx())
	    	if(isIA())load("libsigar-ia64-hpux-11.sl");
	    	else if(isPaRisc())load("libsigar-pa-hpux-11.sl");
	    else if(isAix())
	    	if(isPpc64())load("libsigar-ppc64-aix-5.so");
	    	else if(isPpc())load("libsigar-ppc-aix-5.so");			
		//System.setProperty("java.library.path", System.getProperty("java.io.tmpdir")+";"+System.getProperty("java.library.path"));
		if(!System.getProperty("java.library.path").contains(VariableManager.local.getStringValue("DATA_PATH")))
			System.setProperty("java.library.path", VariableManager.local.getStringValue("DATA_PATH")+";"+System.getProperty("java.library.path"));
	}
	
	private void load(String file){
		try {
			InputStream in = getClass().getResourceAsStream(lib+file);
		    byte[] buffer = new byte[1024];
		    int read = -1;
		    File fileToLoad = new File( VariableManager.local.getStringValue("DATA_PATH")+file);
		   // File temp = File.createTempFile(file, "");
		    FileOutputStream fos = new FileOutputStream(fileToLoad);
		    while((read = in.read(buffer)) != -1) {
		        fos.write(buffer, 0, read);
		    }
		    fos.close();
		    in.close();
		    System.load(fileToLoad.getAbsolutePath());	
		} catch (Exception e) {
		}		
	}
	private boolean isWindows() {		 
		return (OS.indexOf("win") >= 0); 
	} 
	private boolean isMac() { 
		return (OS.indexOf("mac") >= 0); 
	} 
	private boolean isUnix() { 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 ); 
	} 
	private boolean isAix() { 
		return (OS.indexOf("aix") > 0 ); 
	} 
	private boolean isSolaris() { 
		return (OS.indexOf("sunos") >= 0); 
	}	
	private boolean isFreeBSD(){
		return (OS.indexOf("free") >=0);
	}
	private boolean isHpUx(){
		return (OS.indexOf("hp") >=0);
	}
	private boolean isAMD64(){
		return (AR.indexOf("amd64") >= 0);
	}
	private boolean isX86(){
		return (AR.indexOf("x86") >= 0);
	}
	private boolean isSparc(){
		return (AR.indexOf("sparc") >= 0);
	}
	private boolean isIA(){
		return (AR.indexOf("ia64") >=0);
	}
	private boolean isPaRisc(){
		return (AR.indexOf("risc") >= 0);
	}
	private boolean isPpc(){
		return (AR.indexOf("ppc") >= 0);
	}
	private boolean isPpc64(){
		return (AR.indexOf("ppc64") >= 0);
	}
	private boolean isSparc64(){
		return (AR.indexOf("sparc64") >= 0);
	}
}
