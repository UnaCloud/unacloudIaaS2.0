package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.losandes.utils.VariableManager;

public class AbstractGrailsCommunicator{
	public static boolean pushInfo(String serviceName,Object...params){
		final String serverUrl=VariableManager.getStringValue("serverurl");
		try {
			String urlParams=null;
			for(int e=0,i=params.length;e<i;e+=2)urlParams=(urlParams==null?"?":(urlParams+"&"))+params[e]+"="+params[e+1];
			URL url=new URL((serverUrl+"/"+serviceName+urlParams).replace(" ","%20"));
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.connect();
			BufferedReader br=new BufferedReader(new InputStreamReader(http.getInputStream()));
			for(;br.readLine()!=null;);
			http.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static String doRequest(String serviceName,String message){
		final String serverUrl=VariableManager.getStringValue("serverurl");
		String ret="";
		try {
			URL url=new URL(serverUrl+"/"+serviceName+"?type=2");
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			//http.setDoInput(false);
			OutputStream os=http.getOutputStream();
			PrintWriter pw=new PrintWriter(os);
			pw.println("req="+message);
			pw.flush();
			pw.close();
			http.connect();
			BufferedReader br=new BufferedReader(new InputStreamReader(http.getInputStream()));
			for(String h;(h=br.readLine())!=null;)ret+=h;
			http.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
	public static String doRequest(String message){
		return doRequest("UnaCloudServices/clouderClientAttention", message);
	}
	/*public static String doRequest(Object ... msg){
        if(msg==null)return null;
        String[] par=new String[msg.length];
        for(int e=0;e<par.length;e++)par[e]=""+msg[e];
        return doRequest(par);
    }*/
	/*private static String makeMessage(String ... args){
        String resp = "";
        int e=0;
        while(args[e]==null&&e<args.length)e++;
        if(args.length!=e)resp=args[e];
        for(e++;e<args.length;e++)if(args[e]!=null)resp+=MESSAGE_SEPARATOR_TOKEN+args[e];
        return resp;
    }*/
}