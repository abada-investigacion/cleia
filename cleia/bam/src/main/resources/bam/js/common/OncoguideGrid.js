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

Ext.define('App.bam.js.common.OncoguideGrid', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature','Ext.grid.column.Date'],
    extend:'Abada.grid.Panel',
    config:{
        url:undefined,
        patientId:undefined,
        i18n:undefined
    },
    forceFit:true,
    features:[{
        ftype: 'filters',
        autoReload: false,
        local: true, 
        encode:true,
        filters: [{
            type: 'numeric',
            dataIndex: 'patientId'
        }, {
            type: 'string',
            dataIndex: 'processId'
        },
        {
            type: 'string',
            dataIndex: 'type'
        },{
            type: 'string',
            dataIndex: 'processName'
        }, {
            type: 'string',
            dataIndex: 'processInstanceId'
        }, {
            type: 'date',
            //dateFormat : 'd/m/Y', 
            dataIndex: 'start'
        }, {
            type: 'date',
            //dateFormat : 'd/m/Y', 
            dataIndex: 'end'
        }]
    }],
    columns:[           
    {
        header: 'bam.processgrid.patientid', 
        dataIndex: 'patientId',
        hidden:true    
    },{
        header: 'bam.processgrid.processid',  
        dataIndex: 'processId',
        hidden:true
    
    },{
        header: 'bam.processgrid.type', 
        dataIndex: 'type',
        hidden:true    
    },{
        header: 'bam.processgrid.processname', 
        dataIndex: 'processName'
    
    },{
        header: 'bam.processgrid.processinstanceid', 
        dataIndex: 'processInstanceId'
    
    },{
        header: 'bam.processgrid.start', 
        dataIndex: 'start',
        xtype: 'datecolumn', 
        format:'Y-m-d H:i'
    },{
        header: 'bam.processgrid.end', 
        dataIndex: 'end',
        xtype: 'datecolumn', 
        format:'Y-m-d H:i'
    }],
    constructor:function(config){       
        this.initConfig(config);  
       
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore', {    
                url:this.config.url,                
                root:'data',                                
                scope:this,
                pageSize:100,
                fields:['patientId', 'processId','processName', 'processInstanceId',{
                    name:'start',
                    type:'date',
                    dateFormat:'c'
                },{
                    name:'end',
                    type:'date',
                    dateFormat:'c'
                },'type'],
//                sorters:[
//                {
//                    property:'processInstanceId', 
//                    direction:'desc'
//                }
//                ],
                extraParams:{
                    patientId:this.config.patientId
                },
                proxyConfigs:{
                    timeout:90000
                }
            });            
            
        }
        this.selModel= Ext.create('Ext.selection.RowModel');
        this.bbar= Ext.create('Ext.toolbar.Paging', {
            store: this.store,
            pageSize: this.store.pageSize
        });                        
                
        var button=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/historic.gif'),
            text    : this.i18n.getMsg('bam.processgrid.history'),
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0){                    
                    this.fireEvent('historyclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        var button1=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/flow.png'),
            text    : this.i18n.getMsg('bam.processgrid.diagram'),
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0){                    
                    this.fireEvent('graphclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        var button2=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/datos.gif'),
            text    : this.i18n.getMsg('bam.processgrid.data'),
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0){                    
                    this.fireEvent('dataclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        var button3=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/humanTask.png'),
            text    : this.i18n.getMsg('bam.processgrid.task'),
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0 && !this.selModel.getLastSelected().data.end){                    
                    this.fireEvent('taskclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        var button4=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/evento.gif'),
            text    : this.i18n.getMsg('bam.processgrid.events'),
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0 && !this.selModel.getLastSelected().data.end){                    
                    this.fireEvent('signalclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        
        var button5=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/version.png'),
            text    : this.i18n.getMsg('bam.processgrid.version'),
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0 && !this.selModel.getLastSelected().data.end){                    
                    this.fireEvent('versionclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        
        if (this.tbar){
            this.tbar.add(button,button1,button2,button3,button4,button5);
        }else{
            this.tbar=Ext.create('Ext.toolbar.Toolbar',{
                items:[button,button1,button2,button3,button4,button5]
            });
        }
                
        this.callParent([config]);
        
        this.addListener('itemdblclick',this.onDoubleClickTask,this);
        //this.addListener('itemclick',this.onClickTask,this);
        this.addEvents('processinstanceselected','historyclick','graphclick','dataclick','taskclick','signalclick','versionclick');                                        
    },
    onDoubleClickTask:function(grid,record,item,index){        
        this.fireEvent('processinstanceselected',grid,record.data.processInstanceId);
    }
});
