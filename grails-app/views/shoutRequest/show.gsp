<%@ page import="org.powertac.broker.ShoutRequest" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName"
         value="${message(code: 'shoutRequest.label', default: 'ShoutRequest')}"/>
  <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>
<div class="nav">
  <span class="menuButton"><a class="home"
                              href="${createLink(uri: '/')}"><g:message
        code="default.home.label"/></a></span>
  <span class="menuButton"><g:link class="list" action="list"><g:message
      code="default.list.label" args="[entityName]"/></g:link></span>
  <span class="menuButton"><g:link class="create" action="create"><g:message
      code="default.new.label" args="[entityName]"/></g:link></span>
</div>

<div class="body">
  <h1><g:message code="default.show.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <div class="dialog">
    <table>
      <tbody>

      <tr class="prop">
        <td valign="top" class="name"><g:message code="shoutRequest.id.label"
                                                 default="Id"/></td>

        <td valign="top"
            class="value">${fieldValue(bean: shoutRequestInstance, field: "id")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message
            code="shoutRequest.comment.label" default="Comment"/></td>

        <td valign="top"
            class="value">${fieldValue(bean: shoutRequestInstance, field: "comment")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message
            code="shoutRequest.active.label" default="Active"/></td>

        <td valign="top" class="value"><g:formatBoolean
            boolean="${shoutRequestInstance?.active}"/></td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message
            code="shoutRequest.beginTimeslot.label"
            default="Begin Timeslot"/></td>

        <td valign="top"
            class="value">${fieldValue(bean: shoutRequestInstance, field: "beginTimeslot")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message
            code="shoutRequest.endTimeslot.label" default="End Timeslot"/></td>

        <td valign="top"
            class="value">${fieldValue(bean: shoutRequestInstance, field: "endTimeslot")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message
            code="shoutRequest.buySellIndicator.label"
            default="Buy Sell Indicator"/></td>

        <td valign="top"
            class="value">${shoutRequestInstance?.buySellIndicator?.encodeAsHTML()}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message
            code="shoutRequest.executionPrice.label"
            default="Execution Price"/></td>

        <td valign="top"
            class="value">${fieldValue(bean: shoutRequestInstance, field: "executionPrice")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message
            code="shoutRequest.executionQuantity.label"
            default="Execution Quantity"/></td>

        <td valign="top"
            class="value">${fieldValue(bean: shoutRequestInstance, field: "executionQuantity")}</td>

      </tr>

      <tr class="prop">
        <td valign="top" class="name"><g:message
            code="shoutRequest.limitPrice.label" default="Limit Price"/></td>

        <td valign="top"
            class="value">${fieldValue(bean: shoutRequestInstance, field: "limitPrice")}</td>

      </tr>

      %{--<tr class="prop">--}%
        %{--<td valign="top" class="name"><g:message--}%
            %{--code="shoutRequest.product.label" default="Product"/></td>--}%

        %{--<td valign="top"--}%
            %{--class="value">${shoutRequestInstance?.product?.encodeAsHTML()}</td>--}%

      %{--</tr>--}%

      <tr class="prop">
        <td valign="top" class="name"><g:message
            code="shoutRequest.quantity.label" default="Quantity"/></td>

        <td valign="top"
            class="value">${fieldValue(bean: shoutRequestInstance, field: "quantity")}</td>

      </tr>

      </tbody>
    </table>
  </div>

  <div class="buttons">
    <g:form>
      <g:hiddenField name="id" value="${shoutRequestInstance?.id}"/>
      <span class="button"><g:actionSubmit class="edit" action="edit"
                                           value="${message(code: 'default.button.edit.label', default: 'Edit')}"/></span>
      <span class="button"><g:actionSubmit class="delete" action="delete"
                                           value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                           onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/></span>
    </g:form>
  </div>
</div>
</body>
</html>
