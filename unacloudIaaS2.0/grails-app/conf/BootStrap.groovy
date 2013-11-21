import unacloud2.Laboratory;
import unacloud2.NetworkQualityEnum;
import unacloud2.OperatingSystem;
import unacloud2.User

class BootStrap {

    def init = { servletContext ->
		if(User.count() ==0){
			new User(name:'Guest',username:'admin',password:'admin', userType: 'Administrator').save()
		}
		
		if(Laboratory.count() ==0){
			new Laboratory(name: 'TestLab', highAvaliability: false,networkQuality: NetworkQualityEnum.ETHERNET100MBPS)	
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
		 
	}
    def destroy = {
    }
}
