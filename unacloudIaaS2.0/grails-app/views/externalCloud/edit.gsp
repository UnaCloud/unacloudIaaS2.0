<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
   <body>
   	<div class="hero-unit span9" >
   		<g:form name="extCloudAccountCreate" class="form-horizontal" controller="externalCloud" action="setValues" >
   			<div class="control-group">
   			<label class="control-label">Name</label>
	    		<div class="controls">
	    			<input name="name" type="text" value="${provider.name}">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Endpoint</label>
	    		<div class="controls">
	    			<input name="endpoint" type="text" value="${provider.endpoint}">	
	    		</div>
    		</div>
    		<div class="controls">
  			<g:submitButton name="createUser" class="btn" value="Create" />
   			</div>
   			<g:hiddenField name="provider_id" value="${provider.id}"/>
   		</g:form>
   	</div>
   </body>
</html>