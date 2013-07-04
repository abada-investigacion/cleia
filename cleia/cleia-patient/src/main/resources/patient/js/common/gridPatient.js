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
    plugins:[{
        ptype: 'abada.rowexpander',
        rowBodyTpl: [
        '<div>',
        '<p><b>ID:</b></p>',
        '</tpl>',
        '<tpl for="patientidList">',
        '<li><b>{#}. </b> {idTypeidIdType.name}: {value}</li>',
        '</tpl>',
        '</div>'
        ]
     
    
    }],
    columns:[           
    {
        header: 'Nombre paciente', 
        dataIndex: 'name',
        width:50
    
    },
    {
        header: 'Apellido', 
        dataIndex: 'surname1',
        width:50
    
    },
    {
        header: 'Apellido 2', 
        dataIndex: 'surname2',
        width:50
    
    },
    {
        header: 'Genero', 
        dataIndex: 'genre',
        width:50
    
    } ,{
        header: 'Fecha nacimiento', 
        dataIndex: 'birthday',
        xtype: 'datecolumn',
        sortable: true,
        align: 'right',
        format: 'd-m-Y'
        
    
    },
    {
        header: 'Esta muerto', 
        dataIndex: 'exitus',
        align:'center',
        xtype: 'checkboxcolumn',
        width:35

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
            dataIndex: 'name'
        },
        {
            type: 'string',
            dataIndex: 'surname1'
        },{
            type: 'string',
            dataIndex: 'surname2'
        },
        {
            type: 'boolean',
            dataIndex: 'exitus'
        },{
            type: 'boolean',
            dataIndex: 'enabled'
        },{
            type: 'list',
            enumType:'com.abada.cleia.entity.user.Genre',
            dataIndex: 'genre',
            options: [['Male','Male'],
            ['Female','Female'],
            ['Undefined','Undefined']]
        },{
            
            
            type: 'date',
            dataIndex: 'birthday',
            dateFormat : 'd-m-Y'
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
                    name:'birthday',
                    type       : 'date',
                    dateFormat : 'c' 
                },
                'idPatient', 'name', 'surname1','surname2','exitus','enabled','genre','patientidList','oncoguideList']
                ,
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
