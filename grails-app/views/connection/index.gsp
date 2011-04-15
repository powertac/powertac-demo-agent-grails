<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title>Connect to Power TAC Server</title>
</head>
<body>
<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>
<div class="connectContainer">
  <g:form action="connect">
    <input type="hidden" name="targetUri" value="${targetUri}"/>
    <table>
      <tbody>
      <tr><td colspan="2"><div align="center"><b>Connect to Power TAC Server</b></div></td></tr>
      <tr>
        <td>Server:</td>
        <td><input type="text" name="server" value="${server}" size="75"/></td>
      </tr>
      <tr>
        <td></td>
        <td>Research Configuration: URL of the Power TAC Server<br/>Tournament Configuration: URL of Power TAC Web-App</td>
      </tr>
      <tr>
        <td>Username:</td>
        <td><input type="text" name="username" value="${username}" size="75"/></td>
      </tr>
      <tr>
        <td>Password:</td>
        <td><input type="password" name="password" value="${password}" size="75"/></td>
      </tr>
      <tr>
        <td/>
        <td><input type="submit" value="Connect"/></td>
      </tr>
      </tbody>
    </table>
  </g:form>
</div>
</body>
</html>
