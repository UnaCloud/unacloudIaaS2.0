import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class MainTest {

	public static void main(String[] args){
		long l=System.currentTimeMillis();
		try {
			URL url=new URL("http://157.253.219.45:8080/test/");
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			//http.setDoInput(false);
			OutputStream os=http.getOutputStream();
			PrintWriter pw=new PrintWriter(os);
			pw.println("token=hola&mundo");
			pw.close();
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
