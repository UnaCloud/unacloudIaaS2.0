package unacloudservices

import communication.ClouderClientAttention;

class UnaCloudServicesController {

    def clouderClientAttention(){
		String request=params.req
		println request
		println params.type
		render "hola2"
		//render ClouderClientAttention.attendRequest(request)
	}
	def agentVersion(){
		render "2.0.0"
	}
}
