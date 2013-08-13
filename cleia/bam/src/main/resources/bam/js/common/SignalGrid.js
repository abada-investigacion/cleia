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

Ext.define('App.bam.js.common.SignalGrid',{
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature'],
    extend:'Ext.grid.Panel',
    config:{
        url:undefined,
        processId:undefined,
        processInstanceId:undefined
    },
    forceFit:true,
    features:[{
        ftype: 'filters',
        autoReload: false,
        local: true, 
        encode:true,
        filters: [{
            type: 'number',
            dataIndex: 'id'
        }, {
            type: 'string',
            dataIndex: 'name'
        }, {
            type: 'string',
            dataIndex: 'type'
        }, {
            type: 'string',          
            dataIndex: 'eventType'
        }, {
            type: 'string',
            dataIndex: 'processId'
        }]
    }],
    columns:[           
    {
        header: 'id', 
        dataIndex: 'id',
        hidden:true    
    },{
        header: 'Nombre Tarea', 
        dataIndex: 'name'
    
    },{
        header: 'Tipo Tarea', 
        dataIndex: 'type',
        hidden:true
    
    },{
        header: 'Tipo Evento', 
        dataIndex: 'eventType'    
    },{
        header: 'Proceso que lo contiene', 
        dataIndex: 'processId'    
    }],
    constructor:function(config){       
        this.initConfig(config);  
       
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore', {    
                url:this.config.url,                
                root:'data',                                
                scope:this,
                pageSize:20,
                fields:['id', 'name', 'type','eventType','processId'],                
                extraParams:{
                    processId:this.config.processId
                }
            });
        }
        this.selModel= Ext.create('Ext.selection.RowModel');
        this.bbar= Ext.create('Ext.toolbar.Paging', {
            store: this.store,
            pageSize: this.store.pageSize
        });                        
                
        this.callParent([config]);
        
        this.addListener('itemdblclick',this.onDoubleClickTask,this);
        this.addEvents('signalselected');        
        
    },
    onDoubleClickTask:function(grid,record,item,index){        
        this.fireEvent('signalselected',grid,record.data.eventType,record.data.processId,this.config.processInstanceId);
    }
});
