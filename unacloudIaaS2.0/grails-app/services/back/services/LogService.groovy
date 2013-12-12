package back.services

import unacloud2.Log

class LogService {

    def createLog(String origin, String component, String message) {
		new Log(origin: origin, component:component, message:message, timestamp: new Date()).save()
    }
}
