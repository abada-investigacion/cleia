/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('App.manager.js.common.gridrole', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature','Ext.selection.CheckboxModel'],
    extend:'Ext.grid.Panel',
    config:{
        checkboxse:undefined,
        scroll:true,
        page:14
    },
    title: 'Roles',

    columns:[           
    {
        header: 'Roles', 
        dataIndex: 'authority'
    
    }],

    forceFit:true,
    features:[{
        ftype: 'filters',
        autoReload: true,
        local: false, 
        encode:true,
        filters: [ {
            type: 'string',
            dataIndex: 'authority'
        }]
    }],
        
   
    constructor:function(config){
       
        this.initConfig(config);  
       
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore',{
                storeId:'gridRoleStore',
                sorters: {
                    property: 'authority',
                    direction: 'ASC'
                },
                fields:[{
                    name:'authority',
                    mapping:'authority'
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
            
        }

        this.bbar= Ext.create('Ext.toolbar.Paging', {
            store: this.store,
            pageSize: this.store.pageSize
        });  
              
        this.callParent([config]);
        
    }
    
});
