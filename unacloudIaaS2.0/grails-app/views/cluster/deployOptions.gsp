<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
   <body>
   	<div class="hero-unit span9" >
   		<g:form name="clusterDeploy" class="form-horizontal" controller="cluster" action="deploy" >
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
   			<g:each in="${images}" status="i" var="image" >
   			<div class="control-group">
   			<h5>${image.name }</h5>
    		</div>
    		<table  border="0" cellpadding="10" ">
   			<tr>
   			<td>
   			<label>Instances to deploy</label>	    		
    		</td>
    		<td>
    		<label>RAM per instance</label>    	
    		</td>
    		<td>
    		<label>Cores per instance</label>
    		</td>
    		</tr>
    		<tr>
    		<td>
    		<input name="instacesFor${image.id }" class="input-small" type="text">
	    	</td>
    		<td>
    		<select name="RAMFor${image.id }" class="input-small" > 
    			<option>
    		</select> 
    		</td>
    		<td>
    		<select name="coresFor${image.id }" class="input-small" > 
    			<option>
    		</select>
    		</td>
    		</tr>
    		<tr>
    		<td>
    		<label>Green Deployment</label>
    		</td>
    		<td>
    		<input type="checkbox" name="green">
    		</td>
    		</tr>
    		<tr>
    		<td>
    		<label>Prefer same IP pool</label>
    		</td>
    		<td>
    		<input type="checkbox" name="sameIP">
    		</td>
    		</tr>
    		</table>
    		<br>
    		</g:each>
    		
    		<div class="controls">
  			<g:submitButton name="deploy" class="btn" value="Deploy" />
   			</div>
   			
   		</g:form>
   	</div>
   </body>
</html>