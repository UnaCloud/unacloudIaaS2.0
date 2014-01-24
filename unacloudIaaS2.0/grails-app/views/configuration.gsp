<%@page import="unacloud2.ServerVariable"%>
<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
<body>
<div class="span9">
	<div class="hero-unit">
        	<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"></label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">Allocation Name</label>
				<div class="controls" id="data">
					<input name="value" id="value" type="text" value="${ServerVariable.findByName("VM_ALLOCATOR_NAME").variable}">
				</div>
			</div>
        	<g:link  action="updateAgentVersion" controller="unaCloudServices"><button>Update Agent Version</button></g:link>
	</div>		
</div>
    
</body>
</html>