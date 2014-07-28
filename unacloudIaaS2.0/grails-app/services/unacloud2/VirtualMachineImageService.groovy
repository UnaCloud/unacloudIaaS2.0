package unacloud2

import java.awt.Image;
import java.nio.file.Path

import org.apache.commons.io.FileUtils
import org.junit.internal.runners.statements.FailOnTimeout;

class VirtualMachineImageService {
	
	//-----------------------------------------------------------------
	// Properties
	//-----------------------------------------------------------------
	
	/**
	 * System separator 
	 */
	def separator =  java.io.File.separatorChar
	
	//-----------------------------------------------------------------
	// Methods
	//-----------------------------------------------------------------
	
	/**
	 * Gets the image given its id
	 * @param id id of the image
	 * @return image requested if exists
	 */
	public VirtualMachineImage getImage(long id){
		return VirtualMachineImage.get(id)
	}
    
	/**
	 * Sets new values for the image
	 * @param image image to be edited
	 * @param name new image name
	 * @param user new belonging user
	 * @param password new image password
	 */
	
	def setValues(VirtualMachineImage image, name, user, password){
		image.putAt("name", name)
		image.putAt("user", user)
		image.putAt("password", password)
	}
	
	/**
	 * Changes image files for the files uploaded by user
	 * @param i image to be edited
	 * @param files new set of files
	 * @param user owner user 
	 */
	
	def updateFiles(VirtualMachineImage i, files, User user){
		new java.io.File(i.mainFile).getParentFile().deleteDir()
		def repository= Repository.findByName("Main Repository")
		files.each {
			def e=it.getOriginalFilename()
			java.io.File newFile= new java.io.File(repository.root+i.name+"_"+user.username+separator+it.getOriginalFilename()+separator)
			newFile.mkdirs()
			it.transferTo(newFile)
			if(i.isPublic){
			def templateFile= new java.io.File(repository.root+"imageTemplates"+separator+i.name+separator+it.getOriginalFilename())
			FileUtils.copyFile(newFile, templateFile)
			}
			if (e.endsWith(".vmx")||e.endsWith(".vbox"))
			i.putAt("mainFile", repository.root+i.name+"_"+user.username+separator+it.getOriginalFilename())
			i.putAt("imageVersion", i.imageVersion++)
		}
		
	}
	
	/**
	 * Uploads a new image
	 * @param files image files
	 * @param diskSize image disk size
	 * @param name image name
	 * @param isPublic indicates if the image will be uploaded as a public image
	 * @param accessProtocol indicates the access protocol configured in the image
	 * @param operatingSystemId image OS
	 * @param username image access user
	 * @param password image access password
	 * @param user owner user
	 */
	
	def uploadImage(files, diskSize, name, isPublic, accessProtocol, operatingSystemId, username, password,User user) {
		
		//TODO define repository assignment schema
		def repository= Repository.findByName("Main Repository")
		def i= new VirtualMachineImage( fixedDiskSize: diskSize, name: name , avaliable: true,
			isPublic: isPublic, imageVersion: 0,accessProtocol: accessProtocol , operatingSystem: OperatingSystem.get(operatingSystemId),
			user: username, password: password)
		i.save(failOnError: true)
		files.each {
			def e=it.getOriginalFilename()
			java.io.File newFile= new java.io.File(repository.root+i.name+"_"+user.username+separator+it.getOriginalFilename()+separator)
			newFile.mkdirs()
			it.transferTo(newFile)
			if(i.isPublic){
			def templateFile= new java.io.File(repository.root+"imageTemplates"+separator+i.name+separator+it.getOriginalFilename())
			FileUtils.copyFile(newFile, templateFile)
			}
			if (e.endsWith(".vmx")||e.endsWith(".vbox"))
			i.putAt("mainFile", repository.root+i.name+"_"+user.username+separator+it.getOriginalFilename())
		
		}
		
		if(user.images==null)
			user.images
		user.images.add(i)
		user.save()
		if(repository.images==null)
			repository.images
		repository.images.add(i)
		repository.save()
    }
	
	/**
	 * Deletes the virtual machine image
	 * @param user owner user
	 * @param repository image repository 
	 * @param image image to be removed
	 */
	
	def deleteImage(User user, Repository repository, VirtualMachineImage image){
		for(depImage in DeployedImage.getAll()){
			if(depImage.image.equals(image)){
				println "borrando depImage"
				depImage.putAt("image", null)
			}
		}
		repository.images.remove(image)
		repository.save()
		user.images.remove(image)
		user.save()
		image.delete()
	}
	
	/**
	 * Creates a new image based on a public one
	 * @param name image name
	 * @param publicImage public image used as template
	 * @param user owner user
	 */
	
	def newPublic(name, VirtualMachineImage publicImage, User user){
		def i= new VirtualMachineImage( fixedDiskSize: 0, imageVersion: 0,name: name, isPublic: false, accessProtocol: publicImage.accessProtocol ,operatingSystem: publicImage.operatingSystem, user: publicImage.user, password: publicImage.password)
		i.save(onFailError:true)
		java.io.File folder= new java.io.File(publicImage.mainFile.substring(0, publicImage.mainFile.lastIndexOf(separator)))
		println folder.toString()
		//TODO define repository assignment schema
		def repository= Repository.findByName("Main Repository")
		folder.listFiles().each
		{
		def file= new java.io.File(repository.root+"imageTemplates"+separator+publicImage.name+separator+it.getName())
		def newFile= new java.io.File(repository.root+i.name+"_"+user.username+separator+it.getName())
		FileUtils.copyFile(file, newFile)
		if (it.getName().endsWith(".vmx"))
		i.putAt("mainFile", repository.root+i.name+"_"+user.username+separator+newFile.getName())
		}
		if(user.images==null)
			user.images
		user.images.add(i)
		user.save()
		if(repository.images==null)
			repository.images
		repository.images.add(i)
		repository.save()
	}
}
