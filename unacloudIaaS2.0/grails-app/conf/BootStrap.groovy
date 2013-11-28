import fileManager.DataServerSocket;
import unacloud2.IP
import unacloud2.IPPool;
import unacloud2.Laboratory;
import unacloud2.NetworkQualityEnum;
import unacloud2.OperatingSystem;
import unacloud2.ServerVariable
import unacloud2.ServerVariableTypeEnum
import unacloud2.User


class BootStrap {

    def init = { servletContext ->
		if(User.count() ==0){
			new User(name:'Guest',username:'admin',password:'admin', userType: 'Administrator').save()
		}
		println "starting ss"
		DataServerSocket.startServices();
		if(Laboratory.count() ==0){
			IPPool virtualIpPool = new IPPool( virtual: false, gateway: '157.253.202.1', mask: '255.255.255.0').save()
			virtualIpPool.ips= []
			for(int i=0;i<39;i++){
				def virtualIp= new IP(used:false, ip: ('157.253.202.'+(111+i)), ipPool: virtualIpPool).save()	
				virtualIpPool.ips.add(virtualIp)
			}
			virtualIpPool.save()
			new Laboratory( virtualMachinesIPs: virtualIpPool, name: 'Wuaira 1', highAvaliability: false, networkQuality: NetworkQualityEnum.ETHERNET100MBPS).save()	
		
	    }
		
		if(OperatingSystem.count() ==0){
			new OperatingSystem(name:'Windows 7',configurer:'Windows').save()
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
		}
		
	}
    def destroy = {
    }
}
