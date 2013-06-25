/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.define('Oggi.bam.js.common.JumpNodeGrid', {
    requires: ['Abada.data.JsonStore','Ext.toolbar.Paging','Abada.data.JsonStore'],
    extend:'Ext.grid.Panel',
    forceFit:true,    
    columns:[
    {
        header:'Origen.Nodo',
        dataIndex:'nodeFrom'
    },

    {
        header:'Origen.Proceso',
        dataIndex:'processIdFrom'
    },

    {
        header:'Origen.Nodo.Id',
        dataIndex:'nodeIdFrom',
        hidden:true
    },

    {
        header:'Destino.Nodo',
        dataIndex:'nodeTo'
    },

    {
        header:'Destino.Proceso',
        dataIndex:'processIdTo'
    },

    {
        header:'Destino.Nodo.Id',
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


