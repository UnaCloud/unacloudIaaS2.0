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
  	<a href="${createLink(uri: '/group/create', absolute: true)}"><i class="icon-plus-sign"></i></a>
  	</td>
  </tr>
  	
  <tr>
  <th>Group Name</th>
  <th>Users</th>
  <th>Options</th>
  </tr>
 
 <g:each in="${groups}" status="i" var="group">   
  <tr>
    <td>
      ${group.name}
    </td>
    <td>
    <ul>
      <g:each in="${group.users}" status="j" var="user">
      	<li>${user.username }</li>
      </g:each>
    </ul>
    </td>
    
    <td >
    <div class="row-fluid text-center">
    <g:link action="edit" params="${[name: group.name]}"><i class="icon-pencil"></i></g:link>
    <g:link action="delete" params="${[name:group.name]}"><i class="icon-remove-sign"></i></g:link>
    </div>
    </td>
  </tr>
</g:each>
</table>
</div>
</body>
