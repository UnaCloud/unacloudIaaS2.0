<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
   <body>
   	<div class="hero-unit span9" >
   		<g:uploadForm name="imageUpload" class="form-horizontal" controller="virtualMachineImage" action="upload" >
   			<div class="control-group">
   			<label class="control-label">Image Name</label>
	    		<div class="controls">
	    			<input name="name" type="text">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Image Location</label>
	    		<div class="controls">
	    			<input name="files" type="file" multiple>
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Operating System</label>
	    		<div class="controls">
	    			<select name= "osId">
	  				<g:each in="${oss}" status="i" var="os">
	  					<option value="${os.id}">${os.name }</option>
	  				</g:each>
	  				</select>
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">User</label>
	    		<div class="controls">
	    			<input name="user" type="text">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Password</label>
	    		<div class="controls">
	    			<input name="password" type="password">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Public</label>
	    		<div class="controls">
	    			<g:checkBox name="isPublic" value="${false}"/>
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Customizable</label>
	    		<div class="controls">
	    			<g:checkBox name="customizable" value="${false}"/>
	    		</div>
    		</div>
    		<div class="controls">
  			<g:submitButton name="createUser" class="btn" value="Submit" />
   			</div>
   			
   		</g:uploadForm>
   	</div>
   </body>
</html>