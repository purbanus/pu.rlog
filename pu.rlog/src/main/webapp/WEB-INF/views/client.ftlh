<#import "/spring.ftl" as spring />
<#setting numberFormat="0.##" />
<#setting datetimeFormat="MM-dd uu:mm:ss" />
<html>
<html>
	<head>
		<title>Client Log</title>
	<link rel=StyleSheet href="<@spring.url '/css/main.css' />" type="text/css">
	</head>
<body>

	<h1>Client Log</h1>

<#if isError>
	<p class="error">${error}</p>
<#else>
	<table border="0" cellspacing="0" cellpadding="5">
		<tr class="even">
			<th class="label">Client ID</th><td>${client.clientId}</td>
		</tr>
		<tr class="oneven">
			<th class="label">User</th><td>${client.user}</td>
		</tr>
		<tr class="even">
			<th class="label">IP Adres</th><td>${client.ipAddress}/td>
		</tr>
		<tr class="oneven">
			<th class="label">Started</th><td>${client.timeStarted?datetime}</td>
		</tr>
		<tr class="even">
			<th class="label">Last Activity</th><td>${client.timeLastActivity?datetime}</td>
		</tr>
		<tr class="oneven">
			<th class="label">History Size</th><td>${client.historySize}</td>
		</tr>
	</table>
	<#-- Je kunt ook $log weg splitsen in een tabel met kleurtjes, zie logserver-simple --> 
	<pre>${log}</pre>
</#if>
</body>
</html>
