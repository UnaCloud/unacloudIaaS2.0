<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
<body>

<div class="hero-unit span9">

<table class="table table-bordered table-condensed text-center" style="background:white">
  <tr class="info">
  	<td class="info" colspan="6">
  	<a href="${createLink(uri:"/cluster/newCluster", absolute: true)}"><i class="icon-plus-sign"></i></a>
  	</td>
  </tr>
  	
  <tr>
  <th>Cluster Name</th>
  <th>Images</th>
  <th>Options</th>
  <th>Deployment</th>
  </tr>
 
 <g:each in="${clusters}" status="i" var="cluster">   
  <tr>
    <td>
      ${cluster.name}
    </td>
    <td>
    <ul>
   		<g:each in="${cluster.getOrderedImages()}" status="j" var="image">
   			<li>${image.name}
   		</g:each>
    </ul>
    </td>
    
    <td >
    <div class="row-fluid text-center">
    <g:link action="edit" params="${[id: cluster.id]}"><i class="icon-pencil"></i></g:link>
    <g:link action="delete" params="${[id: cluster.id]}"><i class="icon-remove-sign"></i></g:link>
    </div>
    </td>
    <td>
    <div class="row-fluid text-center">
    <g:link action="deployOptions" params="${[id: cluster.id]}"> 
   		<input type="button" value="Deploy" class="button"/> 
   	</g:link>
   	
    <g:link action="history"> 
   		<input type="button" value="History" class="button"/> 
   	</g:link>	
   	</div>
    </td>
  </tr>
</g:each>
</table>
</div>
</body>
