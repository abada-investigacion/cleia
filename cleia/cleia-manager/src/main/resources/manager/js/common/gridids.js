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
