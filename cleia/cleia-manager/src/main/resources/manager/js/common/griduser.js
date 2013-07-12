/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



Ext.define('App.manager.js.common.griduser', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature','Ext.selection.CheckboxModel', 'Ext.ux.CheckColumn',
    'Abada.grid.column.CheckBox'],
    extend:'Ext.grid.Panel',
    config:{
        checkboxse:undefined,
        loadMask: true,
        page:14
    },
    title: 'Usuarios',
 
    columns:[           
    {
        header: 'id', 
        dataIndex: 'id',
        hidden:true,
        width:50
    
    },{
        header: 'Usuario', 
        dataIndex: 'username',
        width:50
    
    },
    {
        header: 'Habilitado', 
        dataIndex: 'enabled',
        align:'center',
        xtype: 'checkboxcolumn',
        width:35

    }

    ],
    features:[{
        ftype: 'filters',
        autoReload: true,
        local: false, 
        encode:true,
        filters: [ {
            type: 'boolean',
            dataIndex: 'enabled'
        },{
            type: 'string',
            dataIndex: 'username'
        }]
    }],
    forceFit:true,
    
    constructor:function(config){
        this.initConfig(config);  
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore',{
                storeId:'griduserStore',
                sorters: {
                    property: 'username',
                    direction: 'ASC'
                },
                fields:[{
                    name:'id',
                    mapping:'id'
                },{
                    name:'username',
                    mapping:'username'
                },{
                    name:'enabled',
                    mapping:'enabled',
                    type: 'boolean'
                },{
                    name:'password',
                    mapping:'password'
                },{
                    name:'roles',
                    mapping:'roles'
                },{
                    name:'groups',
                    mapping:'groups' //grupo
                }],
                url:this.config.url,                
                root:'data',                                
                scope:this,
                pageSize:config.page
            }); 
        }
        if(config.checkboxse){
            this.selModel=Ext.create('Ext.selection.CheckboxModel',{
                checkOnly : true
            });
        } else{
            this.selModel= Ext.create('Ext.selection.RowModel');
        }
        this.bbar= Ext.create('Ext.toolbar.Paging', {
            store: this.store,
            pageSize: this.store.pageSize
        });
        this.callParent([config]);
    }

    
});
