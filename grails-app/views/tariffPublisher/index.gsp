<%@ page import="org.codehaus.groovy.grails.commons.ConfigurationHolder; org.powertac.common.TariffSpecification" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tariffSpecification.label', default: 'TariffSpecification')}" />
        <title><g:message code="default.publish.label" args="[TariffSpecification]" default="Publish TariffSpecification" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.publish.label" args="[TariffSpecification]" default="Publish TariffSpecification" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${tariffSpecificationInstance}">
            <div class="errors">
                <g:renderErrors bean="${tariffSpecificationInstance}" as="list" />
            </div>
            </g:hasErrors>
          <g:form action="publish" method="post">
                <div class="dialog">
                    <table>
                        <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="expiration"><g:message code="tariffSpecification.expiration.label" default="Expiration" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tariffSpecificationInstance, field: 'expiration', 'errors')}">

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="minDuration"><g:message code="tariffSpecification.minDuration.label" default="Min Duration" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tariffSpecificationInstance, field: 'minDuration', 'errors')}">
                                    <g:textField name="minDuration" value="${fieldValue(bean: tariffSpecificationInstance, field: 'minDuration')}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="powerType"><g:message code="tariffSpecification.powerType.label" default="Power Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tariffSpecificationInstance, field: 'powerType', 'errors')}">
                                    <g:select name="powerType" from="${org.powertac.common.enumerations.PowerType?.values()}" keys="${org.powertac.common.enumerations.PowerType?.values()*.name()}" value="${tariffSpecificationInstance?.powerType?.name()}"  />
                                </td>
                            </tr>

                            %{--<tr class="prop">--}%
                                %{--<td valign="top" class="name">--}%
                                  %{--<label for="rates"><g:message code="tariffSpecification.rates.label" default="Rates" /></label>--}%
                                %{--</td>--}%
                                %{--<td valign="top" class="value ${hasErrors(bean: tariffSpecificationInstance, field: 'rates', 'errors')}">--}%
                                    %{--<g:select name="rates" from="${org.powertac.common.Rate.list()}" multiple="yes" optionKey="id" size="5" value="${tariffSpecificationInstance?.rates*.id}" />--}%
                                %{--</td>--}%
                            %{--</tr>--}%

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="earlyWithdrawPayment"><g:message code="tariffSpecification.earlyWithdrawPayment.label" default="Early Withdraw Payment" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tariffSpecificationInstance, field: 'earlyWithdrawPayment', 'errors')}">
                                    <g:textField name="earlyWithdrawPayment" value="${fieldValue(bean: tariffSpecificationInstance, field: 'earlyWithdrawPayment')}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="periodicPayment"><g:message code="tariffSpecification.periodicPayment.label" default="Periodic Payment" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tariffSpecificationInstance, field: 'periodicPayment', 'errors')}">
                                    <g:textField name="periodicPayment" value="${fieldValue(bean: tariffSpecificationInstance, field: 'periodicPayment')}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="signupPayment"><g:message code="tariffSpecification.signupPayment.label" default="Signup Payment" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tariffSpecificationInstance, field: 'signupPayment', 'errors')}">
                                    <g:textField name="signupPayment" value="${fieldValue(bean: tariffSpecificationInstance, field: 'signupPayment')}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="supersedes"><g:message code="tariffSpecification.supersedes.label" default="Supersedes" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tariffSpecificationInstance, field: 'supersedes', 'errors')}">

                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                  <span class="button"><g:submitButton name="publish" class="save" value="${message(code: 'default.button.publish.label', default: 'Publish')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>