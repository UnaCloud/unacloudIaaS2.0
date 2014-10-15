package unacloud2

import grails.transaction.Transactional

@Transactional
class ExternalCloudProviderService {
	
	
    def addProvider(String name, String endpoint) {
		return new ExternalCloudProvider(name: name, endpoint:endpoint).save(failOnError:true)
    }
	
	/**
	 * Deletes the selected Provider
	 * @param p Provider to be deleted
	 */
	
	def deleteProvider(ExternalCloudProvider p){
		p.delete()
		
	}
	
	/**
	 * Edits the given OS
	 * @param os OS to be edited
	 * @param name OS new name
	 * @param configurer OS new configurer class
	 */
	
	def setValues(ExternalCloudProvider p, name, endpoint){
		p.putAt("name", name)
		p.putAt("endpoint", endpoint)
	}
	
}
