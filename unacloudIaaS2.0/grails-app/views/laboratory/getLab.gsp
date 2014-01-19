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
  
  
 <div class="row-fluid text-center">
 
 <g:each in="${machineSet}" status="i" var="machine">    
 <g:if test="${i%6==0 }">
 <tr>
 </g:if>
 <td>
    <table class="table table-bordered table-condensed text-center" style="background:white">
      	<tr class="info">
      		<td>
      		  <input type="checkbox" name="machine${machine.id}" class="all"/>   		
              <g:link action="editMachine" params="${[id: machine.id, labId: lab.id] }" class="nav pull-right"><i class="icon-pencil" ></i></g:link>
      		</td>
   		</tr>
   		<tr>
   		<td>
   			<g:img file="infrastructure.png"/><p>${machine.name}</p>
   		</td>
   		</tr>
   	</table>
 </td>
<g:if test="${i%6==5 }">
 </tr>
 </g:if>
</g:each>
</div>
</td>
</tr>
</g:form>
</table>

</div>
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
