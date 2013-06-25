/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


Ext.define('Oggi.bam.js.common.PatientGrid', {
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
            dataIndex: 'surname1'
        }, {
            type: 'string',
            dataIndex: 'surname2'
        }, {
            type: 'date',
            dateFormat : 'd/m/Y',            
            dataIndex: 'birthday'
        }, {
            type: 'string',
            dataIndex: 'typeGenre'
        }]
    }],
    columns:[           
    {
        header: 'Id', 
        dataIndex: 'idPatient',
        hidden:true    
    },{
        header: 'Nombre', 
        dataIndex: 'name'
    
    },{
        header: 'Apellido 1', 
        dataIndex: 'surname1'
    
    },{
        header: 'Apellido 2', 
        dataIndex: 'surname2'
    
    },{
        header: 'Fecha nacimiento', 
        dataIndex: 'birthday'
    
    },{
        header: 'Genero', 
        dataIndex: 'typeGenre'
    
    },{
        header: 'Lista Paciente', 
        dataIndex: 'patientidList',
        hidden:true
    
    },{
        header: 'Lista Oncoguia', 
        dataIndex: 'oncoguideList',
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
                fields:['idPatient', 'name', 'surname1','surname2','patientidList','oncoguideList',{
                    name:'birthday',
                    xtype:'date',
                    dateFormat:'c'
                },'typeGenre']
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
        var patientname=record.data.name+" "+record.data.surname1+" "+record.data.surname2;
        this.fireEvent('patientselected',grid,record.data.idPatient,patientname);
    }
});
