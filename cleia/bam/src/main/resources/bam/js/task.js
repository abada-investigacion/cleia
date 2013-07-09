/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require(['Ext.window.Window']);

Ext.onReady(function(){   
    
    function onTaskSelected(grid,taskId,taskUrl){
        var panelAux=Ext.create('App.bam.js.common.FormCustomPanel',{
            height:App.height,
            taskId:taskId,
            url:taskUrl
        });
        panelAux.addListener('success',function(panel,frame,response){
            Ext.Msg.alert('Info','Tarea completada correctamente',                        
                function(){
                    win.close();    
                });
        },this);
        panelAux.addListener('failure',function(panel,frame,text){
            Ext.Msg.alert('Error','No se puede completar la tarea.'+text,                        
                function(){
                    win.close();    
                });
        },this);
        
        var win=Ext.create('Ext.window.Window', {
            title: 'Tarea '+taskId,
            height: App.height,
            width: 900,
            autoScroll:true,
            layout: 'fit',
            modal:true,
            items: [panelAux]
        });
        win.addListener('destroy',function(){
            panel.getStore().load();
        },this);
        win.show();
    }
    
    var panel=  Ext.create('App.bam.js.common.TaskGrid',{
        height:App.height,        
        url:getRelativeServerURI('rs/tasks/participation/loggeduser')
    });      
    panel.addListener('taskselected',onTaskSelected);
    
    setCentralPanel(panel);
    
    panel.getStore().load();
});
