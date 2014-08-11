/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoring;

import com.losandes.utils.VariableManager;
import communication.messages.monitoring.MonitorInitialReport;
import communication.messages.monitoring.MonitorReport;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcadavid
 */
public class MonitorDatabaseConnection {
	
	/**
	 * Class Constructor
	 */
    private MonitorDatabaseConnection() {
    }
    
    /**
     * Connects to monitoring database
     * @return connection done
     * @throws SQLException if connection was not possible
     */
    private static Connection generateConnection() throws SQLException {
        Connection conexion;
        String ip = VariableManager.global.getStringValue("MONITORING_SERVER_IP");
        String name = VariableManager.global.getStringValue("MONITORING_DATABASE_NAME");
        String user = VariableManager.global.getStringValue("MONITORING_DATABASE_USER");
        String password = VariableManager.global.getStringValue("MONITORING_DATABASE_PASSWORD");
        String url = "jdbc:mysql://" + ip + ":3306/" + name;
        conexion = DriverManager.getConnection(url, user, password);
        conexion.setAutoCommit(false);
        return conexion;
    }
    
    /**
     * Reports the initial monitoring info
     * @param initialReport
     */
    public static void doInitialReport(MonitorInitialReport initialReport) {
        final String insertQuery = "INSERT INTO "
                + "Nodes "
                + "(UUID,Date,HostName,OSName,OSVersion,OSArchitect"
                + ",CPUModel,CPUVendor,CPUCores,CPUSockets,CPUMhz,CPUCoresXSocket"
                + ",RAMMemorySize,SwapMemorySize,HDSpace,HDFileSystem"
                + ",NETMACAddress) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = generateConnection()) {
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
                pstmt.setString(1, initialReport.getUUID());
                pstmt.setTimestamp(2, initialReport.getTimest());
                pstmt.setString(3, initialReport.getHostname());
                pstmt.setString(4, initialReport.getOperatingSystemName());
                pstmt.setString(5, initialReport.getOperatingSystemVersion());
                pstmt.setString(6, initialReport.getOperatingSystemArchitect());
                pstmt.setString(7, initialReport.getcPUModel());
                pstmt.setString(8, initialReport.getcPUVendor());
                pstmt.setInt(9, initialReport.getcPUCores());
                pstmt.setInt(10, initialReport.getTotalSockets());
                pstmt.setString(11, initialReport.getcPUMhz());
                pstmt.setInt(12, initialReport.getCoresPerSocket());
                pstmt.setFloat(13, initialReport.getrAMMemorySize());
                pstmt.setFloat(14, initialReport.getSwapMemorySize());
                pstmt.setFloat(15, initialReport.getHardDiskSpace());
                pstmt.setString(16, initialReport.getHardDiskFileSystem());
                pstmt.setString(17, initialReport.getNetworkMACAddress());
                pstmt.executeUpdate();
                
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MonitorDatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Makes a monitoring report in batch
     * @param statusReports reports to be saved
     */
    public static void doBatchReport(MonitorReport... statusReports) {
        int[] results=null;
        final String insertQuery = "INSERT INTO "
                + "ResourcesMonitoring"
                + "(UUID,Date,Counter,UserName,UpTime,CPUMflops,CPUMflopsTime"
                + ",CPUIdlePct,CPUUsedPct,CPUUserPct,CPUKernelPct,CPUNicePct,CPUWaitPct"
                + ",CPUCombinedPct,CPUUserTime,CPUKernelTime,CPUNiceTime,CPUWaitTime"
                + ",CPUIdleTime,RAMMemoryFree,RAMMemoryUsed,RAMMemoryFreePct,RAMMemoryUsedPct"
                + ",SwapMemoryFree,SwapMemoryPageIn,SwapMemoryPageOut,SwapMemoryUsed"
                + ",HDFreeSpace,HDUsedSpace,NETIPAddress,NETInterface,NETNetmask"
                + ",NETGateway,NETRxBytes,NETTxBytes,NETSpeed,NETRxErrors,NETTxErrors"
                + ",NETRxPackets,NETTxPackets,processes) VALUES (?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = generateConnection()) {
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
                for (MonitorReport statusReport : statusReports)if(statusReport!=null){
                    pstmt.setString(1, statusReport.getUUID());
                    pstmt.setTimestamp(2, statusReport.getTimest());
                    pstmt.setInt(3, statusReport.getContadorRegistros());
                    pstmt.setString(4, statusReport.getUserName());
                    pstmt.setDouble(5, statusReport.getUptime());
                    pstmt.setDouble(6, statusReport.getMflops());
                    pstmt.setDouble(7, statusReport.getTimeinSecs());
                    pstmt.setDouble(8, statusReport.getIdle());
                    pstmt.setDouble(9, statusReport.getD());
                    pstmt.setDouble(10, statusReport.getCPuser());
                    pstmt.setDouble(11, statusReport.getSys());
                    pstmt.setDouble(12, statusReport.getNice());
                    pstmt.setDouble(13, statusReport.getWait());
                    pstmt.setDouble(14, statusReport.getCombined());
                    pstmt.setLong(15,statusReport.getUser());
                    pstmt.setLong(16, statusReport.getSys0());
                    pstmt.setLong(17, statusReport.getNice0());
                    pstmt.setLong(18, statusReport.getWait0());
                    pstmt.setLong(19, statusReport.getIdle0());
                    pstmt.setFloat(20, statusReport.getrAMMemoryFree());
                    pstmt.setFloat(21, statusReport.getrAMMemoryUsed());
                    pstmt.setDouble(22, statusReport.getFreePercent());
                    pstmt.setDouble(23, statusReport.getUsedPercent());
                    pstmt.setFloat(24, statusReport.getSwapMemoryFree());
                    pstmt.setFloat(25, statusReport.getSwapMemoryPageIn());
                    pstmt.setFloat(26, statusReport.getSwapMemoryPageOut());
                    pstmt.setFloat(27, statusReport.getSwapMemoryUsed());
                    pstmt.setLong(28, statusReport.getHardDiskFreeSpace());
                    pstmt.setLong(29, statusReport.getHardDiskUsedSpace());
                    pstmt.setString(30, statusReport.getNetworkIPAddress());
                    pstmt.setString(31, statusReport.getNetworkInterface());
                    pstmt.setString(32, statusReport.getNetworkNetmask());
                    pstmt.setString(33, statusReport.getNetworkGateway());
                    pstmt.setLong(34,statusReport.getRxBytes());
                    pstmt.setLong(35, statusReport.getTxBytes());
                    pstmt.setLong(36, statusReport.getSpeed());
                    pstmt.setLong(37, statusReport.getRxErrors());
                    pstmt.setLong(38, statusReport.getTxErrors());
                    pstmt.setLong(39, statusReport.getRxPackets());
                    pstmt.setLong(40, statusReport.getTxPackets());
                    pstmt.setString(41,statusReport.getProcesses());
                    pstmt.addBatch();
                }
                results=pstmt.executeBatch();
                con.commit();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MonitorDatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        checkUpdateCounts(results);
    }
    
    /**
     * Check update results and prints if something went wrong 
     * @param updateCounts
     */
    private static void checkUpdateCounts(int[] updateCounts) {
        if(updateCounts==null)return;
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] >= 0) {
                //System.out.println("OK; updateCount="+updateCounts[i]);
            } else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                System.out.println("OK; updateCount=Statement.SUCCESS_NO_INFO");
            } else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                System.out.println("Failure; updateCount=Statement.EXECUTE_FAILED");
            }
        }
    }
}
