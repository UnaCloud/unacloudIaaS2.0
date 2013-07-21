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
  	<a href="${createLink(uri: '/operatingSystem/create', absolute: true)}"><i class="icon-plus-sign"></i></a>
  	</td>
  </tr>
  	
  <tr>
  <th>Operating System ID</th>
  <th>Operating System Name</th>
  <th>Options</th>
  </tr>
 
 <g:each in="${oss}" status="i" var="os">   
  <tr>
    <td>
      ${os.id }
    </td>
    <td>
      ${os.name }
    </td>
    <td >
    <div class="row-fluid text-center">
    <g:link action="edit" params="${[id:os.id]}"><i class="icon-pencil"></i></g:link>
    <g:link action="delete" params="${[id:os.id]}"><i class="icon-remove-sign"></i></g:link>
    </div>
    </td>
  </tr>
</g:each>
</table>
</div>
</body>
