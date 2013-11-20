package unacloudservices

import communication.ClouderClientAttention;

class UnaCloudServicesController {

    def index() { 
		render "hola"
	}
	def clouderClientAttention(){
		String request=params.req
		println request
		println params.type
		render "hola2"
		//render ClouderClientAttention.attendRequest(request)
	}
}
