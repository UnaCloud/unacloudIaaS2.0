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
  	<a href="${createLink(uri:"/cluster/newCluster", absolute: true)}"><i class="icon-plus-sign" title="New Cluster"></i></a>
  	<a href="${createLink(action:"history", controller:"deployment")}"><i class="icon-calendar" title="Deployment History"></i></a>
  	</td>
  </tr>
  	
  <tr>
  <th>Cluster Name</th>
  <th>Images</th>
  <th>Options</th>
  </tr>
 
 <g:each in="${clusters}" status="i" var="cluster">   
  <tr>
    <td>
      <small>${cluster.name}</small>
    </td>
    <td>
    <ul>
   		<g:each in="${cluster.getOrderedImages()}" status="j" var="image">
   			<li><small>${image.name}</small>
   		</g:each>
    </ul>
    </td>
    
    <td >
    <div class="row-fluid text-center">
     <g:link action="delete" params="${[id: cluster.id]}"><i title="Delete Cluster" class="icon-remove-sign"></i></g:link>
     <g:link action="deployOptions" params="${[id: cluster.id]}"><i title="Deploy Cluster" class="icon-play"></i></g:link>
     <g:link action="externalDeployOptions" params="${[id: cluster.id]}"><i title="Deploy Cluster" class="icon-globe"></i></g:link>
    
    </div>
    </td>
    
  </tr>
</g:each>
</table>
</div>
</body>
