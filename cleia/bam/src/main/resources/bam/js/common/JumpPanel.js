/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('Oggi.bam.js.common.JumpPanel',{
    requires: ['Abada.Ajax','Ext.button.Button','Ext.toolbar.Toolbar','Ext.window.MessageBox','Oggi.bam.js.common.JumpNodeGrid'],
    extend:'Ext.panel.Panel',
    initComponent:function(){                                       
        var toolbar=Ext.create('Ext.toolbar.Toolbar',{
            items:[Ext.create('Ext.button.Button', {
                text: 'A&ntilde;adir Nodo',
                scope:this,
                icon:getRelativeURI('images/custom/add.gif'),
                handler: function() {
                    //Asistente
                    this.startAdding();
                }
            }),Ext.create('Ext.button.Button', {
                text: 'Quitar Nodo',
                scope:this,
                icon:getRelativeURI('images/custom/delete.gif'),
                handler: function() {
                    this.deleteNode();
                }
            })]
        });
        
        this.grid=Ext.create('Oggi.bam.js.common.JumpNodeGrid',{
            height: this.height-100,
            width: this.width-5,
            autoScroll:true
        });  
        
        var jumper=Ext.create('Ext.button.Button', {
            text: 'Saltar',
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
        this.setTitle('Seleccione nodo origen.');
    },
    nextNode:function(node){
        if (node){
            this.step++;
            if (this.step==1){
                this.sourceNode=node;
                this.setTitle('Seleccione nodo destino.');
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

