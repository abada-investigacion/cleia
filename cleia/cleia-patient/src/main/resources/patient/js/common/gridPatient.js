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

Ext.define('App.patient.js.common.gridPatient', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature', 'Ext.ux.CheckColumn','Ext.selection.CheckboxModel',
    'Abada.grid.column.CheckBox','Ext.grid.column.Date','Abada.grid.RowExpander'],
    extend:'Abada.grid.Panel',
    config:{
        checkboxse:undefined,
        bbar:undefined,
        loadMask: true,
        page:undefined,
        details:undefined,
        i18n:undefined
      
    },
    title: 'gridpatient.title',
    columns:[  
    {
        header:'gridpatient.id',
        dataIndex:'id',
        width:30,
        hidden:true
    },
    {
        header: 'gridpatient.name', 
        dataIndex: 'name',
        width:40
    
    },
    {
        header: 'gridpatient.surname', 
        dataIndex: 'surname',
        width:40
    
    },
    {
        header: 'gridpatient.surname1', 
        dataIndex: 'surname1',
        width:40
    
    },
    {
        header: 'gridpatient.genre', 
        dataIndex: 'genre',
        width:30,
        hidden:true
    
    },
    {
        header: 'gridpatient.birthDay', 
        dataIndex: 'birthDay',
        xtype: 'datecolumn',
        sortable: true,
        format: 'd-m-Y',
        width:40
    },
    {
        header: 'gridpatient.tlf', 
        dataIndex: 'tlf',
        width:40    
    },
    {
        header: 'gridpatient.address', 
        renderer:templateRenderer(new Ext.Template('{address}, {city}, {cp}, {country}')) ,
        width:50,
        hidden:true
    },
    {
        header: 'gridpatient.enabled', 
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
            dataIndex: 'name'
        },
        {
            type: 'string',
            dataIndex: 'surname'
        },
        {
            type: 'string',
            dataIndex: 'surname1'
        },
        {
            type: 'list',
            enumType:'com.abada.cleia.entity.user.Genre',
            dataIndex: 'genre',
            options: [['Male','Male'],
            ['Female','Female'],
            ['Undefined','Undefined']]
        },
        {   
            type: 'date',
            dataIndex: 'birthDay',
            dateFormat : 'd-m-Y'
        },
        {
            type: 'string',
            dataIndex: 'address'
        },
        {
            type: 'string',
            dataIndex: 'tlf'
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
                    property: 'name',
                    direction: 'ASC'
                },
                fields:[
                {
                    name:'birthDay',
                    type       : 'date',
                    dateFormat : 'c' 
                },
                {
                    name:'id',
                    mapping:'id'
                },
                {
                    name:'name',
                    mapping:'name'
                }, {
                    name:'surname',
                    mapping:'surname'
                },{
                    name:'surname1',
                    mapping:'surname1'
                },{
                    name:'genre',
                    mapping:'genre'
                },{
                    name:'city',
                    mapping:'address.city'
                },{
                    name:'address',
                    mapping:'address.address'
                },{
                    name:'cp',
                    mapping:'address.cp'
                },{
                    name:'country',
                    mapping:'address.countryAddress'
                },{
                    name:'tlf',
                    mapping:'tlf'
                },{
                    name:'username',
                    mapping:'user.username'
                },{
                    name:'enabled',
                    mapping:'user.enabled',
                    type: 'boolean'
                },{
                    name:'roles',
                    mapping:'user.roles'
                },{
                    name:'groups',
                    mapping:'user.groups' //grupo
                },{
                    name:'ids',
                    mapping:'user.ids'
                },{
                    name:'medicals',
                    mapping:'medicals'
                }],
                url:this.config.url,                
                root:'data',                                
                scope:this,
                pageSize:config.page
            }); 
        }
        
        if(config.details){
            
            this.columns.push({
                xtype:'actioncolumn',
                width:30,
                header:'gridpatient.Detalles',
                align:'center',
                items: [{
                    icon: getRelativeURI('patient/image/group-expand.png'),  
                    handler: function(grid, rowIndex, colIndex) {
                        var record = grid.getStore().getAt(rowIndex);
                        getDetails(record);
                    }
                }]
            }) 
            
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

function templateRenderer(template) {
    return function(value, meta, record, rowIndex, colIndex, store) {
        return template.applyTemplate(record.data);
    };
}