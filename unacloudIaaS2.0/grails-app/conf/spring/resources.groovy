// Place your Spring DSL code here
beans = {
	multipartResolver(org.springframework.web.multipart.commons.CommonsMultipartResolver){
		
				// Max in memory 100kbytes
				maxInMemorySize=1024
		
				//1Gb Max upload size
				maxUploadSize=9192000000
				
				//uploadTempDir="/tmp"
		
			}
	
}
