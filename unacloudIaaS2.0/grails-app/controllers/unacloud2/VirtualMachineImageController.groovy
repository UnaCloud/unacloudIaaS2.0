package unacloud2

class VirtualMachineImageController {
	
	def imagePath = 'C:\\images\\'
	
	def beforeInterceptor = {
		if(!session.user){
			
			flash.message="You must log in first"
			redirect(uri:"/", absolute:true)
			return false
		}
		else if(!session.user.isAttached())
			session .user.attach()		
	}
	
    def index() {
		 
		[images: session.user.images]
	}
	
	def newImage(){
		
	}
	
	def newUploadImage(){
		[oss: OperatingSystem.list()]
	}
	
	def newPublic(){
		
	}
	
	def upload(){
		
		def files = request.multiFileMap.files
		
		def i= new VirtualMachineImage( fixedDiskSize: 0, volume: "", name: params.name, customizable: (params.customizable!=null),
			isPublic: (params.isPublic!= null), operatingSystem: OperatingSystem.get(params.osId), user: params.user, password: params.password)
		i.save()
		
		files.each {
			if(it.isEmpty()){
			flash.message = 'file cannot be empty'
			render(view: 'newUploadImage')
			}
			
			else{ 
				def e=it.getOriginalFilename()
				if(!(e.endsWith("vmx")|| e.endsWith("vmdk")) ){
					flash.message = 'invalid file type'
					render(view: 'newUploadImage')
				}
				else{
				java.io.File newFile= new java.io.File(imagePath+i.name+"_"+session.user.username+"\\"+it.getOriginalFilename())
				newFile.mkdirs()
				it.transferTo(newFile)
				def f= new File( fileName: (it.getOriginalFilename()), route: (imagePath+it.getOriginalFilename()))
				f.image= i
				f.save()
				}
			}
		}
		if(session.user.images==null)
			session.user.images
		session.user.images.add(i)
		session.user.save()
		
		redirect(action: 'index')
	}
}
