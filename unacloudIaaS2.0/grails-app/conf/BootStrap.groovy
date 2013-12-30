import back.services.DatabaseService;

import com.losandes.utils.Constants;

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
	def init = { servletContext ->
		if(User.count() ==0){
			new User(name:'Guest',username:'admin',password:'admin', userType: 'Administrator').save()
		}
		println "starting ss"
		DataServerSocket.startServices();
		if(Laboratory.count() ==0){
			uniandes:{
				IPPool virtualIpPool = new IPPool( virtual: false, gateway: '157.253.202.1', mask: '255.255.255.0').save()
				virtualIpPool.ips= []
				for(int i=0;i<39;i++){
					def virtualIp= new IP(used:false, ip: ('157.253.202.'+(111+i)), ipPool: virtualIpPool).save()
					virtualIpPool.ips.add(virtualIp)
				}
				virtualIpPool.save()
				new Laboratory( virtualMachinesIPs: virtualIpPool, name: 'Wuaira 1', highAvaliability: false, networkQuality: NetworkQualityEnum.ETHERNET100MBPS).save()
			}
			
	    }
		if(Repository.count()==0){
			new Repository(name: "Main Repository", capacity: 20, root: "C:\\images\\").save();
		}
		if(OperatingSystem.count() ==0){
			new OperatingSystem(name:'Windows 8',configurer:'Windows').save()
			OperatingSystem win7=new OperatingSystem(name:'Windows 7',configurer:'Windows')
			win7.save()
			new OperatingSystem(name:'Windows XP',configurer:'Windows').save()
			new OperatingSystem(name:'Debian 6',configurer:'Debian').save();
			new OperatingSystem(name:'Debian 7',configurer:'Debian').save();
			new OperatingSystem(name:'Debian 8',configurer:'Debian').save();
			new OperatingSystem(name:'Ubuntu 10',configurer:'Ubuntu').save();
			new OperatingSystem(name:'Ubuntu 11',configurer:'Ubuntu').save();
			new OperatingSystem(name:'Scientific Linux',configurer:'Linux').save();
			test:{
				IPPool virtualIpPool = new IPPool( virtual: false, gateway: '157.253.204.1', mask: '255.255.255.0').save()
				virtualIpPool.ips= []
				def virtualIp= new IP(used:false, ip: ('157.253.204.140'), ipPool: virtualIpPool).save()
				virtualIpPool.ips.add(virtualIp)
				virtualIpPool.save()
				Laboratory labLocal=new Laboratory( virtualMachinesIPs: virtualIpPool, name: 'Local Test', highAvaliability: false, networkQuality: NetworkQualityEnum.ETHERNET100MBPS)
				labLocal.save()
				new PhysicalMachine(cores: 2, highAvaliability: false, ip: new IP(used:true, ip: ('157.253.204.12')), mac: "AA:BB:CC:DD:EE", ram: 4096, state: PhysicalMachineStateEnum.OFF, operatingSystem: win7, withUser: false, name: "Asistente-PC", belongsTo: labLocal, )
			}
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
		//String applicationPath = request.getSession().getServletContext().getRealPath("")
	}
    def destroy = {
    }
}
