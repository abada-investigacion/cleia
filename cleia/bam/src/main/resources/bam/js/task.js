/*
 * #%L
 * Cleia
 * %%
 * Copyright (C) 2013 Abada Servicios Desarrollo (investigacion@abadasoft.com)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
