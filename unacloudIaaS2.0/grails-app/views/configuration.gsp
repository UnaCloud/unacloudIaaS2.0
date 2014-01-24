<%@page import="back.pmallocators.AllocatorEnum"%>
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
			controller="unaCloudServices" action="changeServerVariables">
			<g:each in="${ServerVariable.all}" status="i" var="serverVariable">
				<g:if test="${!(serverVariable.name=='AGENT_VERSION')}">
					<div class="control-group">
						<label style="width: 250px; padding-right: 20px;"
							class="control-label"> ${serverVariable.name}
						</label>
						<div class="controls" id="data">
							<g:if test="${!(serverVariable.name=='VM_ALLOCATOR_NAME')}">
								<input name="${serverVariable.name}" id="${serverVariable.name}"
									type="text" value="${serverVariable.variable}">
							</g:if>
							<g:if test="${serverVariable.name=='VM_ALLOCATOR_NAME'}">
								<select name="${ serverVariable.name}">
									<g:each in="${AllocatorEnum.values()}" status="j" var="value">
										<option value="${value}" ${(value.toString() == serverVariable.variable)?'selected':''} >
											${value }
										</option>
									</g:each>
								</select>
							</g:if>
						</div>
					</div>

				</g:if>
			</g:each>
			<div class="controls">
				<g:submitButton name="changeServerVariables" class="btn"
					value="Change Variables" />

			</div>

		</g:form>
		<g:link action="updateAgentVersion" controller="unaCloudServices">
			<button class="btn">Update Agent Version</button>
		</g:link>
	</div>
</body>
</html>