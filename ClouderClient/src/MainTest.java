import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainTest {

	public static void main(String[] args){
		long l=System.currentTimeMillis();
		try {
			//http://localhost:8080/Unacloud2/test?token=asd
			URL url=new URL("http://localhost:8080/Unacloud2/test");
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			//http.setDoInput(false);
			OutputStream os=http.getOutputStream();
			PrintWriter pw=new PrintWriter(os);
			pw.println("token=holamundo");
			pw.flush();
			pw.close();
			http.connect();
			BufferedReader br=new BufferedReader(new InputStreamReader(http.getInputStream()));
			for(String h;(h=br.readLine())!=null;)System.out.println(h);
			http.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis()-l);
		//System.out.println(URLEncoder.encode("hola & hola"));
	}
}
