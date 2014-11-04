<%@page import="unacloud2.PhysicalMachineStateEnum"%>
<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
<body>

<form id="form_machines">
	<div class="hero-unit span9">
	<div id="label-message"></div>
	<table class="table table-bordered"  style="background:white" >
	  <tr class="info">
	  	<td class="info" colspan="12">
	  	<input type="checkbox" id="selectAll"><small>&nbsp;Select All</small> 	
	  	
	  	<g:link controller="laboratory" action="createMachine" params="${[id: lab.id]}" title="Create PM"><i class="icon-plus-sign pull-right"></i></g:link>  	
	  	<a title="Update agents" class="updateMachines" style="cursor:pointer"><span class="icon-refresh pull-right"></span></a>	
	  	<a title="Clear VM cache" class="clearCache" style="cursor:pointer"><span class="icon-fire pull-right"></span></a>  
	  	<a title="Stop agents" class="stopMachines" style="cursor:pointer"><span class="icon-stop pull-right"></span></a>  		  	
	  	</td>
	  </tr>
	  <tr>
	  <th>Name</th>
	  <th>IP</th>
	  <th>State</th>
	  <th>Being used</th>
	  <th>Options</th>
	  </tr>
	 
	 <g:each in="${machineSet}" status="i" var="machine">    
	      	<tr>
	      	<td>	
	      		  <input type="checkbox" name="machine${machine.id}" class="all"/>   		
	        	  <small>&nbsp;${machine.name}</small> 
	        </td>
	        <td>
	   			<small>${machine.ip.ip}</small>
	   		</td>
	   		<td>
	   		<g:if test="${machine.state.equals(PhysicalMachineStateEnum.ON) }">
	   			<g:img file="green.png" title="On"/>
	   		</g:if>
	   		<g:if test="${machine.state.equals(PhysicalMachineStateEnum.DISABLED) }">
	   			<g:img file="blue.png" title="Disabled"/>
	   		</g:if>
	   		<g:if test="${machine.state.equals(PhysicalMachineStateEnum.OFF) }">
	   			<g:img file="red.png" title="Off"/>
	   		</g:if>
	   		</td>
	   		<td>
	   		<g:if test="${machine.withUser }">
	   			<small>Yes</small>
	   		</g:if>
	   		<g:if test="${!machine.withUser }">
	   			<small>No</small>
	   		</g:if>
	   		</td>
	   		<td>
	   			<g:link action="editMachine" params="${[id: machine.id, labId: lab.id] }" ><i class="icon-pencil" title="Edit Machine"></i></g:link>
	   		</td>
	       </tr>	   	
	 </g:each>
	</table>
	</div>

</form>

 <script src="/unacloud2/js/bootbox.js"></script>
 <script src="/unacloud2/js/ui.js"></script>
 <script src="/unacloud2/js/laboratory.js"></script>
 <script>$(document).on('ready',getLab())</script>
</body>
   