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

Ext.onReady(function(){               
    var menu=Ext.create('Abada.menu.HorizontalMainMenu',{
        url:getRelativeURI('mainmenu.do'),        
        autoLoadData:true,
        //height:'50px',
        width:'100%'
    });
    
    var container=Ext.create('Ext.container.Container',{               
        width:'85%',
        style:{
            margin:'10 auto 10 auto'
        },        
        items:[
        {
            //region: 'north',
            height:'150px',            
            //bbar:[menu],                
            style:{
                borderTopLeftRadius: '5px',
                borderTopRightRadius: '5px',
                border:'0px'
            },
            items:[{                
                html: '<div style=\"float:left;padding:10px;\"><img alt=\" \" src=\"'+getRelativeURI('/images/logos/abada.png')+'\" style=\"height:60px;\" /></div>'
                //+'<div style=\"clear: both\" />'
                ,
                autoHeight:true,
                border: false,
                height:93,
                margins: '0 0 5 0'
            },menu]
        },{
            //region: 'center',
            id:'centralPanel',
            autoScroll:true
        }]
    });
    
    var view=Ext.create('Ext.container.Viewport', {      
        //layout: 'ux.center',               
        autoScroll:true,
        cls: ['body-abada'],
        items: [            
        container
        ]
    });   
});

