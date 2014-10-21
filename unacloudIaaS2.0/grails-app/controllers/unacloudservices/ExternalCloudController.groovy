package unacloudservices


import unacloud2.ExternalCloudAccount;
import unacloud2.ExternalCloudAccountService
import unacloud2.ExternalCloudProvider;
import unacloud2.ExternalCloudProviderService;
import back.services.ExternalCloudCallerService

class ExternalCloudController {
	
	ExternalCloudCallerService externalCloudCallerService
	ExternalCloudProviderService externalCloudProviderService
	ExternalCloudAccountService externalCloudAccountService
	
	def beforeInterceptor = {
		if(!session.user){
			flash.message="You must log in first"
			redirect(uri:"/login", absolute:true)
			return false
		}
		else if(!(session.user.userType.equals("Administrator"))){
			flash.message="You must be administrator to see this content"
			redirect(uri:"/error", absolute:true)
			return false
		}
	}
	
	def index() {
		[providers: ExternalCloudProvider.list()]
	}
	
	def accounts(){
		[accountList: ExternalCloudAccount.list()]
	}
	
	
	def create(){
		
	}
	
	def createAccount(){
		[providers: ExternalCloudProvider.list()]
	}
	
	def add(){
		externalCloudProviderService.addProvider(params.name, params.endpoint, params.type)
		redirect(action:"index")
	}
	
	def addAccount(){
		ExternalCloudProvider p= ExternalCloudProvider.get(params.provider)
		if (!p) {
			redirect(action:"accounts")
		}
		externalCloudAccountService.addAccount(params.name, p, params.account_id, params.account_key)
		redirect(action:"accounts")
	}
	
	def delete(){
		def p = ExternalCloudProvider.get(params.provider_id)
		if (!p) {
        redirect(action:"list")
		}
		else{
			externalCloudProviderService.deleteProvider(p)
			redirect(action:"index")
		}
	}
	
	def deleteAccount(){
		def a = ExternalCloudAccount.get(params.account_id)
		if (!a) {
		redirect(action:"list")
		}
		else{
			externalCloudAccountService.deleteAccount(a)
			redirect(action:"accounts")
		}
	}
	def edit(){
		def p = ExternalCloudProvider.get(params.provider_id)
		if (!p) {
        redirect(action:"list")
		}
		else{
			[provider: p]
		}
	}
	
	def editAccount(){
		def a = ExternalCloudAccount.get(params.account_id)
		if (!a) {
		redirect(action:"accounts")
		}
		[account:a, providers: ExternalCloudProvider.list()]
	}
	
	def saveAccountChanges(){
		def a = ExternalCloudAccount.get(params.acc_id)
		def p = ExternalCloudProvider.get(params.provider)
		externalCloudAccountService.setValues(a,params.name,p,params.account_id, params.account_key)
		redirect(action:"accounts")
	}
	
	def setValues(){
		def p = ExternalCloudProvider.get(params.provider_id)
		externalCloudProviderService.setValues(p,params.name,params.endpoint,params.type)
		redirect(action:"index")
	}
	
}
