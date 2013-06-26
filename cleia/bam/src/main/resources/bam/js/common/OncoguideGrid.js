/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.bam.js.common.OncoguideGrid', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature','Ext.grid.column.Date'],
    extend:'Ext.grid.Panel',
    config:{
        url:undefined,
        patientId:undefined
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
        header: 'Id. Paciente', 
        dataIndex: 'patientId',
        hidden:true    
    },{
        header: 'Proceso Id',  
        dataIndex: 'processId',
        hidden:true
    
    },{
        header: 'Tipo', 
        dataIndex: 'type',
        hidden:true    
    },{
        header: 'Nombre Processo', 
        dataIndex: 'processName'
    
    },{
        header: 'Proceso Instancia Id', 
        dataIndex: 'processInstanceId'
    
    },{
        header: 'inicio', 
        dataIndex: 'start',
        xtype: 'datecolumn', 
        format:'Y-m-d H:i'
    },{
        header: 'fin', 
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
                sorters:[
                {
                    property:'processInstanceId', 
                    direction:'desc'
                }
                ],
                extraParams:{
                    patientId:this.config.patientId
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
            text    : 'Historial',
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0){                    
                    this.fireEvent('historyclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        var button1=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/flow.png'),
            text    : 'Diagrama',
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0){                    
                    this.fireEvent('graphclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        var button2=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/datos.gif'),
            text    : 'Datos',
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0){                    
                    this.fireEvent('dataclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        var button3=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/humanTask.png'),
            text    : 'Tareas',
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0 && !this.selModel.getLastSelected().data.end){                    
                    this.fireEvent('taskclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        var button4=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/evento.gif'),
            text    : 'Eventos',
            scope   : this,
            handler : function() {
                if (this.selModel.getCount()>0 && !this.selModel.getLastSelected().data.end){                    
                    this.fireEvent('signalclick',this,this.selModel.getLastSelected().data.processInstanceId,this.selModel.getLastSelected().data.processId);
                }
            }
        });
        
        var button5=Ext.create('Ext.button.Button', {
            icon:getRelativeURI('bam/image/version.png'),
            text    : 'Cambio Versi&oacute;n',
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
