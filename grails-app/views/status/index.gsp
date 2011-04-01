<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <style type="text/css" media="screen">

  #nav {
    margin-top: 20px;
    margin-left: 30px;
    width: 228px;
    float: left;
  }

  #pageBody {
    margin-top: 25px;
    margin-left: 280px;
    margin-right: 20px;
  }

  </style>
  <title>Power TAC Broker Status</title>
</head>
<body>
<div class="nav">
  <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
</div>
<div class="body">
  <h1>Power TAC Broker Status</h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div id="nav">
    <div class="list">
      <table>
        <thead>
        <tr>
          <th class="simple">Installed Plugins</th>
          <th class="simple">Version</th>
        </tr>
        </thead>
        <tbody>
        <g:set var="pluginManager"
            value="${applicationContext.getBean('pluginManager')}"></g:set>
        <g:each var="plugin" in="${pluginManager.allPlugins}" status="i">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td>${plugin.name}</td>
            <td>${plugin.version}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </div>
  <div id="pageBody">
    <div class="list">
      <table>
        <thead>
        <tr>
          <th class="simple">Application Property</th>
          <th class="simple">Value</th>
        </tr>
        </thead>
        <tbody>
        <g:each var="obj" in="${brokerProperties}" status="i">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td>${obj.key}</td>
            <td>${obj.value}</td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
    <div class="list">
      <table>
        <thead>
        <tr>
          <th class="simple">Available Controllers</th>
        </tr>
        </thead>
        <tbody>
        <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }" status="i">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></td>
          </tr>
        </g:each>
        </tbody>
      </table>
    </div>
  </div>
</div>
</body>
</html>