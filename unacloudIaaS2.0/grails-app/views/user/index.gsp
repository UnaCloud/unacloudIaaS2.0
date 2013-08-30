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
  	<a href="${createLink(uri: '/user/create', absolute: true)}"><i class="icon-plus-sign"></i></a>
  	</td>
  </tr>
  	
  <tr>
  <th>Username</th>
  <th>User Role</th>
  <th>Images</th>
  <th>Virtual Machines</th>
  <th>Clusters</th>
  <th>Options</th>
  </tr>
 
 <g:each in="${users}" status="i" var="user">   
  <tr>
    <td>
      ${user.username }
    </td>
    <td>
      ${user.userType }
    </td>
    <td>
      None
    </td>
    <td>
      None
    </td>
    <td>
      None
    </td>
    <td >
    <div class="row-fluid text-center">
    <g:link action="edit" params="${[username:user.username]}"><i class="icon-pencil"></i></g:link>
    <a href="edit"><i class="icon-check"></i></a>
    <g:link action="delete" params="${[username:user.username]}"><i class="icon-remove-sign"></i></g:link>
    </div>
    </td>
  </tr>
</g:each>
</table>
</div>
</body>
