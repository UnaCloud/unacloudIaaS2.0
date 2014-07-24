package unacloud2

import back.services.AgentService

class VirtualMachineImageController {

	//-----------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------
	
	/**
	 * Representation of image services
	 */
	
	VirtualMachineImageService virtualMachineImageService
	
	/**
	 * Representation of image servicesagent services
	 */
	
	AgentService agentService
	
	//-----------------------------------------------------------------
	// Actions
	//-----------------------------------------------------------------
	
	/**
	 * Makes session verifications before executing any action
	 */
	
	def beforeInterceptor = {
		if(!session.user){
			
			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
			return false
		}
		session.user.refresh()
	}
	
	/**
	 * Virtual machine image index action
	 * @return list of all images related to user
	 */
    def index() {
		 
		[images: session.user.getOrderedImages()]
	}
	
	/**
	 * Change image files form action. 
	 * @return id of the image to be edited.
	 */
	
	def changeVersion(){
		[id:params.id]
	}
	
	/**
	 * Deletes the image files from every physical machine. Redirects to index when
	 * finished
	 */
	
	def clearImageFromCache(){
		agentService.clearImageFromCache(VirtualMachineImage.get(params.id))
		redirect(action: 'index')
	}
	
	/**
	 * Image creation form action
	 */
	
	def newImage(){
		
	}
	
	/**
	 * New uploaded image form action 
	 * @return list of OS for user selection
	 */
	
	def newUploadImage(){
		[oss: OperatingSystem.list()]
	}
	
	/**
	 * Generates the form in order to create a new image based on a public image
	 * @return
	 */
	
	def newPublicImage(){
		[ pImages:VirtualMachineImage.findWhere(isPublic: true) ]
	}
	
	/**
	 * Renders image info when a public image is selected in creation form
	 */
	
	def refreshInfo(){
		def pImage= VirtualMachineImage.get(params.selectedValue)
		if(pImage!=null){
			render "<div class=\"control-group\"><label class=\"control-label\">Operating System:</label><div class=\"controls\"><p><small>"+pImage.operatingSystem.name+"</small></p></div></div><div class=\"control-group\"><label class=\"control-label\">User:</label><div class=\"controls\"><p><small>"+pImage.user+"</small></p></div></div><div class=\"control-group\"><label class=\"control-label\">Password:</label><div class=\"controls\"><p><small>"+pImage.password+"</small></p></div></div><div class=\"control-group\"><label class=\"control-label\">Access Protocol:</label><div class=\"controls\"><p><small>"+pImage.accessProtocol+"</small></p></div></div>"
		}
		else
			render ""
	}
	
	/**
	 * Save new public image. Redirects to index when finished
	 */
	
	def newPublic(){
		def publicImage = VirtualMachineImage.get(params.pImage)
		def user = User.get(session.user.id) 
		virtualMachineImageService.newPublic(params.name, publicImage, user)
		redirect(action: 'index')
	}
	
	/**
	 * Validates file parameters are correct and save new uploaded image. Redirects 
	 * to index when finished or renders an error message if uploaded 
	 * files are not valid. 
	 */
	
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
			if(!(e.endsWith("vmx")|| e.endsWith("vmdk")||e.endsWith("vbox")|| e.endsWith("vdi"))){
				flash.message = 'invalid file type'
				render(view: 'newUploadImage')
			}
			}	
		}
		virtualMachineImageService.uploadImage(files, 0, params.name, (params.isPublic!=null), params.accessProtocol, params.osId, params.user, params.password,user)	
		redirect(action: 'index')
	}
	
	/**
	 * Validates file parameters are correct and save new files for the image. 
	 * Redirects to index when finished or renders an error message if uploaded 
	 * files are not valid. 
	 */
	
	def updateFiles(){
		VirtualMachineImage i= VirtualMachineImage.get(params.id)
		def files = request.multiFileMap.files
		def user= User.get(session.user.id)
		if (i!= null){
			files.each {
			if(it.isEmpty()){
				flash.message = 'file cannot be empty'
				render(view: 'newUploadImage')
			}
			else{
			def e=it.getOriginalFilename()
			if(!(e.endsWith("vmx")|| e.endsWith("vmdk")||e.endsWith("vbox")|| e.endsWith("vdi"))){
				flash.message = 'invalid file type'
				render(view: 'newUploadImage')
			}
			}
			}
			
			virtualMachineImageService.updateFiles(i,files,user)
			redirect(action: "index")
		}
	}
	
	/**
	 * Edit image information form action. 
	 * @return image id to be edited
	 */
	
	def edit(){
		def i= VirtualMachineImage.get(params.id)
		
		if (!i) {
			redirect(action:"index")
		}
		else{
			[image: i]
		}
	}
	
	/**
	 * Save image information changes. Redirect6s to index when finished
	 */
	
	def setValues(){
		def image = VirtualMachineImage.get(params.id)
		virtualMachineImageService.setValues(image,params.name,params.user,params.password)
		redirect(action:"index")
	}
	
	
	/**
	 * Verifies if the image is being used then acquires the repository when it is
	 * deposited and send deletion request. Redirects to index when finished or to 
	 * and error message if the validation processes failed
	 */
	
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
				Repository repository
				for(repo in Repository.all) {
					for (repoImage in repo.images){
						if (repoImage.equals(image)){
							repository= repo
							break
						}
					}
					if (repository.equals(repo))
						break
				}
				virtualMachineImageService.deleteImage(user,repository, image)
				redirect(action:"index")
			}
			else{
				flash.message="The image is being used"
				redirect(action:"index")
			}
			
		}
		
	}
}
