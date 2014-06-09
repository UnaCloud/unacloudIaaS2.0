<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
   <body>
   	<div class="hero-unit span9" >
   		<g:form name="machineCreate" class="form-horizontal" controller="laboratory" action="createLab" >
   			<div class="control-group">
   			<label class="control-label">Laboratory Name</label>
	    		<div class="controls">
	    			<input name="name" type="text">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">High Availability</label>
	    		<div class="controls">
	    			<input name="highAvailability" type="checkbox">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Network Quality</label>
	    		<div class="controls">
	    			<select name= "netConfig">
	  				<g:each in="${netConfigurations}" status="i" var="netConf">
	  					<option value="${netConf}">${netConf}</option>
	  				</g:each>
	  				</select>
	    		</div>
    		</div>
    		
    		<div class="control-group">
    		<label class="control-label">Private Network</label>
	    		<div class="controls">
	    			<input name="virtual" type="checkbox">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Network gateway</label>
	    		<div class="controls">
	    			<input name="netGateway" type="text">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Network mask</label>
	    		<div class="controls">
	    			<input name="netMask" type="text">
	    		</div>
    		</div>
    		<div class="controls">
  			<g:submitButton name="create" class="btn" value="Create" />
   			</div>
   		</g:form>
   	</div>
   </body>
</html>