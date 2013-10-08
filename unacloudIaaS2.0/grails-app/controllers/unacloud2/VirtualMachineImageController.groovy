package unacloud2

import java.nio.file.Path

import org.apache.commons.io.FileUtils;

class VirtualMachineImageController {
	
	def imagePath = 'C:\\images\\'
	
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
		 
		def i= new VirtualMachineImage( fixedDiskSize: 0, volume: "", name: params.name, customizable: (params.customizable!=null),
			isPublic: false, accessProtocol: publicImage.accessProtocol ,operatingSystem: publicImage.operatingSystem, user: publicImage.user, password: publicImage.password) 	
		i.save()
		def files= publicImage.files
		files.each 
		{
		if (it.templateFile){
		def file= new java.io.File(imagePath+"imageTemplates\\"+publicImage.name+"_"+session.user.username+"\\"+it.fileName)
		def newFile= new java.io.File(imagePath+i.name+"_"+session.user.username+"\\"+it.fileName)
		FileUtils.copyFile(file, newFile)
		def f= new File( fileName: (it.fileName), route: (imagePath+i.name+"_"+session.user.username+"\\"+it.fileName))
		f.image= i
		f.save()
		}
		}
		User u= User.findById(session.user.id)
		if(u.images==null)
			u.images
		u.images.add(i)
		u.save()
		
		redirect(action: 'index')
	}
	
	def upload(){
		
		def files = request.multiFileMap.files
		
		def i= new VirtualMachineImage( fixedDiskSize: 0, volume: "", name: params.name , customizable: (params.customizable!=null),
			isPublic: (params.isPublic!= null), accessProtocol: params.accessProtocol , operatingSystem: OperatingSystem.get(params.osId), user: params.user, password: params.password)
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
				if(i.isPublic){
				def templateFile= new java.io.File(imagePath+"imageTemplates\\"+i.name+"_"+session.user.username+"\\"+it.getOriginalFilename())
				FileUtils.copyFile(newFile, templateFile)
				def tf= new File(fileName: (it.getOriginalFilename()), route: (imagePath+"imageTemplates\\"+i.name+"_"+session.user.username+"\\"+it.getOriginalFilename()), templateFile: true)
				tf.image= i
				tf.save()
				}
				def f= new File( fileName: (it.getOriginalFilename()), route: (imagePath+i.name+"_"+session.user.username+"\\"+it.getOriginalFilename()), templateFile: false)
				f.image= i
				f.save()
				}
			}
		}
		User u= User.findById(session.user.id)
		if(u.images==null)
			u.images
		u.images.add(i)
		u.save()
		
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
				user.images.remove(image)
				user.save()
				image.delete()
				redirect(action:"index")
			}
			else{
				flash.message="The image is being used"
				redirect(action:"index")
			}
			
		}
		
	}
}
