<%@ page import="org.powertac.broker.ShoutRequest" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName"
         value="${message(code: 'shoutRequest.label', default: 'ShoutRequest')}"/>
  <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div class="nav">
  <span class="menuButton"><a class="home"
                              href="${createLink(uri: '/')}"><g:message
        code="default.home.label"/></a></span>
  <span class="menuButton"><g:link class="create" action="create"><g:message
      code="default.new.label" args="[entityName]"/></g:link></span>
</div>

<div class="body">
  <h1><g:message code="default.list.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="list">
    <table>
      <thead>
      <tr>

        <g:sortableColumn property="id"
                          title="${message(code: 'shoutRequest.id.label', default: 'Id')}"/>

        <g:sortableColumn property="comment"
                          title="${message(code: 'shoutRequest.comment.label', default: 'Comment')}"/>

        <g:sortableColumn property="active"
                          title="${message(code: 'shoutRequest.active.label', default: 'Active')}"/>

        <g:sortableColumn property="beginTimeslot"
                          title="${message(code: 'shoutRequest.beginTimeslot.label', default: 'Begin Timeslot')}"/>

        <g:sortableColumn property="buySellIndicator"
                          title="${message(code: 'shoutRequest.buySellIndicator.label', default: 'Buy Sell Indicator')}"/>

        <g:sortableColumn property="endTimeslot"
                          title="${message(code: 'shoutRequest.endTimeslot.label', default: 'End Timeslot')}"/>

      </tr>
      </thead>
      <tbody>
      <g:each in="${shoutRequestInstanceList}" status="i"
              var="shoutRequestInstance">
        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

          <td><g:link action="show"
                      id="${shoutRequestInstance.id}">${fieldValue(bean: shoutRequestInstance, field: "id")}</g:link></td>

          <td>${fieldValue(bean: shoutRequestInstance, field: "comment")}</td>

          <td><g:formatBoolean boolean="${shoutRequestInstance.active}"/></td>

          <td>${fieldValue(bean: shoutRequestInstance, field: "beginTimeslot")}</td>

          <td>${fieldValue(bean: shoutRequestInstance, field: "buySellIndicator")}</td>

          <td>${fieldValue(bean: shoutRequestInstance, field: "endTimeslot")}</td>

        </tr>
      </g:each>
      </tbody>
    </table>
  </div>

  <div class="paginateButtons">
    <g:paginate total="${shoutRequestInstanceTotal}"/>
  </div>
</div>
</body>
</html>
