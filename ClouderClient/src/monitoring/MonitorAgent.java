package monitoring;

/**
 *
 * @author jcadavid
 */
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import physicalmachine.MachineMonitor;

public class MonitorAgent{
	private DatabaseConnection conexionBD = new DatabaseConnection();

	void CargarDatosIniciales() throws SQLException, ParseException, IOException {
		conexionBD.conectar();
		RegistrarDatosIniciales();
		iniciarRegistroEnDB();
	}

	public void iniciarMonitoreo(int frecuency, int registerFrecuency) throws IOException, InterruptedException, SQLException, ParseException, InstantiationException, IllegalAccessException {
		if (frecuency < 10) frecuency = 10;
		if (registerFrecuency < 10) registerFrecuency = 10;

		frecuency *= 1000;
		registerFrecuency *= 1000;

		conexionBD.conectar();
		for (int i = 0; i < frecuency; i += registerFrecuency) {
			registrar();
			MachineMonitor.monitorVirtualMachines();
			Thread.sleep(registerFrecuency);
		}
		conexionBD.RegistrarBatch();
	}

	// En este metodo se realiza un registro Inicial de los datos generales del
	// Nodo
	private void RegistrarDatosIniciales() throws SQLException, ParseException {
		MonitorInitialReport initialReport = MonitorReportGenerator.generateInitialReport();
		conexionBD.CargarBatchInicial(initialReport);
		// System.out.println("Terminando Carga de Batch Inicial");
		// salida.flush();
	}

	// En este metodo se realiza un registro full de todos los parametros
	// private void Registrar(PrintWriter salida) throws SQLException
	public void registrar() throws SQLException, ParseException, InstantiationException, IllegalAccessException, IOException {
		MonitorReport statusReport = MonitorReportGenerator.generateStateReport();
		conexionBD.CargarBatch(statusReport);
	}

	private void iniciarRegistroEnDB() throws SQLException {
		conexionBD.RegistrarBatch();
	}

}
