<html>
<head>
<meta name="layout" content="main" />
<r:require modules="bootstrap" />
</head>
<body>
	<div class="hero-unit span9">
		<g:form name="clusterDeploy" class="form-horizontal"
			controller="deployment" >
			<input type=hidden name="id" value="${cluster.id}">
			<div id="remaining" class="alert alert-info">
				<table>
					<tr>
						<td rowspan="3"><i class="icon-exclamation-sign"></i>&nbsp;&nbsp;&nbsp;
						</td>
						<td><label class="info">Remaining Common Instances: ${limit}</label>
						</td>
					</tr>
					<tr>
						<td><label> Remaining High Availability Instances: ${limitHA }</label>
						</td>
					</tr>
					<g:if test="${limit==0 && account!= null}">
						<tr>
						<td><label> There are no remaining local physical machines, but you can deploy on ${account.provider.name } </label>
						</td>
					</tr> 
					</g:if>
				</table>
			</div>
			<br>
			<g:each in="${cluster.images}" status="i" var="image">
				<div class="control-group">
					<h5>
						${image.name}
					</h5>
				</div>
				<table border="0" cellpadding="10"">
					<tr>
						<td><label>Instances to deploy</label></td>
						<td><label>Hardware Profile</label></td>
						
					</tr>
					<tr>
						<td><input name="instances" class="input-small" type="text">
						</td>
						<td><select name="hardwareProfile" class="input-small">
								<g:each in="${hardwareProfiles}" status="j" var="hp">
								<option value="${hp.id}">
									${hp.name}
								</option>
								</g:each>
							</select>
						</td>
						
					</tr>
					<tr>
						<td><label>High Availability</label></td>
						<td><input type="checkbox" name="highAvailability${image.id.toString()}"></td>
					</tr>
					
					<tr>
						<td><label>Hostname</label></td>
						<td colspan="2"><input type="text" name="hostname" value="${image.name}"></td>
					</tr>
				</table>
				<br>
			</g:each>
			<div class="control-group">
				<label class="control-label">Execution Time</label>
				<div class="controls">
					<select name="time" class="input-small">
						<option value="${1}">1 hour</option>
						<option value="${2}">2 hours</option>
						<option value="${4}">4 hours</option>
						<option value="${12}">12 hours</option>
						<option value="${24}">1 day</option>
						<option value="${90*24}">90 days</option>
					</select>
				</div>
			</div>
			<div class="controls">
			<g:if test="${limit!=0 || limitHA!=0}">
				<g:actionSubmit name="deploy" class="btn" value="Deploy" action="deploy"/>
			</g:if>
			<g:if test="${account!=null}">
				<g:actionSubmit name="extdeploy" class="btn" value="External Deploy" action="externalDeploy"/>
			</g:if>
			</div>
		</g:form>
		<g:if test="${flash.message && flash.message!=""}">
			<div class="alert alert-error">
				<i class="icon-exclamation-sign"></i>&nbsp;&nbsp;&nbsp;${flash.message }
			</div>
		</g:if>
	</div>
</body>
</html>