package unacloud2

import grails.transaction.Transactional

@Transactional
class ExternalCloudAccountService {

    def addAccount(name, provider, account_id, account_key) {
		return new ExternalCloudAccount(name: name, provider: provider, account_id:account_id, account_key:account_key).save(failOnError: true)
    }
	
	def deleteAccount(ExternalCloudAccount a){
		a.delete(FailOnError: true)
	}
	
	def setValues(ExternalCloudAccount a, name, provider, account_id, account_key){
		a.putAt('name', name)
		a.putAt("provider", provider)
		a.putAt("account_id", account_id)
		a.putAt("account_key", account_key)
	}
}
