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



Ext.define('App.manager.js.common.griduser', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature','Ext.selection.CheckboxModel', 'Ext.ux.CheckColumn',
    'Abada.grid.column.CheckBox'],
    extend:'Abada.grid.Panel',
    config:{
        checkboxse:undefined,
        bbar:undefined,
        loadMask: true,
        page:undefined,
        i18n:undefined
    }, 
    columns:[           
    {
        header: 'manager.grid.column.idTitle', 
        dataIndex: 'id',
        width:25
    
    },{
        header: 'manager.grid.column.usernameTitle', 
        dataIndex: 'username',
        width:50
    
    },
    {
        header: 'manager.grid.column.enabledTitle', 
        dataIndex: 'enabled',
        align:'center',
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
            dataIndex: 'enabled'
        },{
            type: 'string',
            dataIndex: 'username'
        }]
    }],
    forceFit:true,
    
    constructor:function(config){
        this.initConfig(config);  
        if (config.url || config.url==undefined){
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
                },{
                    name:'ids',
                    mapping:'ids'
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
        
        if(config.bbar || config.bbar==undefined){
            this.bbar= Ext.create('Ext.toolbar.Paging', {
                store: this.store,
                pageSize: this.store.pageSize
            });
        }
        
        this.callParent([config]);
    }

    
});
