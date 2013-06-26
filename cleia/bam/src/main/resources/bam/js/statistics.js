/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require(['Ext.window.Window']);



Ext.onReady(function(){   

    var startPanel=Ext.create('Abada.form.field.Date',{});
    var endPanel=Ext.create('Abada.form.field.Date',{});
    var cbOncoguide=Ext.create('Abada.form.field.SimpleGroupingComboBox',{
        url:getRelativeServerURI('/rs/process/definition/list/combo')
    }); 
    var button=Ext.create('Ext.button.Button', {
        text: 'Ver',
        scope:this,
        handler: function() {
            if(cbOncoguide.getValue()){
                var panelAux=Ext.create('App.bam.js.common.ProcessInstancePanel',{
                    urlImage:getRelativeURI('/bam/statistic/image.do'),
                    processInstanceId:cbOncoguide.getValue(),
                    start:startPanel.getValue(),
                    end:endPanel.getValue()
                });
            }else{
                Ext.Msg.alert('', 'Seleccione un proceso');
            }
           
            
            var win=Ext.create('Ext.window.Window', {
                title: 'Estadisticas de '+cbOncoguide.getValue(),
                height: 600,
                width: 900,
                autoScroll:true,
                layout: 'fit',
                modal:true,
                items: [panelAux]
            });
            win.show();
        }
    });

    var panel=Ext.create('Ext.panel.Panel',{
        title:'Estadisticas',
        height:App.height,
        items:[
        cbOncoguide,startPanel,endPanel,button
        ]
    });

    setCentralPanel(panel);        
});
