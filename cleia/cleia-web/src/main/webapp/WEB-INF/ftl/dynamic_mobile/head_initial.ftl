<#import "/spring.ftl" as spring />
<title><@spring.message code="title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!--CSS Touch -->
<link rel="stylesheet" type="text/css" href="<@spring.url relativeUrl="/touch/resources/css/sencha-touch.css"/>" />
<!--Tocuch base -->
<script src="<@spring.url relativeUrl="/touch/sencha-touch.js"/>" type="text/javascript"></script>

<script type="text/javascript">
    Ext.Loader.setConfig({
        enabled: true,
        disableCaching:true,//FIXME set false in production environment
        paths:{
            'Ext': '<@spring.url relativeUrl="/touch/src" />',
            'App': '<@spring.url relativeUrl="/" />'
            //'Abada': '<@spring.url relativeUrl="/abada" />'
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
    App.baseRef='<@spring.baseRef />';
    App.urlServer='<@spring.message code="secureUrlServer" />';

</script>
<!--locales-->