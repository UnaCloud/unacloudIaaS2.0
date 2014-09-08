<!DOCTYPE html>
<html lang="en">
   <g:if test="${session.user==null}">
   	 <g:javascript>
		window.location.href = '<g:createLink uri='/' absolute='true' />';
	</g:javascript>
   </g:if>
   <head>
   		<meta charset="utf-8">
   	
    <title>UnaCloud 2.0</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
	<link rel="shortcut icon" href="/~unacloud/dokuwiki/lib/tpl/PlantillaLab/images/favicon.ico" />
	<asset:stylesheet href="application.css"/>
    <!-- Le styles -->
    
    <style type="text/css">
      body {
        
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }

      @media (max-width: 980px) {
        /* Enable use of floated navbar text */
        .navbar-text.pull-right {
          float: none;
          padding-left: 5px;
          padding-right: 5px;
        }
      }
    </style>
      <g:layoutTitle/>
      
      
   	
   </head>
   <body id="body">
      <asset:javascript src="application.js"/>
      <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="brand" href="${createLink(controller: 'user', action: 'home')}">UnaCloud 2.0</a>
          <div class="nav-collapse collapse">
            <ul class="nav pull-right">
              <li><a href="${createLink(uri: '/account/', absolute: true)}">My Account</a></li>
              <li><g:link controller="user" action="logout">Log Out</g:link></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
    <div class="container-fluid">
	<div class="row-fluid">
	
      <div class="span3">
          <div class="well sidebar-nav">
            <ul class="nav nav-list">
              <li><a class="nav-header" href="${createLink(uri: '/functionalities', absolute: true)}">Functionalities</a></li>
              <li><a href="${createLink(uri: '/virtualMachineImage/', absolute: true)}">My Images</a></li>
              <li><a href="${createLink(uri: '/cluster/', absolute: true)}">My Clusters</a></li>
              <li><a href="${createLink(uri: '/deployment/', absolute: true)}">My Deployments</a></li>
              <g:if test="${session.user.userType.equals("Administrator")}">
              <li><a class="nav-header" href="${createLink(uri: '/administration', absolute: true)}">Administration</a></li>
              <li><a href="${createLink(uri: '/user/', absolute: true)}">User Management</a></li>
              <li><a href="${createLink(uri: '/group/', absolute: true)}">Group Management</a></li>
              <li><a href="${createLink(uri: '/hypervisor/', absolute: true)}">Hypervisor Management</a></li>
              <li><a href="${createLink(uri: '/operatingSystem/', absolute: true)}">OS Management</a></li>
              <li><a href="${createLink(uri: '/laboratory/', absolute: true)}">Infrastructure Management</a></li>
              <li><a class="nav-header" href="${createLink(uri: '/configuration', absolute: true)}">Configuration</a></li>
              <li><a href="${createLink(uri: '/configuration/serverVariables', absolute: true)}">Server Variables</a></li>
          	  <li><a href="${createLink(uri: '/configuration/agentVersion', absolute: true)}">Update Agent Version</a></li>
              <li><a href="${createLink(uri: '/UnaCloudServices/updater', absolute: true)}">Download Agent Files</a></li>
              </g:if>
            </ul>
          </div><!--/.well -->
        </div><!--/span-->
        <g:layoutBody/>
        </div>
        </div>
   </body>
</html>