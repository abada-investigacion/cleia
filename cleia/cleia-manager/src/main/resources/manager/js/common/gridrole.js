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
Ext.define('App.manager.js.common.gridrole', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature'],
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
