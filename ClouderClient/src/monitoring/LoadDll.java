package monitoring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class LoadDll {
	
	private static final String lib = "/monitoring/sigar/";
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String AR = System.getProperty("os.arch").toLowerCase();
	
	public void loadLibrary() {
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
		
		System.setProperty("java.library.path", System.getProperty("java.io.tmpdir")+";"+System.getProperty("java.library.path"));
	}
	
	private void load(String file){
		try {
			InputStream in = getClass().getResourceAsStream(lib+file);
		    byte[] buffer = new byte[1024];
		    int read = -1;
		    File temp = File.createTempFile(file, "");
		    FileOutputStream fos = new FileOutputStream(temp);
		    while((read = in.read(buffer)) != -1) {
		        fos.write(buffer, 0, read);
		    }
		    fos.close();
		    in.close();
		    System.load(temp.getAbsolutePath());	
		} catch (Exception e) {
		}		
	}
	public boolean isWindows() {		 
		return (OS.indexOf("win") >= 0); 
	} 
	public  boolean isMac() { 
		return (OS.indexOf("mac") >= 0); 
	} 
	public  boolean isUnix() { 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 ); 
	} 
	public  boolean isAix() { 
		return (OS.indexOf("aix") > 0 ); 
	} 
	public  boolean isSolaris() { 
		return (OS.indexOf("sunos") >= 0); 
	}	
	public boolean isFreeBSD(){
		return (OS.indexOf("free") >=0);
	}
	public boolean isHpUx(){
		return (OS.indexOf("hp") >=0);
	}
	public boolean isAMD64(){
		return (AR.indexOf("amd64") >= 0);
	}
	public boolean isX86(){
		return (AR.indexOf("x86") >= 0);
	}
	public boolean isSparc(){
		return (AR.indexOf("sparc") >= 0);
	}
	public boolean isIA(){
		return (AR.indexOf("ia64") >=0);
	}
	public boolean isPaRisc(){
		return (AR.indexOf("risc") >= 0);
	}
	public boolean isPpc(){
		return (AR.indexOf("ppc") >= 0);
	}
	public boolean isPpc64(){
		return (AR.indexOf("ppc64") >= 0);
	}
	public boolean isSparc64(){
		return (AR.indexOf("sparc64") >= 0);
	}
}
