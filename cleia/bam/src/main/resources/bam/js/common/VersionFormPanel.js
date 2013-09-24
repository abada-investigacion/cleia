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

Ext.define('App.bam.js.common.VersionFormPanel',{
    requires: ['Abada.Ajax'],
    extend:'Ext.panel.Panel',        
    config:{
        urlOncoguides:getRelativeServerURI('/rs/process/definition/list/combo'),
        urlSubmit:undefined,
        processInstanceId:undefined,
        urlImage:'/rs/process/definition/{0}/image',
        urlInfo:'rs/process/definition/{0}/tree',
        urlDiagramInfo:'rs/process/definition/{0}/diagram',
        urlActiveNodeInfo:'rs/process/instance/{0}/allActiveNodeInfo',
        urlChangeVersion:'rs/process/instance/{0}/{1}/version',
        i18n:undefined
    },
    initComponent:function(){    
        this.cbOncoguide=Ext.create('Abada.form.field.SimpleGroupingComboBox',{
            url:this.config.urlOncoguides
        }); 
        
        this.chFix=Ext.create('Ext.form.field.Checkbox',{
            boxLabel:this.i18n.getMsg('bam.version.fix'),
            checked:false
        });
        
        var button=Ext.create('Ext.button.Button',{ 
            icon:getRelativeURI('bam/image/version.png'),
            text:this.i18n.getMsg('bam.version.change')
        });
        button.addListener('click',this.onClickChangeVersion,this);
                        
        this.grid=Ext.create('App.bam.js.common.JumpNodeGrid',{
            autoScroll:true,
            height:400,
            i18n:this.i18n,
            tbar:Ext.create('Ext.toolbar.Toolbar',{
                items:[Ext.create('Ext.button.Button', {
                    text: this.i18n.getMsg('bam.version.toolbar.button1'),
                    scope:this,
                    icon:getRelativeURI('images/custom/add.gif'),
                    handler: function() {
                        this.onNodeSourceSelected();
                    }
                }),Ext.create('Ext.button.Button', {
                    text: this.i18n.getMsg('bam.version.toolbar.button2'),
                    scope:this,
                    icon:getRelativeURI('images/custom/delete.gif'),
                    handler: function() {
                        //TODO preguntar si se quita
                        this.deleteNode();
                    }
                })]
            })
        });
        
        this.loadActiveNodes();        
        
        var button1=Ext.create('Ext.button.Button',{            
            text:this.i18n.getMsg('bam.version.button1.text')
        });
        button1.addListener('click',this.onClickButtonState,this);

        this.items= [this.cbOncoguide,this.chFix,button1,button,this.grid];
        this.callParent();
        
        this.addEvents('success');
    },   
    loadActiveNodes:function(){
        Abada.Ajax.requestJsonData({            
            url:getRelativeServerURI(this.config.urlActiveNodeInfo,[this.processInstanceId]),
            method:'GET',
            scope:this,
            success:function(records){
                var aux;
                for (var i=0;i<records.total;i++){
                    aux=records.data[i];
                    this.grid.addData({
                        nodeFrom:aux.activeNode.name,
                        nodeIdFrom:aux.activeNode.id,
                        processIdFrom:aux.activeNode.processId
                    });
                }
            }
        });
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
    },
    onClickChangeVersion:function(){
        function createInfo(store,processInstanceId,observation,processId,fixProcess){
            var result={};
            result.observation=observation;
            result.processInstanceId=processInstanceId;
            result.processId=processId;
            result.nodes=[];
            result.fixProcess=fixProcess
            var aux;
            for (var i=0;i<store.count();i++){
                aux=store.getAt(i);
                result.nodes.push({
                    toNode:{
                        id:aux.get('nodeIdTo'),
                        processId:aux.get('processIdTo')
                    },
                    fromNode:{
                        id:aux.get('nodeIdFrom'),
                        processId:aux.get('processIdFrom')
                    }
                });
            }
            return Ext.JSON.encode(result);
        }
        
        var win=Ext.Msg.prompt(this.i18n.getMsg('bam.warning'),this.i18n.getMsg('bam.version.alert1.text'),function(btn,text){
                var aux=text.substr(0,2).toLowerCase();
                if (aux==this.i18n.getMsg('bam.signal.response1') || aux == this.i18n.getMsg('bam.signal.response2')){
                    Abada.Ajax.requestJson({
                        url:getRelativeServerURI(this.config.urlChangeVersion,[this.processInstanceId,this.cbOncoguide.getValue()]),
                        method:'POST',
                        scope:this,
                        headers:{
                            'Content-Type':'application/json'
                        },
                        params:createInfo(this.grid.store,this.processInstanceId,text.substring(2),this.cbOncoguide.getValue(),this.chFix.getValue()),
                        success:function(records){                            
                            win.close();     
                            this.fireEvent('success',this);
                        },
                        failure:function(error){
                            Ext.Msg.show({
                                title:this.i18n.getMsg('bam.error'),
                                msg: this.i18n.getMsg('bam.tabinstance.error1.text',error.reason),
                                icon: Ext.Msg.ERROR                             
                            });
                        }
                    });
                }
            },this,100);
    },
    onClickButtonState:function(btn){                
        var imageOncoguide=Ext.create('App.bam.js.common.ProcessInstanceTabPanel',{
            height:App.height,
            i18n:this.i18n,
            mode:'version'
        });
                
        imageOncoguide.loadProcessInstancePanels(this.processInstanceId);        
        
        var win=Ext.create('Ext.window.Window', {            
            height: 600,
            width: 600,
            autoScroll:true,
            layout: 'fit',
            modal:true,
            items: [imageOncoguide]
        });        
        
        win.show();
    },
    onNodeSourceSelected:function(btn){
        if (this.grid.selModel.getLastSelected()){
            function onNodeSelected(parentPanel,processId,nodeName,nodeId){
                if (this.grid.modifySelectedData(nodeName,nodeId,processId)){
                    win.close();
                }
            }
            
            if (this.cbOncoguide.getValue()){
                var panelAux=Ext.create('App.bam.js.common.ProcessTabPanel',{
                    urlImage:this.config.urlImage,
                    urlInfo:this.config.urlInfo,
                    urlDiagramInfo:this.config.urlDiagramInfo
                });
            
                panelAux.addListener('nodeselected',onNodeSelected,this);
                panelAux.loadProcessPanels(this.cbOncoguide.getValue());
            
                var win=Ext.create('Ext.window.Window', {
                    title: this.i18n.getMsg('bam.version.node.title',this.cbOncoguide.getValue()),
                    height: 600,
                    width: 900,
                    autoScroll:true,
                    layout: 'fit',
                    modal:true,
                    items: [panelAux]
                });
                win.addListener('destroy',function(){
                    panelAux.removeListener('nodeselected',onNodeSelected,this);
                },this);
                
                win.show();
            }
        }
    }
});
