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


Ext.define('App.bam.js.common.NodeHistoryGrid', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore','Ext.grid.column.Date'],
    extend:'Abada.grid.Panel',
    config:{
        url:undefined,
        processInstanceId:undefined,
        i18n:undefined
    },
    title: 'bam.nodegrid.title',
    forceFit:true,
    columns:[           
        {
            header: 'bam.nodegrid.id', 
            dataIndex: 'id',
            hidden:true
    
        },{
            header: 'bam.nodegrid.nodeid', 
            dataIndex: 'nodeId',
            hidden:true
    
        },{
            header: 'bam.nodegrid.processinstanceid', 
            dataIndex: 'processInstanceId',
            hidden:true
    
        },{
            header: 'bam.nodegrid.processid', 
            dataIndex: 'processId',
            hidden:true    
        },{
            header: 'bam.nodegrid.name', 
            dataIndex: 'nodeName'
    
        },{
            header: 'bam.nodegrid.date', 
            dataIndex: 'date',
            xtype: 'datecolumn', format:'Y-m-d H:i:s'    
        },{
            header: 'bam.nodegrid.nodeinstanceid', 
            dataIndex: 'nodeInstanceId',
            hidden:true
    
        },{
            header: 'bam.nodegrid.observation', 
            dataIndex: 'observation'    
        },{
            header: 'bam.nodegrid.type', 
            dataIndex: 'type',
            renderer:function(value){
                if (value=='1'){
                    return this.i18n.getMsg('bam.nodegrid.end');
                }else if (value=='0'){
                    return this.i18n.getMsg('bam.nodegrid.start');
                }else if (value=='2'){
                    return this.i18n.getMsg('bam.nodegrid.cancel');
                }
                return '';
            }
        }],
    constructor:function(config){       
        this.initConfig(config);  
       
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore', {    
                url:this.config.url,                
                root:'data',                                
                scope:this,
                fields:['id', 'nodeId', 'processInstanceId','processId','nodeName',{name:'date',type:'date',dateFormat:'c'},'observation','nodeInstanceId',
                    'type'],
                extraParams:{
                    processInstanceId:this.config.processInstanceId
                }
            });            
            }
        this.selModel= Ext.create('Ext.selection.RowModel');
        this.bbar= Ext.create('Ext.toolbar.Paging', {
            store: this.store/*,
            pageSize: this.store.pageSize*/
        });                     
                
        this.callParent([config]);
        
        this.addListener('itemdblclick',this.onDoubleClickTask,this);
        this.addEvents('taskselected');        
        
    },
    onDoubleClickTask:function(grid,record,item,index){        
        this.fireEvent('taskselected',grid,record.data.id,record.data.url);
    }
});
