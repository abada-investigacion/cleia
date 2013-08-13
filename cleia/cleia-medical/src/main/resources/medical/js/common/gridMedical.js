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

Ext.define('App.medical.js.common.gridMedical', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature', 'Ext.ux.CheckColumn',
    'Abada.grid.column.CheckBox','Ext.grid.column.Date','Abada.grid.RowExpander'],
    extend:'Ext.grid.Panel',
    config:{
        loadMask: true,
        page:13
    },
    title: 'Medicos',
    columns:[  
    {
        header:'Id',
        dataIndex:'id',
        hidden:true
    },
    {
        header: 'Nombre medico', 
        dataIndex: 'name',
        width:40
    
    },
    {
        header: 'Apellido', 
        dataIndex: 'surname',
        width:40
    
    },
    {
        header: 'Apellido 2', 
        dataIndex: 'surname1',
        width:40
    
    },
    {
        header: 'Genero', 
        dataIndex: 'genre',
        width:30
    
    },
    {
        header: 'Fecha nacimiento', 
        dataIndex: 'patient.birthDay',
        xtype: 'datecolumn',
        sortable: true,
        format: 'd-m-Y',
        width:40
    },
    {
        header: 'Telefono', 
        dataIndex: 'tlf',
        width:40    
    },
    {
        header: 'Direccion', 
        renderer:templateRenderer(new Ext.Template('{address}, {city}, {cp}, {country}')) ,
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
            type: 'string',
            dataIndex: 'patient.name'
        },
        {
            type: 'string',
            dataIndex: 'patient.surname'
        },
        {
            type: 'string',
            dataIndex: 'patient.surname1'
        },
        {
            type: 'list',
            enumType:'com.abada.cleia.entity.user.Genre',
            dataIndex: 'patient.genre',
            options: [['Male','Male'],
            ['Female','Female'],
            ['Undefined','Undefined']]
        },
        {   
            type: 'date',
            dataIndex: 'patient.birthDay',
            dateFormat : 'd-m-Y'
        },
        {
            type: 'string',
            dataIndex: 'patient.address'
        },
        {
            type: 'string',
            dataIndex: 'patient.tlf'
        }

        ]
    }],
    forceFit:true,

    constructor:function(config){
        this.initConfig(config);  
        if (config.url){
            
            this.store=Ext.create('Abada.data.JsonStore',{
                storeId:'gridPatientStore',
                sorters: {
                    property: 'patient.name',
                    direction: 'ASC'
                },
                fields:[
                {
                    name:'patient.birthDay',
                    type       : 'date',
                    dateFormat : 'c' 
                },
                {
                    name:'id',
                    mapping:'id'
                },
                {
                    name:'name',
                    mapping:'patient.name'
                }, {
                    name:'surname',
                    mapping:'patient.surname'
                },{
                    name:'surname1',
                    mapping:'patient.surname1'
                },{
                    name:'genre',
                    mapping:'patient.genre'
                },{
                    name:'city',
                    mapping:'patient.address.city'
                },{
                    name:'address',
                    mapping:'patient.address.address'
                },{
                    name:'cp',
                    mapping:'patient.address.cp'
                },{
                    name:'country',
                    mapping:'patient.address.countryAddress'
                },{
                    name:'tlf',
                    mapping:'patient.tlf'
                },{
                    name:'username',
                    mapping:'patient.user.username'
                },{
                    name:'enabled',
                    mapping:'patient.user.enabled',
                    type: 'boolean'
                },{
                    name:'roles',
                    mapping:'patient.user.roles'
                },{
                    name:'groups',
                    mapping:'patient.user.groups' //grupo
                },{
                    name:'ids',
                    mapping:'patient.user.ids'
                }],
                url:this.config.url,                
                root:'data',                                
                scope:this,
                pageSize:config.page
            }); 
        }
       
        this.selModel= Ext.create('Ext.selection.RowModel');
        this.bbar= Ext.create('Ext.toolbar.Paging', {
            store: this.store,
            pageSize: this.store.pageSize
        });
        this.callParent([config]);
    }

    
});

function templateRenderer(template) {
    return function(value, meta, record, rowIndex, colIndex, store) {
        return template.applyTemplate(record.data);
    };
}