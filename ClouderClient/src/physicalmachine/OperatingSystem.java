package physicalmachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import virtualMachineManager.LocalProcessExecutor;
import static com.losandes.utils.Constants.*;

/**
 * @author Eduardo Rosales
 * Responsible for executing operating system operations
 */
public class OperatingSystem {

    private String operatingSystemName;
    private String operatingSystemCurrentUser;

    public OperatingSystem() {
        operatingSystemName = getOperatingSystemName();
        operatingSystemCurrentUser = getUserName();
    }

    /**
     * Responsible for obtaining the operating system name
     * @return
     */
    public String getOperatingSystemName() {
        return System.getProperty("os.name");
    }

    /**
     * Responsible for obtaining the operating system version
     * @return
     */
    public String getOperatingSystemVersion() {
        return System.getProperty("os.version");
    }

    /**
     * Responsible for obtaining the operating system architect
     * @return
     */
    public String getOperatingSystemArchitect() {
        return System.getProperty("os.arch");
    }

    /**
     * Responsible for turning off the local operating system
     * @return
     */
    public String turnOff() {
        String result = "";
        if (operatingSystemName.toLowerCase().contains("windows")) {
            LocalProcessExecutor.executeCommand(WINDOWS_TURN_OFF_COMMAND);
        } else if (operatingSystemName.toLowerCase().contains("mac")) {
            LocalProcessExecutor.executeCommand("sh " + BIN + PATH_SEPARATOR + MAC_TURN_OFF_FILE_COMMAND);
        } else if (operatingSystemName.toLowerCase().contains("linux")) {
            LocalProcessExecutor.executeCommand("sh " + BIN + PATH_SEPARATOR + LINUX_TURN_OFF_FILE_COMMAND);
        } else {
            result = ERROR_MESSAGE + "The operating system is not supported: " + operatingSystemName;
            System.err.println(result);
        }
        return result;
    }

    /**
     * Responsible for restarting the local operating system
     * @return
     */
    public String restart() {
        String result = "";
        if (operatingSystemName.toLowerCase().contains("windows")) {
            LocalProcessExecutor.executeCommand(WINDOWS_RESTART_COMMAND);
        } else if (operatingSystemName.toLowerCase().contains("mac")) {
            LocalProcessExecutor.executeCommand("sh " + BIN + PATH_SEPARATOR + MAC_RESTART_FILE_COMMAND);
        } else if (operatingSystemName.toLowerCase().contains("linux")) {
            LocalProcessExecutor.executeCommand("sh " + BIN + PATH_SEPARATOR + LINUX_RESTART_FILE_COMMAND);
        } else {
            result = ERROR_MESSAGE + "The operating system is not supported: " + operatingSystemName;
            System.err.println(result);
        }
        return result;
    }

    /**
     * Responsible for logout the local operating system
     * @return
     */
    public String logOut() {
        String result = "";
        if (operatingSystemName.toLowerCase().contains("windows")) {
            LocalProcessExecutor.executeCommand(WINDOWS_LOGOUT_COMMAND);
        } else if (operatingSystemName.toLowerCase().contains("linux")) {
            LocalProcessExecutor.executeCommand("sh " + BIN + PATH_SEPARATOR + LINUX_LOGOUT_FILE_COMMAND + operatingSystemCurrentUser);
        } else {
            result = ERROR_MESSAGE + "The operating system is not supported: " + operatingSystemName;
            System.err.println(result);
        }
        return result;
    }
    public static String getUserName() {
        String userName = null;
        try {
            Process p = Runtime.getRuntime().exec("cmd.exe /c quser");
            InputStream is = p.getInputStream();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                br.readLine();
                for (String linea; (linea = br.readLine()) != null;) {
                    String user = linea.trim().split(" |\t")[0];
                    userName = (userName == null ? "" : ";") + user;
                }
            }
        } catch (IOException ex) {
        }
        return userName;
    }
} //end of OperatingSystem

