package unacloud2

class OperatingSystemService {

    def addOS(name, configurer){
		def o= new OperatingSystem(name: name , configurer:configurer )
		o.save()
		
	}
	def deleteOS(OperatingSystem os){
		os.delete()
		
	}
	
	def setValues(OperatingSystem os, name, configurer){
		os.putAt("name", name)
		os.putAt("configurer", configurer)
	}
}
