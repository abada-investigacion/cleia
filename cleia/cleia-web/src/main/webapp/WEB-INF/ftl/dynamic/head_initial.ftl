<#import "/spring.ftl" as spring />
<title><@spring.message code="title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!--CSS Ext4 -->
<#if (!isDesktop?? || isDesktop) >
<link rel="stylesheet" type="text/css" href="<@spring.url relativeUrl="/ext4/resources/ext-theme-neptune/ext-theme-neptune-all.css"/>" />
<link rel="stylesheet" type="text/css" href="<@spring.url relativeUrl="/abada/css/abada.css"/>" />
<link rel="stylesheet" type="text/css" href="<@spring.url relativeUrl="/css/backend.css"/>" />
<!--Ext4 base -->
<script src="<@spring.url relativeUrl="/ext4/bootstrap.js"/>" type="text/javascript"></script>
<!--script src="<@spring.url relativeUrl="/ext4/builds/ext-core-debug.js" />" type="text/javascript"></script-->
<script src="<@spring.url relativeUrl="/abada/ext-abada-utils.js"/>" type="text/javascript"></script>
<#else>
<!--CSS Touch -->
<link rel="stylesheet" type="text/css" href="<@spring.url relativeUrl="/touch/resources/css/sencha-touch.css"/>" />
<!--Tocuch base -->
<script src="<@spring.url relativeUrl="/touch/sencha-touch-all.js"/>" type="text/javascript"></script>
<script src="<@spring.url relativeUrl="/abada-touch/ext-abada-utils.js"/>" type="text/javascript"></script>
</#if>

<script type="text/javascript">
    Ext.Loader.setConfig({
        enabled: true,
        disableCaching:true,//FIXME set false in production environment
        paths:{
<#if (!isDesktop?? || isDesktop) >
            'Ext': '<@spring.url relativeUrl="/ext4/src" />',
            'App': '<@spring.url relativeUrl="/" />',
            'Abada': '<@spring.url relativeUrl="/abada" />'
<#else>
            'Ext': '<@spring.url relativeUrl="/touch/src" />',
            'App': '<@spring.url relativeUrl="/" />',
            'Abada': '<@spring.url relativeUrl="/abada-touch" />'
</#if>
        }
    });

    Ext.require([
        'Ext.Ajax','Abada.Ajax'
    ]);

    Ext.onReady(function() {
        Ext.Ajax.withCredentials = true;
        Abada.Ajax.withCredentials = true;

        Ext.Ajax.useDefaultXhrHeader = false;
        Abada.Ajax.useDefaultXhrHeader = false;
    });

    App={};    
    App.height=500;    
    App.baseRef='<@spring.baseRef />';
    App.urlServer='<@spring.message code="secureUrlServer" />';

</script>
<!--locales-->
<!--script src="<@spring.url relativeUrl="/ext4/locale/ext-lang-es.js"/>" type="text/javascript"></script-->