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
