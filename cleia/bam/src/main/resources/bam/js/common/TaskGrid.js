/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


Ext.define('App.bam.js.common.TaskGrid', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore','Ext.grid.column.Template'],
    extend:'Ext.grid.Panel',
    config:{
        url:undefined,
        processInstanceId:undefined,
        patientId:undefined
    },
    forceFit:true,
    title: 'Tareas',
    columns:[           
    {
        header: 'Id', 
        dataIndex: 'id',
        hidden:true
    
    },{
        header: 'Proceso instancia id', 
        dataIndex: 'processInstanceId',
        hidden:true
    
    },{
        header: 'Proceso id', 
        dataIndex: 'processId',
        hidden:true
    
    },{
        header: 'Nombre', 
        dataIndex: 'name'
    
    },{
        header: 'Asignado', 
        dataIndex: 'assignee'
    
    },{
        header: 'Bloqueada', 
        dataIndex: 'isBlocking'
    
    },{
        header: 'Señalización', 
        dataIndex: 'isSignalling'
    
    },{
        header: 'Estado Actual', 
        dataIndex: 'currentState'
    
    },{
        header: 'url', 
        dataIndex: 'url',
        hidden:true
    
    },{
        header: 'Fecha Debido', 
        dataIndex: 'dueDate',
        hidden:true
    
    },{
        header: 'Fecha creación', 
        dataIndex: 'createDate',
        hidden:true
    
    },{
        header: 'Prioridad', 
        dataIndex: 'priority'
    
    },{
        header: 'Descripción', 
        dataIndex: 'description'    
    },
    { header: 'Servicios Asignados', xtype: 'templatecolumn', tpl: '<tpl for="participantGroups"><p>{idRef}</p><br /></tpl>' }
    ,
    { header: 'Usuarios Potenciales Asignados', xtype: 'templatecolumn', tpl: '<tpl for="participantUsers"><p>{idRef}</p><br /></tpl>' , hidden:true}
    ],
    constructor:function(config){       
        this.initConfig(config);  
       
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore', {    
                url:this.config.url,                
                root:'data',                                
                scope:this,
                fields:['id', 'processInstanceId', 'processId','name','assignee','isBlocking','isSignalling',
                'currentState','url','dueDate','createDate','priority','description','participantGroups','participantUsers'],
                extraParams:{
                    processInstanceId:this.config.processInstanceId,
                    patientId:this.config.patientId
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
