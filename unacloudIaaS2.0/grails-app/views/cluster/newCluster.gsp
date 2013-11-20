<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
   <body>
   	<div class="hero-unit span9" >
   		<g:form name="clusterCreate" class="form-horizontal" controller="cluster" action="save" >
   			<div class="control-group">
   			<label class="control-label">Cluster Name</label>
	    		<div class="controls">
	    			<input name="name" type="text">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Images</label>
	    		<div class="controls">
	    			<select name= "images" multiple="multiple">
	  				<g:each in="${images}" status="i" var="image">
	  					<option value="${image.id}">${image.name}</option>
	  				</g:each>
	  				</select>
	    		</div>
    		</div>
    		<div class="controls">
  			<g:submitButton name="createHyp" class="btn" value="Create" />
   			</div>
   			
   		</g:form>
   	</div>
   </body>
</html>