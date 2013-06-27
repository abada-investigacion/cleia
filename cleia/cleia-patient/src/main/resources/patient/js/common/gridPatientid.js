/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


Ext.define('App.patient.js.common.gridPatientid', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Ext.ux.grid.FiltersFeature',
    'Abada.grid.RowExpander','Ext.selection.CheckboxModel','Ext.util.*','Ext.grid.plugin.CellEditing','Abada.form.field.ComboBoxDeSelect'],
    extend:'Ext.grid.Panel',
    config:{
        loadMask: true,
        page:14,
        idpatient:null
    },
    id:'gridPatientid',
    title: 'ID Pacientes',
    rowspan: 4,
    columns:[           
    {
        header: 'Id', 
        dataIndex: 'idPatientId',
        hidden:true,
        width:50    
    },
    {
        header: 'Id Identificador', 
        dataIndex: 'idIdType',
        hidden:true,
        width:50
    },{
        header: 'Identificador', 
        dataIndex: 'name',
        width:50        
    },
    {
        header: 'Valor', 
        dataIndex: 'value',
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
            dataIndex: 'value'
        },{
            type: 'string',
            dataIndex: 'idTypeidIdType.name'
        }]
    }],
    forceFit:true,    
    constructor:function(config){
        this.initConfig(config);  
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore',{
                storeId:'gridPatientidStore',
                sorters: {
                    property: 'name',
                    direction: 'ASC'
                },
                fields:[{
                    name:'idPatientId',
                    mapping:'idPatientId'
                },{
                    name:'value',
                    mapping:'value'
                },{
                    name:'idIdType',
                    mapping:'idTypeidIdType.idIdType'

                },{
                    name:'name',
                    mapping:'idTypeidIdType.name'

                }],
                url:this.config.url,                
                root:'data',                                
                scope:this,
                pageSize:config.page                                    
            }); 
        }
        
        
        this.bbar= Ext.create('Ext.toolbar.Paging', {
            store: this.store,
            pageSize: this.store.pageSize
        });
        
        this.callParent([config]);
    },
    initComponent: function(){
        this.editing = Ext.create('Ext.grid.plugin.CellEditing');
        Ext.apply(this, {
            iconCls: 'icon-grid',
            frame: true,
            plugins: [this.editing],
            dockedItems: [{
                xtype: 'toolbar',
                items: [{
                    icon:getRelativeURI('images/custom/add.gif'),
                    text: 'Insertar',
                    scope: this,
                    handler: this.onAddClick
                }, {
                    icon:getRelativeURI('images/custom/changestatus.png'),
                    text: 'Modficar',
                    disabled: true,
                    itemId: 'update',
                    scope: this,
                    handler: this.onUpdateClick
                }, {
                    icon:getRelativeURI('images/custom/delete.gif'),
                    text: 'Borrar',
                    disabled: true,
                    itemId: 'delete',
                    scope: this,
                    handler: this.onDeleteClick
                }]
            }]
        });
        this.callParent();
        this.getSelectionModel().on('selectionchange', this.onSelectChange, this);
    },
    
    onSelectChange: function(selModel, selections){
        this.down('#update').setDisabled(selections.length === 0);
        this.down('#delete').setDisabled(selections.length === 0);
    },

    onSync: function(){
        this.store.sync();
    },

    onDeleteClick: function(){
        var selection = this.getView().getSelectionModel().getSelection()[0];
        if (selection) {
            this.store.remove(selection);
        }
    },
    onUpdateClick: function(){
        var selection = this.getView().getSelectionModel().getSelection()[0];
        if (selection) {
            handleFormulario('Modifica',selection)
        }
    },

    onAddClick: function(){
        handleFormulario('Inserta',null)

        
    }
});

function handleFormulario(opt,selection){
    var name,idtype,value;

    if(selection!=null){
        name=selection.get('name');
        idtype=selection.get('idIdType');
        value=selection.get('value');
    }
    var comboidentificadores=Ext.create('Abada.form.field.ComboBoxDeSelect',{
        id:'idIdType',
        url:getRelativeServerURI('rs/idtype/search/combo'),
        emptyText : 'seleccione un Identificador',            
        noSelection:'seleccione un Identificador',
        editable:false,
        maxWidth:225,
        allowBlank : false,
        selectedValue:'',
        listeners:{
            select: function(combo, record, index) {
                name=record[0].get('value');
            },
            load:function(){
                if(idtype!=undefined){
                    comboidentificadores.setValue(''+idtype);
                }
            }
            
        }
    });
   
    comboidentificadores.loadStore()
    var formpanel = Ext.create('Ext.form.Panel', {
        title : 'Id Paciente',
        defaultType : 'textfield',
        monitorValid : true,
        layout: {
            type: 'table',
            columns: 2
        }, 
        defaults: {
            frame:true, 
            width:250
        },
        items : [ 
        comboidentificadores, {
            fieldLabel : 'Valor',
            name : 'value',
            id:'value',
            value:value,
            allowBlank : false,
            width:225
            

        }
            
        ],
        buttons:[{
            text:opt+'r',           
            formBind:true,
            handler:function(){
                if(formpanel.getForm().isValid()){
                    var rec = new Writer.Patientid({
                        value: formpanel.getComponent("value").getValue(),
                        idIdType: formpanel.getComponent("idIdType").getValue(),
                        name: name,
                        idPatientId:null
                    })
                    var store=Ext.getCmp('gridPatientid').store;
                    if(opt!='Modifica'){
                        store.insert(0, rec);
                    } else{
                        index=store.indexOf(selection)
                        store.remove(selection)
                        store.insert(index, rec);
                    }
                    wind.close();
                }
                   
            }
        }]
    });
      
    var wind=Ext.create('Ext.window.Window',{
        id:'Paciente id',
        autoScroll:false,
        closable:true,
        modal:true,
        items:[formpanel]
    });

    wind.show();

}
Ext.define('Writer.Patientid', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'idPatientId'
        
    }, 'value', 'idIdType', 'name'],
    validations: [{
        type: 'length',
        field: 'value',
        min: 1
    }, {
        type: 'length',
        field: 'idTypeidIdType.idIdType',
        min: 1
    }, {
        type: 'length',
        field: 'idTypeidIdType.name',
        min: 1
    }]
});