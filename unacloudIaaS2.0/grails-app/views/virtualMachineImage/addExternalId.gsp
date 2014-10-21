<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
      <link href="https://rawgithub.com/hayageek/jquery-upload-file/master/css/uploadfile.css" rel="stylesheet">
	  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	  <script src="https://rawgithub.com/hayageek/jquery-upload-file/master/js/jquery.uploadfile.min.js"></script>
   </head>
   <body>
   	<div class="hero-unit span9" >
   		<g:form class="form-horizontal" controller="virtualMachineImage" action="setExternalId" enctype="multipart/form-data" >
   			
    		<div class="control-group">
   			<label class="control-label">External Image Id</label>
	    		<div class="controls">
	    			<input name="externalId" type="text" value="${(image.externalId==null)?'':(image.externalId) }">
	    			<input name="id" type="hidden" value="${image.id}"/>
	    		</div>
    		</div>
    		<div class="controls">
  			<g:submitButton name="createUser" class="btn" value="Submit" />
   			</div>
   			
   		</g:form>
   	</div>
   	
   </body>   
   <script>
	$(document).ready(function()
	{
		$("#fileuploader").uploadFile({
			fileName:"myfile"
			maxChunkSize: 1000000
		});
	});
	</script>
</html>
