/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


Ext.define('App.patient.js.common.gridIdtype', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Ext.ux.grid.FiltersFeature',
    'Abada.grid.RowExpander','Ext.selection.CheckboxModel', 'Ext.ux.CheckColumn',
    'Ext.util.*','Abada.grid.column.CheckBox'],
    extend:'Ext.grid.Panel',
    config:{
        checkboxse:undefined,
        loadMask: true,
        page:14   
    },
    
    columns:[           
    {
        header: 'Id', 
        dataIndex: 'value',
        width:50    
    },{
        header: 'Descripci&oacute;n', 
        dataIndex: 'description',
        width:50
    },{
        header: 'Se puede repetir', 
        dataIndex: 'repeatable',
        xtype: 'checkboxcolumn',
        width:50
    }
    ],
    features:[{
        ftype: 'filters',
        autoReload: true,
        local: false, 
        encode:true,
        filters: [ {
            type: 'boolean',
            dataIndex: 'repeatable'
        },{
            type: 'string',
            dataIndex: 'value'
        }]
    }],
    forceFit:true,    
    constructor:function(config){
        this.initConfig(config);  
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore',{
                storeId:'grididtypeStore',
                sorters: {
                    property: 'value',
                    direction: 'ASC'
                },
                fields:[{
                    name:'value',
                    mapping:'value'
                },{
                    name:'description',
                    mapping:'description'
                },{
                    name:'repeatable',
                    mapping:'repeatable',
                    type: 'boolean'

                }],
                url:this.config.url,                
                root:'data',                                
                scope:this,
                pageSize:config.page                                    
            }); 
        }
        
        if(config.checkboxse){
            this.selModel=Ext.create('Ext.selection.CheckboxModel');
        } else{
            this.selModel= Ext.create('Ext.selection.RowModel');
        }
        
        this.bbar= Ext.create('Ext.toolbar.Paging', {
                store: this.store,
                pageSize: this.store.pageSize
            });
        
        this.callParent([config]);
    },
    renderTo: Ext.getBody()    
});
