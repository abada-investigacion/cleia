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
        header: 'Servicio', 
        dataIndex: 'value',
        width:50
    
    },
    {
        header: 'Habilitado', 
        dataIndex: 'enabled',
        align:'center',
        xtype: 'checkboxcolumn',
        width:50

    }],
    features:[{
        ftype: 'filters',
        autoReload: true,
        local: false, 
        encode:true,
        filters: [ {
            type: 'string',
            dataIndex: 'value'
        },{
            type: 'boolean',
            dataIndex: 'enabled'
        }]
    }],
    forceFit:true,
    constructor:function(config){
        this.initConfig(config);  
       
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore',{
                storeId:'gridgroupStore',
                sorters: {
                    property: 'value',
                    direction: 'ASC'
                },
                fields:[{
                    name:'value',
                    mapping:'value'
                },{
                    name:'enabled',
                    mapping:'enabled',
                    type: 'boolean'
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
