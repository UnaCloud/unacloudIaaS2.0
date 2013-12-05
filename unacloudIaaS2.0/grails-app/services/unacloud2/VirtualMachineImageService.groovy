package unacloud2

import java.awt.Image;
import java.nio.file.Path

import org.apache.commons.io.FileUtils
import org.junit.internal.runners.statements.FailOnTimeout;

class VirtualMachineImageService {
	
	public VirtualMachineImage getImage(long id){
		return VirtualMachineImage.get(id)
	}
    
	def uploadImage(files, diskSize, name, isPublic, accessProtocol, operatingSystemId, username, password,User user) {
		
		//TODO define repository assignment schema
		def repository= Repository.findByName("Main Repository")
		def i= new VirtualMachineImage( fixedDiskSize: diskSize, name: name , avaliable: true,
			isPublic: isPublic, accessProtocol: accessProtocol , operatingSystem: OperatingSystem.get(operatingSystemId),
			user: username, password: password)
		i.save(failOnError: true)
		files.each {
			def e=it.getOriginalFilename()
			
			java.io.File newFile= new java.io.File(repository.root+i.name+"_"+user.username+"\\"+it.getOriginalFilename())
			newFile.mkdirs()
			it.transferTo(newFile)
			if(i.isPublic){
			def templateFile= new java.io.File(repository.root+"imageTemplates\\"+i.name+"_"+user.username+"\\"+it.getOriginalFilename())
			FileUtils.copyFile(newFile, templateFile)
			if (e.endsWith(".vmx"))
				i.putAt("mainFile", repository.root+i.name+"_"+user.username+"\\"+it.getOriginalFilename())
			}
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
	
	def deleteImage(User user, Repository repository, VirtualMachineImage image){
		
		user.images.remove(image)
		user.save()
		repository.images.remove(image)
		user.save()
		image.delete()
	}
	
	def newPublic(name, VirtualMachineImage publicImage, User user){
		def i= new VirtualMachineImage( fixedDiskSize: 0, name: name, isPublic: false, accessProtocol: publicImage.accessProtocol ,operatingSystem: publicImage.operatingSystem, user: publicImage.user, password: publicImage.password)
		i.save(onFailError:true)
		java.io.File folder= new java.io.File(publicImage.mainFile.substring(0, publicImage.mainFile.lastIndexOf("\\")))
		println folder.toString()
		//TODO define repository assignment schema
		def repository= Repository.findByName("Main Repository")
		folder.listFiles().each
		{
		def file= new java.io.File(repository.root+"imageTemplates\\"+publicImage.name+"_"+user.username+"\\"+it.getName())
		def newFile= new java.io.File(repository.root+i.name+"_"+user.username+"\\"+it.getName())
		FileUtils.copyFile(file, newFile)
		if (it.getName().endsWith(".vmx"))
		i.putAt("mainFile", repository.root+i.name+"_"+user.username+"\\"+newFile.getName())
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
