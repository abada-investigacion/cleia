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

Ext.define('App.bam.js.common.JumpPanel',{
    requires: ['Abada.Ajax','Ext.button.Button','Ext.toolbar.Toolbar','Ext.window.MessageBox','App.bam.js.common.JumpNodeGrid'],
    extend:'Ext.panel.Panel',
    config:{
       i18n:undefined
    },
    initComponent:function(){                                       
        var toolbar=Ext.create('Ext.toolbar.Toolbar',{
            items:[Ext.create('Ext.button.Button', {
                text: this.i18n.getMsg('bam.jumppanel.add'),
                scope:this,
                icon:getRelativeURI('images/custom/add.gif'),
                handler: function() {
                    //Asistente
                    this.startAdding();
                }
            }),Ext.create('Ext.button.Button', {
                text: this.i18n.getMsg('bam.jumppanel.remove'),
                scope:this,
                icon:getRelativeURI('images/custom/delete.gif'),
                handler: function() {
                    this.deleteNode();
                }
            })]
        });
        
        this.grid=Ext.create('App.bam.js.common.JumpNodeGrid',{
            height: this.height-100,
            width: this.width-5,
            autoScroll:true,
            i18n:this.i18n
        });  
        
        var jumper=Ext.create('Ext.button.Button', {
            text: this.i18n.getMsg('bam.jumppanel.jump'),
            scope:this,
            handler: function() {
                this.fireEvent('jump',this,this.grid.store);
            }
        });
        
        this.items=[toolbar,this.grid,jumper];
        this.callParent();   
        
        this.addEvents('jump');
    },
    startAdding:function(){
        this.step=0;
        this.sourceNode=undefined;           
        this.destinationNode=undefined;
        this.setTitle(this.i18n.getMsg('bam.jumppanel.text1'));
    },
    nextNode:function(node){
        if (node){
            this.step++;
            if (this.step==1){
                this.sourceNode=node;
                this.setTitle(this.i18n.getMsg('bam.jumppanel.text2'));
            }else if(this.step==2){
                this.destinationNode=node;
                this.addToGrid(this.sourceNode,this.destinationNode);
                this.setTitle(undefined);
            }
        }
    },
    deleteNode:function(){
        this.grid.removeSelectedData();
    },
    addToGrid:function(sourceNode,destinationNode){
        this.grid.addData({
            nodeIdTo:destinationNode.nodeId,
            processIdTo:destinationNode.processId,
            nodeTo:destinationNode.nodeName,
            nodeIdFrom:sourceNode.nodeId,
            processIdFrom:sourceNode.processId,
            nodeFrom:sourceNode.nodeName
        });
        this.doLayout();
    }
});

