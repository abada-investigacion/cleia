/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.patient.js.common.gridPatient', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'
    ,'Ext.ux.grid.FiltersFeature', 'Ext.ux.CheckColumn',
    'Abada.grid.column.CheckBox','Ext.grid.column.Date','Abada.grid.RowExpander'],
    extend:'Ext.grid.Panel',
    config:{
        loadMask: true,
        page:13
    },
    title: 'Pacientes',
    //    plugins:[{
    //        ptype: 'abada.rowexpander',
    //        rowBodyTpl: [
    //        '<div>',
    //        '<p><b>ID:</b></p>',
    //        '</tpl>',
    //        '<tpl for="patientidList">',
    //        '<li><b>{#}. </b> {idTypeidIdType.name}: {value}</li>',
    //        '</tpl>',
    //        '</div>'
    //        ]
    //     
    //    
    //    }],
    columns:[  
    {
        header:'Id',
        dataIndex:'id',
        hidden:true
    },
    {
        header: 'Nombre paciente', 
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
        dataIndex: 'birthDay',
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