package unacloud2

import java.nio.file.Path

import org.apache.commons.io.FileUtils

class VirtualMachineImageService {

    def uploadImage(files, diskSize, name, isPublic, accessProtocol, operatingSystemId, User user, password) {
		
		def i= new VirtualMachineImage( fixedDiskSize: diskSize, name: name , avaliable: true,
			isPublic: isPublic, accessProtocol: accessProtocol , operatingSystem: OperatingSystem.get(operatingSystemId),
			user: user, password: password)
		i.save()
		def imagePath= 'C:\\images\\'
		files.each {
			def e=it.getOriginalFilename()
			java.io.File newFile= new java.io.File(imagePath+i.name+"_"+user.username+"\\"+it.getOriginalFilename())
			newFile.mkdirs()
			it.transferTo(newFile)
			if(i.isPublic){
			def templateFile= new java.io.File(imagePath+"imageTemplates\\"+i.name+"_"+user.username+"\\"+it.getOriginalFilename())
			FileUtils.copyFile(newFile, templateFile)
			def tf= new File(fileName: (it.getOriginalFilename()), route: (imagePath+"imageTemplates\\"+i.name+"_"+ user.username+"\\"+it.getOriginalFilename()), templateFile: true)
			tf.image= i
			tf.save()
			}
			def f= new File( fileName: (it.getOriginalFilename()), route: (imagePath+i.name+"_"+user.username+"\\"+it.getOriginalFilename()), templateFile: false)
			f.image= i
			f.save()
			}
		
		if(user.images==null)
			user.images
		user.images.add(i)
		user.save()
    }
}
