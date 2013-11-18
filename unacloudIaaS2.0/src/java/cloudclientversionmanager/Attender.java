/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudclientversionmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Clouder
 */
public class Attender extends Thread{

    private Socket s;
    
    public Attender(Socket s) {
        this.s = s;
        start();
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String h = br.readLine();
            String[] t = h.split(" +");
            if(t[0].equals("update")) {
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                String version = t[1],serverVersion=getVersion();
                System.out.println("Atendiendo "+version+"\t"+serverVersion);
                if (version.equals(serverVersion)) {
                    pw.println("version ok");
                    pw.flush();
                } else {
                    pw.println("no last version");
                    pw.println(serverVersion);
                    pw.flush();
                }
                s.close();
            }else if(t[0].equals("files")){
                ZipOutputStream zoz=new ZipOutputStream(s.getOutputStream());
                recursiveFill(zoz,new Archivo(new File("version"),"."));
                zoz.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void enviarArchivo(OutputStream os, File c) {
        try {
            FileInputStream fis = new FileInputStream(c);
            byte[] buffer = new byte[1024];
            int a,s=0;
            while ((a = fis.read(buffer)) != -1) {
                s+=a;
                os.write(buffer, 0, a);
            }
            os.flush();
            fis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void recursiveFill(ZipOutputStream zos,Archivo file){
        if(file.f.isFile()){
            try {
                zos.putNextEntry(new ZipEntry(file.path));
                enviarArchivo(zos,file.f);
                zos.closeEntry();
            } catch (IOException ex) {
                Logger.getLogger(Attender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(file.f.isDirectory())for(File c:file.f.listFiles()){
            recursiveFill(zos,new Archivo(c,file.path+"/"+c.getName()));
        }
    }

    class Archivo{
        File f;
        String path;
        public Archivo(File f, String path) {
            this.f = f;
            this.path = path;
        }
    }
    private String getVersion() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("version.txt"));
            String h = br.readLine();
            br.close();
            return h;
        } catch (Exception e) {
        }
        return "1.0";
    }
}