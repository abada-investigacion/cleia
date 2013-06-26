/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require(['Ext.window.Window']);

Ext.onReady(function(){   
    
    function onTaskSelected(grid,taskId,taskUrl){
        var panelAux=Ext.create('App.bam.js.common.FormCustomPanel',{
            //height:600,
            taskId:taskId,
            url:getRelativeURI('/bam/')
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
            height: 600,
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
        height:600,        
        url:getRelativeURI('/bam/task/user/task.do')
    });      
    panel.addListener('taskselected',onTaskSelected);
    
    setCentralPanel(panel);
    
    panel.getStore().load();
});
