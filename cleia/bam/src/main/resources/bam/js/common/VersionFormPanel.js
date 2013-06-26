/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.bam.js.common.VersionFormPanel',{
    requires: ['Abada.Ajax'],
    extend:'Ext.panel.Panel',        
    config:{
        urlOncoguides:getRelativeURI('/bam/oncoguides.do'),
        urlSubmit:undefined,
        processInstanceId:undefined,
        urlImage:getRelativeURI('/bam/process/image.do'),
        urlInfo:getRelativeURI('/bam/process/tree.do'),
        urlDiagramInfo:getRelativeURI('/bam/process/diagram.do'),
        urlActiveNodeInfo:getRelativeURI('/bam/instance/activeNodes.do'),
        urlChangeVersion:getRelativeURI('/bam/instance/version.do')
    },
    initComponent:function(){    
        this.cbOncoguide=Ext.create('Abada.form.field.SimpleGroupingComboBox',{
            url:this.config.urlOncoguides
        }); 
        
        this.chFix=Ext.create('Ext.form.field.Checkbox',{
            boxLabel:'Arreglar Proceso',
            checked:false
        });
        
        var button=Ext.create('Ext.button.Button',{ 
            icon:getRelativeURI('bam/image/version.png'),
            text:'Cambiar version'           
        });
        button.addListener('click',this.onClickChangeVersion,this);
                        
        this.grid=Ext.create('App.bam.js.common.JumpNodeGrid',{
            autoScroll:true,
            height:400,
            tbar:Ext.create('Ext.toolbar.Toolbar',{
                items:[Ext.create('Ext.button.Button', {
                    text: 'Conf.Destino.Nodo',
                    scope:this,
                    icon:getRelativeURI('images/custom/add.gif'),
                    handler: function() {
                        this.onNodeSourceSelected();
                    }
                }),Ext.create('Ext.button.Button', {
                    text: 'Quitar.Nodo',
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
            text:'Oncoguia Actual'
        });
        button1.addListener('click',this.onClickButtonState,this);

        this.items= [this.cbOncoguide,this.chFix,button1,this.grid,button];
        this.callParent();
        
        this.addEvents('success');
    },   
    loadActiveNodes:function(){
        Abada.Ajax.requestJsonData({            
            url:this.config.urlActiveNodeInfo,
            method:'GET',
            scope:this,
            params:{
                processInstanceId:this.processInstanceId                
            },
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
        
        var win=Ext.Msg.prompt('Cuidado','&iquest;Esta seguro de querer realizar los cambios al proceso?\n'+
            'Este hecho puede causar problemas en la ejecuci&oacute;n del proceso.\n'+
            'S&iacute; Comentario o No',function(btn,text){
                var aux=text.substr(0,2).toLowerCase();
                if (aux=='sí' || aux == 'si'){
                    Abada.Ajax.requestJson({
                        url:this.config.urlChangeVersion,
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
                                title:'Error',
                                msg: 'Ha ocurrido un error inesperado. Contacte con el servicio tecnico '+error.reason,
                                icon: Ext.Msg.ERROR                             
                            });
                        }
                    });
                }
            },this,100);
    },
    onClickButtonState:function(btn){                
        var imageOncoguide=Ext.create('App.bam.js.common.ProcessInstanceTabPanel',{
            height:App.height
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
                    title: 'Seleccione Nodo '+this.cbOncoguide.getValue(),
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
