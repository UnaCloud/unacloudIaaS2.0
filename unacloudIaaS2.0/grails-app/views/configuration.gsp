<%@page import="unacloud2.ServerVariable"%>
<html>
<html>
<head>
<meta name="layout" content="main" />
<r:require modules="bootstrap" />
</head>
<body>
	<div class="hero-unit span9">
		<g:form name="instancesDeploy" class="form-horizontal"
			controller="deployment" action="addInstances">
			<g:each in="${ServerVariable.all}" status="i" var="serverVariable">
				<div class="control-group">
					<label class="control-label">
						${serverVariable.name}
					</label>
					<div class="controls" id="data">
						<input name="value" id="value" type="text"
							value="${serverVariable.variable}">
					</div>
				</div>
				
				
			</g:each>
			<div class="controls">
					<g:submitButton name="addInstances" class="btn" value="Deploy" />
					<g:link action="updateAgentVersion" controller="unaCloudServices">
					<button>Update Agent Version</button>
				</g:link>
				</div>
			
		</g:form>
	</div>
</body>
</html>