/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


Ext.define('App.bam.js.common.PatientGrid', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature'],
    extend:'Ext.grid.Panel',
    config:{
        url:undefined
    },
    forceFit:true,
    title: 'Pacientes',
    features:[{
        ftype: 'filters',
        autoReload: true,
        local: false, 
        encode:true,
        filters: [{
            type: 'string',
            dataIndex: 'name'
        }, {
            type: 'string',
            dataIndex: 'surname'
        }, {
            type: 'string',
            dataIndex: 'surname1'
        }, {
            type: 'date',
            dateFormat : 'd/m/Y',            
            dataIndex: 'birthDay'
        }, {
            type: 'string',
            dataIndex: 'genre'
        }]
    }],
    columns:[           
    {
        header: 'Id', 
        dataIndex: 'id',
        hidden:true    
    },{
        header: 'Nombre', 
        dataIndex: 'name'
    
    },{
        header: 'Apellido 1', 
        dataIndex: 'surname'
    
    },{
        header: 'Apellido 2', 
        dataIndex: 'surname1'
    
    },{
        header: 'Fecha nacimiento', 
        dataIndex: 'birthDay'
    
    },{
        header: 'Genero', 
        dataIndex: 'genre'
    
    },{
        header: 'Lista Paciente', 
        dataIndex: 'ids',
        hidden:true
    
    },{
        header: 'Lista Oncoguia', 
        dataIndex: 'processInstances',
        hidden:true    
    }],
    constructor:function(config){       
        this.initConfig(config);  
       
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore', {    
                url:this.config.url,                
                root:'data',                                
                scope:this,
                pageSize:20,
                fields:['id', 'name', 'surname','surname1','ids','processInstances',{
                    name:'birthDay',
                    xtype:'date',
                    dateFormat:'c'
                },'genre']
            });
        }
        this.selModel= Ext.create('Ext.selection.RowModel');
        this.bbar= Ext.create('Ext.toolbar.Paging', {
            store: this.store,
            pageSize: this.store.pageSize
        });                        
                
        this.callParent([config]);
        
        this.addListener('itemdblclick',this.onDoubleClickTask,this);
        this.addEvents('patientselected');        
        
    },
    onDoubleClickTask:function(grid,record,item,index){  
        var patientname=record.data.name+" "+record.data.surname+" "+record.data.surname1;
        this.fireEvent('patientselected',grid,record.data.id,patientname);
    }
});
