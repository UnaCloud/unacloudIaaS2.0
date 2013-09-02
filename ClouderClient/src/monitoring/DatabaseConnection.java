/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package monitoring;

import com.losandes.utils.Log;
import com.losandes.utils.VariableManager;
import java.sql.*;
/**
 *
 * @author jcadavid
 */
public class DatabaseConnection
{
    Statement Query;

  /*Statement es la función que ejecuta la sentencia SQL
    Le llamamos Query, porque realmente es lo mismo. */

    ResultSet Resultado;

 /*ResultSet es la función que recibe el resultado
  * generado por la función Statement*/

    Connection Conexion;

     /*Connection es la Clase que nos permite la conexión
      * a SQL Server, al igual que Statement y ResultSet
      * esta función solo se puede utilizar si importamos
      * las librerías de SQL en nuestro programa, lo cual
      * hacemos con: import java.sql.*;  */

    PreparedStatement pstmt;

    public void Desconectar() throws SQLException
       {Conexion.close();}
    String databasename;
    public Connection Conectar()
    {
        String ip=VariableManager.getStringValue("MONITORING_SERVER_IP");
        String name=VariableManager.getStringValue("MONITORING_DATABASE_NAME");
        String user=VariableManager.getStringValue("MONITORING_DATABASE_USER");
        String password=VariableManager.getStringValue("MONITORING_DATABASE_PASSWORD");
        String url ="jdbc:mysql://"+ip+":3306/"+name;
        databasename=VariableManager.getStringValue("MONITORING_DATABASE_NAME");
        try{
            Conexion = DriverManager.getConnection(url,user,password);
            Query = Conexion.createStatement();
            pstmt=null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        } //Muestra en la consola si se genera un error durante la conexión
        return Conexion; // Retorna conexión
    }

    /*Utilizamos la clase Public Statement Sentencias()
    para poder enviar sentencias SQL desde otras clases
    de nuestro proyecto, solo haciendo instancia de esta clase*/
    public Statement Sentencias()
    {return Query;}

    void CargarBatchInicial(String UUID, Timestamp timest, String hostname, String domain, String operatingSystemName,
            String operatingSystemVersion, String operatingSystemArchitect, String cPUModel, String cPUVendor,
            int cPUCores, int totalSockets, String cPUMhz, int coresPerSocket, float rAMMemorySize, float swapMemorySize,
            long hardDiskSpace, String hardDiskFileSystem, String networkMACAddress) throws SQLException
    {
        Conexion.setAutoCommit(false);
        String INSERT_RECORD = "INSERT INTO "+databasename+".Nodes "
                + "(UUID,Date,HostName,DomainName,OSName,OSVersion,OSArchitect"
                + ",CPUModel,CPUVendor,CPUCores,CPUSockets,CPUMhz,CPUCoresXSocket"
                + ",RAMMemorySize,SwapMemorySize,HDSpace,HDFileSystem"
                + ",NETMACAddress) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //Conexion.setAutoCommit(false);
        //pstmt = Conexion.prepareStatement("INSER INTO [Monitoring].[dbo].[Nodos] ([id] ");
        pstmt = Conexion.prepareStatement(INSERT_RECORD);
        pstmt.setString(1,UUID);
        pstmt.setTimestamp(2,timest);
        pstmt.setString(3,hostname);
        pstmt.setString(4,domain);
        pstmt.setString(5,operatingSystemName);
        pstmt.setString(6,operatingSystemVersion);
        pstmt.setString(7,operatingSystemArchitect);
        pstmt.setString(8,cPUModel);
        pstmt.setString(9,cPUVendor);
        pstmt.setInt(10,cPUCores);
        pstmt.setInt(11,totalSockets);
        pstmt.setString(12,cPUMhz);
        pstmt.setInt(13,coresPerSocket);
        pstmt.setFloat(14,rAMMemorySize);
        pstmt.setFloat(15,swapMemorySize);
        pstmt.setFloat(16,hardDiskSpace);
        pstmt.setString(17,hardDiskFileSystem);
        pstmt.setString(18,networkMACAddress);
        pstmt.addBatch();
    }

    void CargarBatch(String UUID, Timestamp timest, int ContadorRegistros, String UserName,
            double uptime, double mflops, double timeinSecs, double idle, double d, double CPuser, double sys,
            double nice, double wait, double combined, long user, long sys0, long nice0, long wait0,
            long idle0, float rAMMemoryFree, float rAMMemoryUsed, double freePercent, double usedPercent,
            float swapMemoryFree, float swapMemoryPageIn, float swapMemoryPageOut, float swapMemoryUsed,
            long hardDiskFreeSpace, long hardDiskUsedSpace, String networkIPAddress, String networkInterface,
            String networkNetmask, String networkGateway, long rxBytes, long txBytes, long speed, long rxErrors,
            long txErrors, long rxPackets, long txPackets) throws SQLException
    {
        Conexion.setAutoCommit(false);
        String INSERT_RECORD = "INSERT INTO "+databasename+".ResourcesMonitoring"
                + "(UUID,Id,Date,Counter,UserName,UpTime,CPUMflops,CPUMflopsTime"
                + ",CPUIdlePct,CPUUsedPct,CPUUserPct,CPUKernelPct,CPUNicePct,CPUWaitPct"
                + ",CPUCombinedPct,CPUUserTime,CPUKernelTime,CPUNiceTime,CPUWaitTime"
                + ",CPUIdleTime,RAMMemoryFree,RAMMemoryUsed,RAMMemoryFreePct,RAMMemoryUsedPct"
                + ",SwapMemoryFree,SwapMemoryPageIn,SwapMemoryPageOut,SwapMemoryUsed"
                + ",HDFreeSpace,HDUsedSpace,NETIPAddress,NETInterface,NETNetmask"
                + ",NETGateway,NETRxBytes,NETTxBytes,NETSpeed,NETRxErrors,NETTxErrors"
                + ",NETRxPackets,NETTxPackets) VALUES (?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        //Conexion.setAutoCommit(false);
        if(pstmt==null)pstmt = Conexion.prepareStatement(INSERT_RECORD);
        String ID=UserName+'#'+timest.getTime();
        pstmt.setString(1,UUID);
        pstmt.setString(2,ID);
        pstmt.setTimestamp(3,timest);
        pstmt.setInt(4,ContadorRegistros);
        pstmt.setString(5,UserName);
        pstmt.setDouble(6,uptime);
        pstmt.setDouble(7,mflops);
        pstmt.setDouble(8,timeinSecs);
        pstmt.setDouble(9,idle);
        pstmt.setDouble(10,d);
        pstmt.setDouble(11,CPuser);
        pstmt.setDouble(12,sys);
        pstmt.setDouble(13,nice);
        pstmt.setDouble(14,wait);
        pstmt.setDouble(15,combined);
        pstmt.setLong(16,user);
        pstmt.setLong(17,sys0);
        pstmt.setLong(18,nice0);
        pstmt.setLong(19,wait0);
        pstmt.setLong(20,idle0);
        pstmt.setFloat(21,rAMMemoryFree);
        pstmt.setFloat(22,rAMMemoryUsed);
        pstmt.setDouble(23,freePercent);
        pstmt.setDouble(24,usedPercent);
        pstmt.setFloat(25,swapMemoryFree);
        pstmt.setFloat(26,swapMemoryPageIn);
        pstmt.setFloat(27,swapMemoryPageOut);
        pstmt.setFloat(28,swapMemoryUsed);
        pstmt.setLong(29,hardDiskFreeSpace);
        pstmt.setLong(30,hardDiskUsedSpace);
        pstmt.setString(31,networkIPAddress);
        pstmt.setString(32,networkInterface);
        pstmt.setString(33,networkNetmask);
        pstmt.setString(34,networkGateway);
        pstmt.setLong(35,rxBytes);
        pstmt.setLong(36,txBytes);
        pstmt.setLong(37,speed);
        pstmt.setLong(38,rxErrors);
        pstmt.setLong(39,txErrors);
        pstmt.setLong(40,rxPackets);
        pstmt.setLong(41,txPackets);
        pstmt.addBatch();
    }

    public void RegistrarBatch() throws SQLException
    {
        Statement st = Conexion.createStatement();
        int[] updateCounts = pstmt.executeBatch();
        checkUpdateCounts(updateCounts);
        Conexion.commit();
        st.close();
        Desconectar();
    }

    public static void checkUpdateCounts(int[] updateCounts) {
        for (int i=0; i<updateCounts.length; i++) {
            if (updateCounts[i] >= 0) {
                //System.out.println("OK; updateCount="+updateCounts[i]);
            }
            else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                System.out.println("OK; updateCount=Statement.SUCCESS_NO_INFO");
            }
            else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                System.out.println("Failure; updateCount=Statement.EXECUTE_FAILED");
            }
        }
    }

    private static void outputResultSet(ResultSet rs) throws Exception {
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int numberOfColumns = rsMetaData.getColumnCount();
        for (int i = 1; i < numberOfColumns + 1; i++) {
          String columnName = rsMetaData.getColumnName(i);
          System.out.print(columnName + "   ");
        }
        System.out.println();
        System.out.println("----------------------");

        while (rs.next()) {
          for (int i = 1; i < numberOfColumns + 1; i++) {
            System.out.print(rs.getString(i) + "   ");
          }
          System.out.println();
        }
      }

}
