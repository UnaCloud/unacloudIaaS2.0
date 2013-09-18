<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
   <body>
   	<div class="hero-unit span9" >
   		<g:uploadForm name="imageNewPublic" class="form-horizontal" controller="virtualMachineImage" action="newPublic" >
   			<div class="control-group">
   			<label class="control-label">Image Name</label>
	    		<div class="controls">
	    			<input name="name" type="text">
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Public Image</label>
	    		<div class="controls">
	    			<select name= "pImage" onchange="refreshFields(this.value)">
	  				<option value="0">--Select a Public Image--</option>
	    			
	  				<g:each in="${pImages}" status="i" var="pImage">
	  					<option value="${pImage.id}">${pImage.name }</option>
	  				</g:each>
	  				</select>
	    		</div>
    		</div>
    		<div id="info"></div>	
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
   		<script>
            function refreshFields(value) {
                <g:remoteFunction controller="virtualMachineImage" action="refreshInfo" update="info" params="'selectedValue='+value"/>
                
            }
        </script>
   </body>
</html>