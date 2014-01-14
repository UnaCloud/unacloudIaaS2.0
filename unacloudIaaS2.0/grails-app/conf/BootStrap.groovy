import back.services.DatabaseService;
import back.services.VariableManagerService;

import com.losandes.utils.Constants;
import com.losandes.utils.VariableManager;

import fileManager.DataServerSocket;
import unacloud2.Hypervisor;
import unacloud2.IP
import unacloud2.IPPool;
import unacloud2.Laboratory;
import unacloud2.NetworkQualityEnum;
import unacloud2.OperatingSystem;
import unacloud2.PhysicalMachine;
import unacloud2.PhysicalMachineStateEnum;
import unacloud2.ServerVariable
import unacloud2.ServerVariableTypeEnum
import unacloud2.User
import unacloud2.Repository

class BootStrap {
	DatabaseService databaseService
	VariableManagerService variableManagerService
	
	def init = { servletContext ->
		if(User.count() ==0){
			new User(name:'Guest',username:'admin',password:'admin', userType: 'Administrator').save()
		}
		if(Laboratory.count() ==0){
			def win7=new OperatingSystem(name:'Windows 7',configurer:'Windows');
			win7.save()
			wayra1:{
				IPPool virtualIpPool = new IPPool( virtual: false, gateway: '157.253.202.1', mask: '255.255.255.0').save()
				virtualIpPool.ips= []
				for(int i=0;i<30;i++){
					def virtualIp= new IP(used:false, ip: ('157.253.202.'+(111+i)), ipPool: virtualIpPool).save()
					virtualIpPool.ips.add(virtualIp)
				}
				virtualIpPool.save()
				def labWaira1 = new Laboratory( virtualMachinesIPs: virtualIpPool, name: 'Wuaira 1', highAvaliability: false, networkQuality: NetworkQualityEnum.ETHERNET100MBPS);
				labWaira1.physicalMachines = []
				for(int e=0;e<30;e++){
					def ip=new IP(used:false, ip: ('157.253.202.'+(11+e)));
					ip.save()
					def pm=new PhysicalMachine(cores: 4, highAvaliability: false, hypervisorPath: "/", ip: ip, name: "ISC"+(201+e), mac: "AA:BB:CC:DD:EE", ram: 8192, withUser: false, state: PhysicalMachineStateEnum.OFF, operatingSystem: win7, laboratory: labWaira1, lastReport: new Date(0));
					labWaira1.physicalMachines.add(pm)
				}
				labWaira1.save()
			}
			wayra2:{
				IPPool virtualIpPool = new IPPool( virtual: false, gateway: '157.253.239.1', mask: '255.255.255.0').save()
				virtualIpPool.ips= []
				for(int i=0;i<30;i++){
					def virtualIp= new IP(used:false, ip: ('157.253.239.'+(111+i)), ipPool: virtualIpPool).save()
					virtualIpPool.ips.add(virtualIp)
				}
				virtualIpPool.save()
				def labWaira2 = new Laboratory( virtualMachinesIPs: virtualIpPool, name: 'Wuaira 2', highAvaliability: false, networkQuality: NetworkQualityEnum.ETHERNET100MBPS);
				labWaira2.physicalMachines = []
				for(int e=0;e<30;e++){
					def ip=new IP(used:false, ip: ('157.253.239.'+(11+e)));
					ip.save();
					def pm=new PhysicalMachine(cores: 4, highAvaliability: false, hypervisorPath: "/", ip: ip, name: "ISC"+(301+e), mac: "AA:BB:CC:DD:EE", ram: 8192, withUser: false, state: PhysicalMachineStateEnum.OFF, operatingSystem: win7, laboratory: labWaira2, lastReport: new Date(0));
					labWaira2.physicalMachines.add(pm)
				}
				labWaira2.save()
			}
			
	    }
		if(Repository.count()==0){
			new Repository(name: "Main Repository", capacity: 20, root: "C:\\images\\").save();
		}
		if(OperatingSystem.count() == 1){
			new OperatingSystem(name:'Windows 8',configurer:'Windows').save()
			
			new OperatingSystem(name:'Windows XP',configurer:'Windows').save()
			new OperatingSystem(name:'Debian 6',configurer:'Debian').save();
			new OperatingSystem(name:'Debian 7',configurer:'Debian').save();
			new OperatingSystem(name:'Debian 8',configurer:'Debian').save();
			new OperatingSystem(name:'Ubuntu 10',configurer:'Ubuntu').save();
			new OperatingSystem(name:'Ubuntu 11',configurer:'Ubuntu').save();
			new OperatingSystem(name:'Scientific Linux',configurer:'Linux').save();
		}
		if(ServerVariable.count() ==0){
			new ServerVariable(name:'CLOUDER_SERVER_PORT',serverVariableType: ServerVariableTypeEnum.INT,variable:'26').save()
			new ServerVariable(name:'CLOUDER_CLIENT_PORT',serverVariableType: ServerVariableTypeEnum.INT,variable:'81').save()
			new ServerVariable(name:'DATA_SOCKET',serverVariableType: ServerVariableTypeEnum.INT,variable:'25').save()
			new ServerVariable(name:'FILE_TRANSFER_SOCKET',serverVariableType: ServerVariableTypeEnum.INT,variable:'1575').save()
			new ServerVariable(name:'LOG_SOCKET',serverVariableType: ServerVariableTypeEnum.INT,variable:'1576').save()
			new ServerVariable(name:'MONITOR_FREQUENCY',serverVariableType: ServerVariableTypeEnum.INT,variable:'60').save()
			new ServerVariable(name:'MONITOR_REGISTER_FREQUENCY',serverVariableType: ServerVariableTypeEnum.INT,variable:'300').save()
			new ServerVariable(name:'VERSION_MANAGER_PORT',serverVariableType: ServerVariableTypeEnum.INT,variable:'2003').save()
			new ServerVariable(name:'CLOUDER_SERVER_IP',serverVariableType: ServerVariableTypeEnum.STRING,variable:'157.253.236.160').save()
			new ServerVariable(name:'MONITORING_DATABASE_NAME',serverVariableType: ServerVariableTypeEnum.STRING,variable:'clouderqos').save()
			new ServerVariable(name:'MONITORING_DATABASE_PASSWORD',serverVariableType: ServerVariableTypeEnum.STRING,variable:'pmonitoreo$#').save()
			new ServerVariable(name:'MONITORING_DATABASE_USER',serverVariableType: ServerVariableTypeEnum.STRING,variable:'pmonitoreo').save()
			new ServerVariable(name:'MONITORING_ENABLE',serverVariableType: ServerVariableTypeEnum.STRING,variable:'true').save()
			new ServerVariable(name:'MONITORING_SERVER_IP',serverVariableType: ServerVariableTypeEnum.STRING,variable: '157.253.236.160').save()
			new ServerVariable(name:'AGENT_VERSION',serverVariableType: ServerVariableTypeEnum.STRING,variable: '2.0.1').save()
			
			new ServerVariable(name:'SERVER_URL',serverVariableType: ServerVariableTypeEnum.STRING,variable: 'http://'+InetAddress.getLocalHost().getHostAddress()+'/Unacloud2').save()
		}
		if(Hypervisor.count() == 0){
			new Hypervisor(name: Constants.VIRTUAL_BOX, hypervisorVersion: "4.3.4").save()
			new Hypervisor(name: Constants.VM_WARE_WORKSTATION, hypervisorVersion: "10").save()
			new Hypervisor(name: Constants.VM_WARE_PLAYER, hypervisorVersion: "10").save()
			
		}
		databaseService.initDatabase()
		DataServerSocket.startServices(variableManagerService.getIntValue("DATA_SOCKET"));
		//String applicationPath = request.getSession().getServletContext().getRealPath("")
	}
    def destroy = {
    }
}
