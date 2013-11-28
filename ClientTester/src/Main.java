import java.io.PrintWriter;
import java.net.Socket;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Main {

	public static void main(String[] args)throws Exception{
		Socket s=new Socket("127.0.0.1",3020);
		long sum=0,time=-System.currentTimeMillis();
		try{
			PrintWriter pw=new PrintWriter(s.getOutputStream());
			ZipInputStream zis=new ZipInputStream(s.getInputStream());
			pw.println(1);
			pw.flush();
			byte[] buffer=new byte[1024*100];
			for(ZipEntry entry;(entry=zis.getNextEntry())!=null;){
				System.out.println(entry.getName());
				for(int n;(n=zis.read(buffer))!=-1;){
					sum+=n;
				}
				System.out.println(sum);
				zis.closeEntry();
			}
		}catch(Exception e){
			
		}
		System.out.println("Leidos "+sum);
		time+=System.currentTimeMillis();
		System.out.println((sum*1000)/time);
		s.close();
	}

}
