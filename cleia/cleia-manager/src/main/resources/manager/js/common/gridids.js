/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



Ext.define('App.manager.js.common.gridids', {
    requires: ['Ext.toolbar.Paging',
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
        header: 'Numero', 
        dataIndex: 'value',
        width:50
    
    },{
        header: 'Tipo', 
        dataIndex: 'idtype',
        width:50
    }
    ],
    forceFit:true,
    
    constructor:function(config){
        this.initConfig(config);  
            
            this.store=Ext.create('Abada.data.JsonStore',{
                storeId:'gridIdStore',
             
                fields:[{
                    name:'value',
                    mapping:'value'
                },{
                    name:'idtype',
                    mapping:'type.value'
                }],             
                url:this.config.url,                
                root:'data',
                scope:this,
                pageSize:config.page
            }); 
        
         if(config.checkboxse){
            this.selModel=Ext.create('Ext.selection.CheckboxModel',{
                checkOnly : true
            });
        } else{
            this.selModel= Ext.create('Ext.selection.RowModel');
        }
       
        this.callParent([config]);
    }

    
});
