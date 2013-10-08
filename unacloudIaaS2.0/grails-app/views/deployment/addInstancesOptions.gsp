<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
   <body>
   	<div class="hero-unit span9" >
   		<g:form name="instancesDeploy" class="form-horizontal" controller="deployment" action="addInstances" >
   			<input type= hidden name="id" value="${id}" >
   			<div id="remaining" class="alert alert-info">
   			<table>
   			<tr>
   			<td rowspan="2">
   			<i class="icon-exclamation-sign"></i>&nbsp;&nbsp;&nbsp;
   			</td>
   			<td>
   			<label class="info">Remaining Common Instances: ${limit}</label>
   			</td>
   			</tr>
   			<tr>
   			<td>
   			<label> Remaining High Avaliability Instances: ${limitHA }</label>
   			</td>
   			</tr>
   			</table>
   			</div>
   			<br>
   			<div class="control-group">
   			<label class="control-label">Number of Instances</label>
	    		<div class="controls">
	    			<input type="text" name="instances"> 
	    		</div>
    		</div>
    		<div class="control-group">
   			<label class="control-label">Execution Time</label>
	    		<div class="controls">
	    			<select name="time" class="input-small">
    					<option value="${60*60*1000}">1 hour</option>
    					<option value="${2*60*60*1000}">2 hours</option>
    				</select>
	    		</div>
    		</div>
    		
    		<div class="controls">
  			<g:submitButton name="addInstances" class="btn" value="Deploy" />
   			</div>
   		</g:form>
   	</div>
   </body>
</html>