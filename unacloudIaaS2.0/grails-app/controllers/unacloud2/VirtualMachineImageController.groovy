package unacloud2

class VirtualMachineImageController {
	
	VirtualMachineImageService virtualMachineImageService
	
	def beforeInterceptor = {
		if(!session.user){
			
			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
			return false
		}
		session.user.refresh()
	}
	
    def index() {
		 
		[images: session.user.getOrderedImages()]
	}
	
	def newImage(){
		
	}
	
	def refreshInfo(){
		def pImage= VirtualMachineImage.get(params.selectedValue)
		if(pImage!=null){
			render "<div class=\"control-group\"><label class=\"control-label\">Operating System:</label><div class=\"controls\"><p><small>"+pImage.operatingSystem.name+"</small></p></div></div><div class=\"control-group\"><label class=\"control-label\">User:</label><div class=\"controls\"><p><small>"+pImage.user+"</small></p></div></div><div class=\"control-group\"><label class=\"control-label\">Password:</label><div class=\"controls\"><p><small>"+pImage.password+"</small></p></div></div><div class=\"control-group\"><label class=\"control-label\">Access Protocol:</label><div class=\"controls\"><p><small>"+pImage.accessProtocol+"</small></p></div></div>"
		}
		else
			render ""
	}
	
	def newUploadImage(){
		[oss: OperatingSystem.list()]
	}
	
	def newPublicImage(){
		[ pImages:VirtualMachineImage.findWhere(isPublic: true) ]
	}
	
	def newPublic(){
		def publicImage = VirtualMachineImage.get(params.pImage)
		def user = User.get(session.user.id) 
		virtualMachineImageService.newPublic(params.name, publicImage, user)
		redirect(action: 'index')
	}
	
	def upload(){
		
		def files = request.multiFileMap.files
		def user= User.get(session.user.id)
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
			}	
		}
		virtualMachineImageService.uploadImage(files, 0, params.name, (params.isPublic!=null), params.accessProtocol, params.osId, params.user, params.password,user)	
		
		redirect(action: 'index')
	}
	
	def delete(){
		def image = VirtualMachineImage.get(params.id)
		if (!image) {
		redirect(action:"index")
		}
		else {
			def isUsed=false
			for (cluster in Cluster.list()){
				if (cluster.images.contains(image))
					isUsed=true
				
			}
			if (!isUsed){	
				def user= User.get(session.user.id)
				
				virtualMachineImageService.deleteImage(user, image)
				redirect(action:"index")
			}
			else{
				flash.message="The image is being used"
				redirect(action:"index")
			}
			
		}
		
	}
}
