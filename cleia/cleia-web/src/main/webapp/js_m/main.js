/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.require([
    'Ext.container.Viewport',
    'Ext.container.Container',
    'Ext.layout.container.Border',
    'Ext.ux.layout.Center',
    'Abada.menu.HorizontalMainMenu'
]);

Ext.onReady(function() {
    var menu = Ext.create('Abada.menu.HorizontalMainMenu', {
        url: getRelativeURI('mainmenu.do'),
        autoLoadData: true
    });

    var container = Ext.create('Ext.container.Container', {
        region: 'center',   
        margins:'10 75 10 75',
        items: [
            {                
                height: 150,                       
                style: {
                    borderTopLeftRadius: '5px',
                    borderTopRightRadius: '5px',
                    border: '0px'
                },
                items: [{
                        html: '<div style=\"float:left;padding:10px;\"><img alt=\" \" src=\"' + getRelativeURI('/images/logos/abada.png') + '\" style=\"height:60px;\" /></div>'
                                + '<div style=\"float:left;padding:10px;\"><img alt=\" \" src=\"' + getRelativeURI('/images/logos/cleia.jpg') + '\" style=\"height:60px;\" /></div>'
                                //+'<div style=\"clear: both\" />'
                                ,
                        border: false,
                        height: 93,
                        margins: '0 0 5 0'
                    }, menu]
            }, {                                
                layout:'fit',
                id: 'centralPanel'
            }]
    });

    var view = Ext.create('Ext.container.Viewport', {
        cls: ['body-abada'],
        autoScroll: true,
        layout: {
            type: 'border'
        },
        items: [
            container
        ]
    });
});

