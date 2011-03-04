

<%@ page import="org.powertac.common.TariffSpecification" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'tariffSpecification.label', default: 'TariffSpecification')}" />
        <title><g:message code="default.publish.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
        </div>
        <div class="body">
            <h1><g:message code="default.publish.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:form action="publish" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="brokerId"><g:message code="tariffSpecification.brokerId.label" default="Broker Id" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: tariffSpecificationInstance, field: 'brokerId', 'errors')}">
                                    <g:textField name="brokerId" value="${tariffSpecificationInstance?.brokerId}" />
                                </td>
                            </tr>
                        
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
                        
                        </tbody>
                    </table>
                </div>
                <div class="dialog">
                    <table>
                        <tbody>                       
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="providerUrl"><g:message code="jms.providerUrl.label" default="Provider URL" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: jms, field: 'providerUrl', 'errors')}">
                                    <g:textField name="providerUrl" value="${jms?.providerUrl}" />
                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="destinationName"><g:message code="jms.destinationName" default="Destination Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: jms, field: 'destinationName', 'errors')}">
                                    <g:textField name="destinationName" value="${jms?.destinationName}" />
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
