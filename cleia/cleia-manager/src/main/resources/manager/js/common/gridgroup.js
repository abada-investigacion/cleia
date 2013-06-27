/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.manager.js.common.gridgroup', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature'],
    extend:'Ext.grid.Panel',
    config:{
        checkboxse:undefined,
        page:14
    },
    title: 'Servicios',
    columns:[           
    {
        header: 'Servicios', 
        dataIndex: 'name'
    
    }],
    features:[{
        ftype: 'filters',
        autoReload: true,
        local: false, 
        encode:true,
        filters: [ {
            type: 'string',
            dataIndex: 'name'
        }]
    }],
    forceFit:true,
    constructor:function(config){
        this.initConfig(config);  
       
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore',{
                storeId:'gridgroupStore',
                sorters: {
                    property: 'name',
                    direction: 'ASC'
                },
                fields:[{
                    name:'idGroup',
                    mapping:'idGroup'
                },{
                    name:'name',
                    mapping:'name'
                }],
                url:this.config.url,                
                root:'data',                                
                scope:this,
                pageSize: this.config.page
                
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
            pageSize: this.config.page
        });
        this.callParent([config]);
    }

    
});
