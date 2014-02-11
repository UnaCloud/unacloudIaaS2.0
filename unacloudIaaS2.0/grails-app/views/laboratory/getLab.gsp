<%@page import="unacloud2.PhysicalMachineStateEnum"%>
<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
<body>
<g:form>
<div class="hero-unit span9">
<table class="table table-bordered text-center" style="background:white" >
  <tr class="info">
  	<td class="info" colspan="12">
  	<input type="checkbox" id="selectAll"><small>&nbsp;Select All</small>
  	<g:link controller="laboratory" action="createMachine" params="${[id: lab.id]}" title="Create PM"><i class="icon-plus-sign pull-right"></i></g:link>
  	<g:actionSubmitImage value="refresh" src="${resource(dir: 'images', file: 'empty.gif')}" action="updateMachines" title="Update agents" class="icon-refresh pull-right"/>
  	<g:actionSubmitImage value="stop" src="${resource(dir: 'images', file: 'empty.gif')}" action="stopMachines" title="Stop agents" class="icon-stop pull-right"/>
    <g:actionSubmitImage value="clearCache" src="${resource(dir: 'images', file: 'empty.gif')}" action="clearCache" title="Clear VM cache" class="icon-leaf pull-right"/>
  	
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
   			<g:link action="editMachine" params="${[id: machine.id, labId: lab.id] }" ><i class="icon-pencil" ></i></g:link>
   		</td>
       </tr>	   	
 </g:each>
</table>
</div>

</g:form>


<script type="text/javascript">
$(function () {
    $('#selectAll').click(function (event) {

        var selected = this.checked;
        // Iterate each checkbox
        $('.all:checkbox').each(function () {    this.checked = selected; });

    });
 });
</script>
</body>
