<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="bootstrap"/>
   </head>
<body>
<div class="hero-unit span9">
<g:form controller="deployment">
<script>
var myVar=setInterval(function(){reload()},8000);

function reload()
{
	
}
</script>
<table class="table table-bordered table-condensed text-center" style="background:white">
  <g:if test="${session.user.userType == 'Administrator'}">
  <tr class="info">
  	<td class="info" colspan="7">
  	<input type="checkbox" name="View All" ><small>&nbsp;View All</small>
  	</td>
  </tr>
  </g:if>
  <tr class="info">
  	<td class="info" colspan="7" >
  	<input type="checkbox" id="selectAll"><small>&nbsp;Select All</small>
  	          <g:actionSubmitImage value="stop" src="${resource(dir: 'images', file: 'empty.gif')}" action="stop" class="icon-off pull-right"/>
              
              <g:actionSubmitImage value="refresh " src="${resource(dir: 'images', file: 'empty.gif')}"action="index" class="icon-refresh pull-right"/>
              
    </td>
  </tr>	
  <tr>
  <th>Cluster</th>
  <th>Images</th>
  <th>Access Type</th>
  <th>HostName</th>
  <th>Status</th>
  <th>Time Left</th>
  <th>IP</th>
  </tr>
 <div class="all">
 <g:each in="${deployments}" status="i" var="deployment">   
  <tr>
    <td rowspan="${deployment.getTotalActiveVMs()}">
      <input type="checkbox" name="cluster" id="selectCluster${i}" class="all">
      <small>
      <g:if test="${deployment.cluster.cluster!=null }">
      ${deployment.cluster.cluster.name}
      </g:if>
      <g:if test="${deployment.cluster.cluster==null }">
      None
      </g:if>
      </small>
    </td>
    
    <g:each in="${deployment.cluster.images}" status="j" var="image">
   		
    <td rowspan="${image.numberOfActiveMachines() }">
   	<small>${image.image.name }</small>
   	<br>
   	<g:link action="addInstancesOptions" controller="deployment" params="${ [id:image.id] }"><i class="icon-plus-sign"></i></g:link></td>
    <td rowspan="${image.numberOfActiveMachines() }">
    <small>${image.image.accessProtocol }</small>
    </td> 
    <g:each in="${image.getOrderedVMs() }" status="k" var="virtualMachine">  
   	<g:if test="${ virtualMachine.status!= virtualMachine.FINISHED}">
   	<td >
   	<input type="checkbox" name="hostname${virtualMachine.id}" class="hostname${i} all">
   	<small>${virtualMachine.name }</small>
    </td>
    <td>
    <g:if test="${virtualMachine.status== virtualMachine.CONFIGURING || virtualMachine.status== virtualMachine.COPYING}">
     <g:img file="blue.png"/>
     </g:if>
     <g:if test="${virtualMachine.status==virtualMachine.DEPLOYING}">
     <g:img file="amber.png"/>
     </g:if>
     <g:if test="${virtualMachine.status==virtualMachine.DEPLOYED}">
     <g:img file="green.png"/>
     </g:if>
    <g:if test="${virtualMachine.status==virtualMachine.FAILED}">
     <g:img file="red.png"/>
     </g:if>
    </td>
  	<td>
  	<small>${ virtualMachine.remainingTime()}</small>
    </td>
    <td>
    <small>${virtualMachine.ip.ip }</small>	
    </td>
    
    </tr>
    <tr>
    </g:if>
    </g:each>
    
    </g:each>
  </tr>
  <tr>
</g:each>
</div>
</table>

</g:form>
</div>
<script type="text/javascript">
$(function () {
    $('#selectAll').click(function (event) {

        var selected = this.checked;
        // Iterate each checkbox
        $('.all:checkbox').each(function () {    this.checked = selected; });

    });
 });
</script>
<g:each in="${deployments}" status="i" var="deployment">
<script type="text/javascript">
 $('#selectCluster${i}').click(function (event) {

        var selected = this.checked;
        // Iterate each checkbox
        $('.hostname${i}:checkbox').each(function () {    this.checked = selected; });

    });
</script>
</g:each>

</body>

