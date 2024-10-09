<#import "/spring.ftl" as spring />
<#setting numberFormat="0.##" />
<#-- Dit heeft alleen effect op java.Util.Date objecten, niet op LocalDate/Times! -->
<#setting datetimeFormat="yyyy-MM-dd uu:mm:ss" />
<html>
<head>
	<title>Active Logs</title>
	<link rel=StyleSheet href="<@spring.url '/css/main.css' />" type="text/css">
<#--
	<script type="text/javascript" src="<@spring.url '/js/jquery-1.11.1.js'" /></script> 
	<script type="text/javascript" src="<@spring.url '/js/jquery.tablesorter-2.0.js'" /></script>
-->
	<script type="text/javascript">
$(document).ready(function() 
{ 
    $("#detabel").tablesorter({widgets: ['zebra']}); 
});
	</script 

</head>
<body>
	<h1>Active Logs</h1>
	<br />
<#--	<p>Aantal	Issues=${issuesDocument.issues.size()}</p> -->
	<table id="detabel" class="sortable" >
		<thead>
			<tr>
				<th class="header number">Client ID&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
				<th class="header string">User</th>
				<th class="header number sorter-ipAddress">IP Adres&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
				<th class="header date"  >Started</th>
				<th class="header date"  >Last Activity</th>
				<th class="header number">History Size&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
			</tr>
		</thead>
		<#list clients as client>
			<tr class=${client?itemParity}>
				<td class="number"><a href='<@spring.url "/client.html?id=${client.clientId}" />'>${client.clientId}</a></td>
				<td class="string">${client.user}</td>
				<td class="number">${client.ipAddress}</td>
				<td class="date"  >${client.timeStarted?datetime}</td>
				<td class="date"  >${client.timeLastActivity?datetime}</td>
				<td class="number">${client.historySize}</td>
			</tr>
		</#list>
	</table>

	<br />
</body>
</html>
