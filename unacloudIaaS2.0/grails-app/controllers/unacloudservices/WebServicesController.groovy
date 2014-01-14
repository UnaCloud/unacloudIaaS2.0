package unacloudservices

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
class WebServicesController{
	static responseFormats = ['json', 'xml']

    def index(int vmSize) {
		render "holamundo";
	}
}
