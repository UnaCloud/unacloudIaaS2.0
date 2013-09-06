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
public class DatabaseConnection{
	Statement Query;

	/*
	 * Statement es la funci髇 que ejecuta la sentencia SQL Le llamamos Query,
	 * porque realmente es lo mismo.
	 */

	ResultSet Resultado;

	/*
	 * ResultSet es la funci髇 que recibe el resultado generado por la funci髇
	 * Statement
	 */

	Connection Conexion;

	/*
	 * Connection es la Clase que nos permite la conexi贸n a SQL Server, al
	 * igual que Statement y ResultSet esta funci贸n solo se puede utilizar si
	 * importamos las librer铆as de SQL en nuestro programa, lo cual hacemos
	 * con: import java.sql.*;
	 */

	PreparedStatement pstmt;

	public void Desconectar() throws SQLException {
		Conexion.close();
	}

	String databasename;

	public Connection conectar() {
		String ip = VariableManager.getStringValue("MONITORING_SERVER_IP");
		String name = VariableManager
				.getStringValue("MONITORING_DATABASE_NAME");
		String user = VariableManager
				.getStringValue("MONITORING_DATABASE_USER");
		String password = VariableManager
				.getStringValue("MONITORING_DATABASE_PASSWORD");
		String url = "jdbc:mysql://" + ip + ":3306/" + name;
		databasename = VariableManager
				.getStringValue("MONITORING_DATABASE_NAME");
		try {
			Conexion = DriverManager.getConnection(url, user, password);
			Query = Conexion.createStatement();
			pstmt = null;
		} catch (Exception e) {
			e.printStackTrace();
		} // Muestra en la consola si se genera un error durante la conexi贸n
		return Conexion; // Retorna conexi贸n
	}

	/*
	 * Utilizamos la clase Public Statement Sentencias() para poder enviar
	 * sentencias SQL desde otras clases de nuestro proyecto, solo haciendo
	 * instancia de esta clase
	 */
	public Statement Sentencias() {
		return Query;
	}

	void CargarBatchInicial(MonitorInitialReport initialReport) throws SQLException {
		Conexion.setAutoCommit(false);
		String INSERT_RECORD = "INSERT INTO "
				+ databasename
				+ ".Nodes "
				+ "(UUID,Date,HostName,DomainName,OSName,OSVersion,OSArchitect"
				+ ",CPUModel,CPUVendor,CPUCores,CPUSockets,CPUMhz,CPUCoresXSocket"
				+ ",RAMMemorySize,SwapMemorySize,HDSpace,HDFileSystem"
				+ ",NETMACAddress) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// Conexion.setAutoCommit(false);
		// pstmt =
		// Conexion.prepareStatement("INSER INTO [Monitoring].[dbo].[Nodos] ([id] ");
		pstmt = Conexion.prepareStatement(INSERT_RECORD);
		pstmt.setString(1, initialReport.getUUID());
		pstmt.setTimestamp(2, initialReport.getTimest());
		pstmt.setString(3, initialReport.getHostname());
		pstmt.setString(4, initialReport.getDomain());
		pstmt.setString(5, initialReport.getOperatingSystemName());
		pstmt.setString(6, initialReport.getOperatingSystemVersion());
		pstmt.setString(7, initialReport.getOperatingSystemArchitect());
		pstmt.setString(8, initialReport.getcPUModel());
		pstmt.setString(9, initialReport.getcPUVendor());
		pstmt.setInt(10, initialReport.getcPUCores());
		pstmt.setInt(11, initialReport.getTotalSockets());
		pstmt.setString(12, initialReport.getcPUMhz());
		pstmt.setInt(13, initialReport.getCoresPerSocket());
		pstmt.setFloat(14, initialReport.getrAMMemorySize());
		pstmt.setFloat(15, initialReport.getSwapMemorySize());
		pstmt.setFloat(16, initialReport.getHardDiskSpace());
		pstmt.setString(17, initialReport.getHardDiskFileSystem());
		pstmt.setString(18, initialReport.getNetworkMACAddress());
		pstmt.addBatch();
	}

	void CargarBatch(MonitorReport statusReport) throws SQLException {
		Conexion.setAutoCommit(false);
		String INSERT_RECORD = "INSERT INTO "
				+ databasename
				+ ".ResourcesMonitoring"
				+ "(UUID,Id,Date,Counter,UserName,UpTime,CPUMflops,CPUMflopsTime"
				+ ",CPUIdlePct,CPUUsedPct,CPUUserPct,CPUKernelPct,CPUNicePct,CPUWaitPct"
				+ ",CPUCombinedPct,CPUUserTime,CPUKernelTime,CPUNiceTime,CPUWaitTime"
				+ ",CPUIdleTime,RAMMemoryFree,RAMMemoryUsed,RAMMemoryFreePct,RAMMemoryUsedPct"
				+ ",SwapMemoryFree,SwapMemoryPageIn,SwapMemoryPageOut,SwapMemoryUsed"
				+ ",HDFreeSpace,HDUsedSpace,NETIPAddress,NETInterface,NETNetmask"
				+ ",NETGateway,NETRxBytes,NETTxBytes,NETSpeed,NETRxErrors,NETTxErrors"
				+ ",NETRxPackets,NETTxPackets) VALUES (?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		// Conexion.setAutoCommit(false);
		if (pstmt == null)
			pstmt = Conexion.prepareStatement(INSERT_RECORD);
		String ID = statusReport.getUserName() + '#' + statusReport.getTimest().getTime();
		pstmt.setString(1,statusReport.getUUID());
		pstmt.setString(2, ID);
		pstmt.setTimestamp(3,statusReport.getTimest());
		pstmt.setInt(4,statusReport.getContadorRegistros());
		pstmt.setString(5, statusReport.getUserName());
		pstmt.setDouble(6, statusReport.getUptime());
		pstmt.setDouble(7, statusReport.getMflops());
		pstmt.setDouble(8, statusReport.getTimeinSecs());
		pstmt.setDouble(9, statusReport.getIdle());
		pstmt.setDouble(10, statusReport.getD());
		pstmt.setDouble(11, statusReport.getCPuser());
		pstmt.setDouble(12, statusReport.getSys());
		pstmt.setDouble(13, statusReport.getNice());
		pstmt.setDouble(14, statusReport.getWait());
		pstmt.setDouble(15, statusReport.getCombined());
		pstmt.setLong(16, statusReport.getUser());
		pstmt.setLong(17, statusReport.getSys0());
		pstmt.setLong(18, statusReport.getNice0());
		pstmt.setLong(19, statusReport.getWait0());
		pstmt.setLong(20, statusReport.getIdle0());
		pstmt.setFloat(21, statusReport.getrAMMemoryFree());
		pstmt.setFloat(22, statusReport.getrAMMemoryUsed());
		pstmt.setDouble(23, statusReport.getFreePercent());
		pstmt.setDouble(24, statusReport.getUsedPercent());
		pstmt.setFloat(25, statusReport.getSwapMemoryFree());
		pstmt.setFloat(26, statusReport.getSwapMemoryPageIn());
		pstmt.setFloat(27, statusReport.getSwapMemoryPageOut());
		pstmt.setFloat(28, statusReport.getSwapMemoryUsed());
		pstmt.setLong(29, statusReport.getHardDiskFreeSpace());
		pstmt.setLong(30, statusReport.getHardDiskUsedSpace());
		pstmt.setString(31, statusReport.getNetworkIPAddress());
		pstmt.setString(32, statusReport.getNetworkInterface());
		pstmt.setString(33, statusReport.getNetworkNetmask());
		pstmt.setString(34, statusReport.getNetworkGateway());
		pstmt.setLong(35, statusReport.getRxBytes());
		pstmt.setLong(36, statusReport.getTxBytes());
		pstmt.setLong(37, statusReport.getSpeed());
		pstmt.setLong(38, statusReport.getRxErrors());
		pstmt.setLong(39, statusReport.getTxErrors());
		pstmt.setLong(40, statusReport.getRxPackets());
		pstmt.setLong(41, statusReport.getTxPackets());
		pstmt.addBatch();
	}

	public void RegistrarBatch() throws SQLException {
		Statement st = Conexion.createStatement();
		int[] updateCounts = pstmt.executeBatch();
		checkUpdateCounts(updateCounts);
		Conexion.commit();
		st.close();
		Desconectar();
	}

	public static void checkUpdateCounts(int[] updateCounts) {
		for (int i = 0; i < updateCounts.length; i++) {
			if (updateCounts[i] >= 0) {
				// System.out.println("OK; updateCount="+updateCounts[i]);
			} else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
				System.out.println("OK; updateCount=Statement.SUCCESS_NO_INFO");
			} else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
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
