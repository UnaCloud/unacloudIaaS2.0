<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
<body>
<div class="hero-unit span9">
<table class="table table-bordered text-center" style="background:white" >
  <tr class="info">
  	<td class="info" colspan="12">
  	<g:link controller="laboratory" action="createMachine" params="${[id: lab.id]}"><i class="icon-plus-sign"></i></g:link>
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
      		  <g:checkBox name="${"checkbox"+machine.name }"/>   		
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
</table>
</div>
</body>
