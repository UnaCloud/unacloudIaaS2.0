<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
<body>
<div class="hero-unit span9">
<h3 class="text-center">Laboratory Selection </h3>       
	<div class="row-fluid text-center">
        <g:each in="${laboratories}" status="i" var="lab">
        	<div class="span4">
        		<g:link controller="laboratory" action="getLab" params="${[id: lab.id]}"><g:img file="infrastructure.png"/></g:link>
        		<p>${lab.name}</p>
        		
        	</div>
        </g:each>
    </div>
</div>
</body>
