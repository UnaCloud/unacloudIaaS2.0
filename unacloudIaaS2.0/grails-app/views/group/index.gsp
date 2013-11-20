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
  	 <g:link action="create"><i class="icon-plus-sign"></i></g:link>
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
     <small>${group.name}</small> 
    </td>
    <td>
    <ul>
      <g:each in="${group.users}" status="j" var="user">
      	<li><small>${user.username }</small></li>
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
