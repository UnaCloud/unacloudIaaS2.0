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
  	<a href="${createLink(uri:"/virtualMachineImage/newImage", absolute: true)}"><i class="icon-plus-sign" title="New Image"></i></a>
  	</td>
  </tr>
  	
  <tr>
  <th>Image Name</th>
  <th>Options</th>
  </tr>
 
 <g:each in="${images}" status="i" var="image">   
  <tr>
    <td>
      <small>${image.name}</small>
    </td>
    <td >
    <div class="row-fluid text-center">
    <g:link action="edit" params="${[id: image.id]}"><i class="icon-pencil" title="Edit Image"></i></g:link>
    <g:link action="delete" params="${[id: image.id]}"><i class="icon-remove-sign" title="Delete Image"></i></g:link>
    <g:link action="changeVersion" params="${[id: image.id]}"><i class="icon-repeat" title="Change Version"></i></g:link>
    
    <g:link controller="deployment" action="deployImage" params="${[id: image.id]}"><i class="icon-play" title="Deploy Image (For edition only)"></i></g:link>
    </div>
    </td>
  </tr>
</g:each>
</table>
</div>
</body>
