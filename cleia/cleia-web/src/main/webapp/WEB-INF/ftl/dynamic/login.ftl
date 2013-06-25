<html>
<head>
    <#include "/dynamic/head_initial.ftl" parse=true />    
    <#if css??>
        <#list css as c>            
    <link rel="stylesheet" type="text/css" href="<@spring.url relativeUrl="${c}"/>" />
        </#list>
    </#if>    
    <#if js??>
        <#list js as j>
    <script src="<@spring.url relativeUrl="${j}"/>" type="text/javascript"></script>
        </#list>
    </#if>
    <script type="text/javascript">
        App.urlServerRoles='<@spring.message code="urlServerRoles" />';
    </script>
</head>
<body>
</body>
</html>