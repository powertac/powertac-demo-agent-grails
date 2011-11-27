<%@ page import="org.powertac.broker.ShoutRequest" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <g:set var="entityName"
         value="${message(code: 'shoutRequest.label', default: 'ShoutRequest')}"/>
  <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<div class="nav">
  <span class="menuButton"><a class="home"
                              href="${createLink(uri: '/')}"><g:message
        code="default.home.label"/></a></span>
  <span class="menuButton"><g:link class="list" action="list"><g:message
      code="default.list.label" args="[entityName]"/></g:link></span>
</div>

<div class="body">
  <h1><g:message code="default.create.label" args="[entityName]"/></h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:hasErrors bean="${shoutRequestInstance}">
    <div class="errors">
      <g:renderErrors bean="${shoutRequestInstance}" as="list"/>
    </div>
  </g:hasErrors>
  <g:form action="save">
    <div class="dialog">
      <table>
        <tbody>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="active"><g:message code="shoutRequest.active.label"
                                           default="Active"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: shoutRequestInstance, field: 'active', 'errors')}">
            <g:checkBox name="active" value="${shoutRequestInstance?.active}"/>
          </td>
        </tr>

        %{--<tr class="prop">--}%
          %{--<td valign="top" class="name">--}%
            %{--<label for="product"><g:message code="shoutRequest.product.label"--}%
                                            %{--default="Product"/></label>--}%
          %{--</td>--}%
          %{--<td valign="top"--}%
              %{--class="value ${hasErrors(bean: shoutRequestInstance, field: 'product', 'errors')}">--}%
            %{--<g:select name="product"--}%
                      %{--from="${org.powertac.common.enumerations.ProductType?.values()}"--}%
                      %{--keys="${org.powertac.common.enumerations.ProductType?.values()*.name()}"--}%
                      %{--value="${shoutRequestInstance?.product?.name()}"/>--}%
          %{--</td>--}%
        %{--</tr>--}%

        <tr class="prop">
          <td valign="top" class="name">
            <label for="buySellIndicator"><g:message
                code="shoutRequest.buySellIndicator.label"
                default="Buy Sell Indicator"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: shoutRequestInstance, field: 'buySellIndicator', 'errors')}">
            <g:select name="buySellIndicator"
                      from="${ShoutRequest.OrderType.values()}"
                      keys="${ShoutRequest.OrderType.values()*.name()}"
                      value="${shoutRequestInstance?.buySellIndicator?.name()}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="quantity"><g:message code="shoutRequest.quantity.label"
                                             default="Quantity"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: shoutRequestInstance, field: 'quantity', 'errors')}">
            <g:textField name="quantity"
                         value="${fieldValue(bean: shoutRequestInstance, field: 'quantity')}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="limitPrice"><g:message
                code="shoutRequest.limitPrice.label"
                default="Limit Price"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: shoutRequestInstance, field: 'limitPrice', 'errors')}">
            <g:textField name="limitPrice"
                         value="${fieldValue(bean: shoutRequestInstance, field: 'limitPrice')}"/>
          </td>
        </tr>


        %{--<tr class="prop">--}%
          %{--<td valign="top" class="name">--}%
            %{--<label for="executionQuantity"><g:message--}%
                %{--code="shoutRequest.executionQuantity.label"--}%
                %{--default="Execution Quantity"/></label>--}%
          %{--</td>--}%
          %{--<td valign="top"--}%
              %{--class="value ${hasErrors(bean: shoutRequestInstance, field: 'executionQuantity', 'errors')}">--}%
            %{--<g:textField name="executionQuantity"--}%
                         %{--value="${fieldValue(bean: shoutRequestInstance, field: 'executionQuantity')}"/>--}%
          %{--</td>--}%
        %{--</tr>--}%

        %{--<tr class="prop">--}%
          %{--<td valign="top" class="name">--}%
            %{--<label for="executionPrice"><g:message--}%
                %{--code="shoutRequest.executionPrice.label"--}%
                %{--default="Execution Price"/></label>--}%
          %{--</td>--}%
          %{--<td valign="top"--}%
              %{--class="value ${hasErrors(bean: shoutRequestInstance, field: 'executionPrice', 'errors')}">--}%
            %{--<g:textField name="executionPrice"--}%
                         %{--value="${fieldValue(bean: shoutRequestInstance, field: 'executionPrice')}"/>--}%
          %{--</td>--}%
        %{--</tr>--}%

        <tr class="prop">
          <td valign="top" class="name">
            <label for="beginTimeslot"><g:message
                code="shoutRequest.beginTimeslot.label"
                default="Begin Timeslot"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: shoutRequestInstance, field: 'beginTimeslot', 'errors')}">
            <g:textField name="beginTimeslot"
                         value="${fieldValue(bean: shoutRequestInstance, field: 'beginTimeslot')}"/>
          </td>
        </tr>

        <tr class="prop">
          <td valign="top" class="name">
            <label for="endTimeslot"><g:message
                code="shoutRequest.endTimeslot.label"
                default="End Timeslot"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: shoutRequestInstance, field: 'endTimeslot', 'errors')}">
            <g:textField name="endTimeslot"
                         value="${fieldValue(bean: shoutRequestInstance, field: 'endTimeslot')}"/>
          </td>
        </tr>


        <tr class="prop">
          <td valign="top" class="name">
            <label for="comment"><g:message code="shoutRequest.comment.label"
                                            default="Comment"/></label>
          </td>
          <td valign="top"
              class="value ${hasErrors(bean: shoutRequestInstance, field: 'comment', 'errors')}">
            <g:textField name="comment"
                         value="${shoutRequestInstance?.comment}"/>
          </td>
        </tr>

        </tbody>
      </table>
    </div>

    <div class="buttons">
      <span class="button"><g:submitButton name="create" class="save"
                                           value="${message(code: 'default.button.create.label', default: 'Create')}"/></span>
    </div>
  </g:form>
</div>
</body>
</html>
