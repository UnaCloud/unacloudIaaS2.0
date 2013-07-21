<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
<body>
<div class="span9">
	<div class="hero-unit">
        <h3 class="text-center">Functionalities </h3>
        <br>
        <div class="row-fluid text-center">
        	<div class="span4">
        		<g:link controller="image" action="index"><g:img file="image.png"/></g:link>
        		<p>My Clusters</p>
        	</div>
        	<div class="span4">
        		<g:link controller="template" action="index"><g:img file="vm.png"/></g:link>
        		<p>My Images</p>
        	</div>
        	<div class="span4">
        		<g:link controller="cluster" action="index"><g:img file="cluster.png"/></g:link>
        		<p>My Virtual Machines</p>
        	</div>
        </div>
        <div class="row-fluid text-center">
        	<div class="span4">
        		<g:link controller="cluster" action="index"><g:img file="deployment.png"/></g:link>
        		<p>My Deployments</p>
        	</div>
        </div>
        	
        </div>	
        	
          
    </div>
</div>
    
</body>
</html>