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
Ext.define('App.bam.js.common.JumpNodeGrid', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'],
    extend:'Abada.grid.Panel',
    forceFit:true,    
    columns:[
    {
        header:'bam.jumpgrid.source.node',
        dataIndex:'nodeFrom'
    },

    {
        header:'bam.jumpgrid.source.process',
        dataIndex:'processIdFrom'
    },

    {
        header:'bam.jumpgrid.source.id',
        dataIndex:'nodeIdFrom',
        hidden:true
    },

    {
        header:'bam.jumpgrid.target.node',
        dataIndex:'nodeTo'
    },

    {
        header:'bam.jumpgrid.target.process',
        dataIndex:'processIdTo'
    },

    {
        header:'bam.jumpgrid.target.id',
        dataIndex:'nodeIdTo',
        hidden:true
    },                
    ],
    constructor:function(config){       
        this.initConfig(config);  
       
        this.store=Ext.create('Ext.data.Store',{
            fields:[
            {
                name:'nodeIdTo',
                type:'string'
            },

            {
                name:'nodeTo',
                type:'string'
            },

            {
                name:'processIdTo',
                type:'string'
            },

            {
                name:'nodeIdFrom',
                type:'string'
            },

            {
                name:'nodeFrom',
                type:'string'
            },

            {
                name:'processIdFrom',
                type:'string'
            }                
            ],
            data:[]
        });
       
        this.selModel= Ext.create('Ext.selection.RowModel');
                                           
        this.callParent([config]);                              
    },
    addData:function(data){
        var contains=false;
        for (var i=0;i<this.store.count();i++){
            if (//this.store.getAt(i).get('nodeIdTo')==data.nodeIdTo
                this.store.getAt(i).get('nodeIdFrom')==data.nodeIdFrom
                //&& this.store.getAt(i).get('processIdTo')==data.processIdTo
                && this.store.getAt(i).get('processIdFrom')==data.processIdFrom
                ){
                contains=true;
            }   
        }
        if (!contains)
            this.store.add(data);
    },
    removeSelectedData:function(){
        this.store.remove(this.selModel.getSelection());
    },
    modifySelectedData:function(nodeTo,nodeIdTo,processIdTo){
        if (this.selModel.getCount()==1){
            this.selModel.getSelection()[0].set('nodeTo',nodeTo);
            this.selModel.getSelection()[0].set('nodeIdTo',nodeIdTo);
            this.selModel.getSelection()[0].set('processIdTo',processIdTo);
            return true;
        }
        return false;
    }
});


