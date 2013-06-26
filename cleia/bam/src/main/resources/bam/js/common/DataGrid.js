/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


Ext.define('App.bam.js.common.DataGrid', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore','Ext.grid.column.Date'],
    extend:'Ext.grid.Panel',
    config:{
        url:undefined,
        processInstanceId:undefined,
        patientId:undefined
    },
    forceFit:true,
    title: 'Datos',
    columns:[           
    {
        header: 'Id', 
        dataIndex: 'id'    
    },{
        header: 'valor', 
        dataIndex: 'value',
        renderer:function(value){
            return this.formatValue(value);
        }
    }],
    constructor:function(config){       
        this.initConfig(config);  
       
        if (config.url){
            this.store=Ext.create('Abada.data.JsonStore', {    
                url:this.config.url,                
                root:'data',                                
                scope:this,
                fields:['id', 'value'],
                extraParams:{
                    processInstanceId:this.config.processInstanceId,
                    patientId:this.config.patientId
                }
            });            
        }
        this.selModel= Ext.create('Ext.selection.RowModel');
        this.bbar= Ext.create('Ext.toolbar.Paging', {
            store: this.store/*,
            pageSize: this.store.pageSize*/
        });                     
                
        this.callParent([config]);      
        
    },
    formatValue:function(object){        
        if (!Ext.isPrimitive(object)){
            var member, members = [];

            // Cannot use Ext.encode since it can recurse endlessly (if we're lucky)
            // ...and the data could be prettier!
            Ext.Object.each(object, function (name, value,meself) {
                if (typeof(value) === "function") {
                    return;
                }

                if (!Ext.isDefined(value) || value === null ||
                    Ext.isDate(value) ||
                    Ext.isString(value) || (typeof(value) == "number") ||
                    Ext.isBoolean(value)) {
                    member = Ext.encode(value);
                } else if (Ext.isArray(value)) {
                    member = '[ '+this.formatValue(meself[name])+' ]';
                } else if (Ext.isObject(value)) {
                    member = '{ '+this.formatValue(meself[name])+' }';
                } else {
                    member = 'undefined';
                }
                members.push(Ext.encode(name) + ': ' + member);
            },this);

            if (members.length) {
                return members.join(',\n  ');
            }
            return '';  
        }
        return object;
    }
});
