/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.bam.js.common.ProcessInstanceTabPanel',{
    requires: ['Abada.Ajax','Ext.window.MessageBox','Ext.JSON'],
    extend:'Ext.tab.Panel',        
    config:{
        urlImagePI:'rs/process/instance/{0}/image',
        urlImageP:'rs/process/definition/{0}/image',
        urlInfo:'rs/process/instance/{0}/tree',
        urlDiagramInfo:'rs/process/definition/{0}/diagram',
        urlDocumentsNode:getRelativeURI('/bam/dms/documentsnode.do'),
        urlDocumentsNodeProcessInstance:getRelativeURI('/bam/dms/process/instance/documentsnode.do'),
        urlJumpInTime:'rs/process/instance/{0}/jump'
    },
    initComponent:function(){        
        this.callParent();

        this.addEvents('click','nodeselected');
        
        this.imgTooltip=Ext.create('Ext.tip.ToolTip',{
            target:this.getId(),
            dismissDelay:0,
            trackMouse:true            
        });
        this.imgTpl=new Ext.XTemplate('<b>Documentos Generales</b><br />'+
            '<tpl for="doc.data">'+                        
            '<a href="'+getRelativeURI('/bam/dms/file/read.do')+'?uuid={uuid}&fileName={fileName}" >{fileName}</a><br />'+
            '</tpl>'+
            '<b>Informes Paciente</b><br />'+
            '<tpl for="patient.data">'+
            '<a href="'+getRelativeURI('/bam/dms/file/read.do')+'?uuid={uuid}&fileName={fileName}" >{fileName}</a><br />'+            
            '</tpl>');
        
        var button=Ext.create('Ext.button.Button',{
            icon:getRelativeURI('bam/image/cambio.gif'),
            width : 130,
            text:'Cambiar estado'
        });
                             
        this.addDocked(Ext.create('Ext.toolbar.Toolbar', {
            dock:'bottom',
            items:[
            button
            ]
        }));

        button.addListener('click',function(btn,pressed,cfn){
            function onNodeSelected(parentPanel,processId,nodeName,nodeId){
                panel.nextNode({
                    processId:processId,
                    nodeName:nodeName,
                    nodeId:nodeId
                });
            }
            var panel=Ext.create('App.bam.js.common.JumpPanel',{                
                autoScroll:true,
                height: 300,
                width: 400
            });
            panel.addListener('jump',function(p,store){
                function createInfo(store,processInstanceId,observation){
                    var result={};
                    result.observation=observation;
                    result.processInstanceId=processInstanceId;
                    result.nodes=[];
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
                
                Ext.Msg.prompt('Cuidado','&iquest;Esta seguro de querer realizar los cambios al proceso?\n'+
                    'Este hecho puede causar problemas en la ejecuci&oacute;n del proceso.\n'+
                    'S&iacute; Comentario o No',function(btn,text){
                        var aux=text.substr(0,2).toLowerCase();
                        if (aux=='sí' || aux == 'si'){
                            Abada.Ajax.requestJson({
                                url:getRelativeServerURI(this.config.urlJumpInTime,[this.processInstanceId]),
                                method:'POST',
                                scope:this,
                                headers:{
                                    'Content-Type':'application/json'
                                },
                                params:createInfo(store,this.processInstanceId,text.substring(2)),
                                success:function(records){
                                    this.loadProcessInstancePanels(this.processInstanceId);
                                    win.close();
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
            },this);
            
            panel.startAdding();
                
            this.addListener('nodeselected',onNodeSelected,this);
            var win=Ext.create('Ext.window.Window', {
                title: 'Saltos',                                               
                resizable:false,
                modal:false,
                items: [panel]
            });
            win.addListener('destroy',function(){
                this.removeListener('nodeselected',onNodeSelected,this);
            },this);
            win.show();
        },this);
    },
    loadProcessInstancePanels:function(processInstanceId){
        this.processInstanceId=processInstanceId;
        this.removeAll();
        Abada.Ajax.requestJsonData({
            url: getRelativeServerURI(this.config.urlInfo,[processInstanceId]),
            scope:this,
            method:'GET',
            success: function(json) {                                                       
                this.addProcessInstancePanel(json.data,0);                                    
                this.doLayout();
            },
            failure:function(){                
            }
        });
    },
    addProcessInstancePanel:function(records,i){
        function handle(cmp){
            this.add(cmp);
            if (i<records.length-1){
                this.addProcessInstancePanel(records,i+1);
            }
        }
        
        var panel=Ext.create('App.bam.js.common.ProcessInstancePanel',{
            urlImagePI:this.config.urlImagePI,
            urlImageP:this.config.urlImageP,
            urlDiagramInfo:this.config.urlDiagramInfo,
            autoScroll:true,
            processInstanceId:records[i].processInstanceId,
            processId:records[i].processId,
            autoLoad:false
        });
        panel.addListener('click',this.onImageClick,this);
        panel.addListener('nodeselected',this.onNodeSelected,this);
        panel.addListener('nodeover',this.onNodeOver,this);
        panel.addListener('diagramloadsuccess',handle,this);
        panel.addListener('diagramloadfailure',handle,this);        
        panel.loadDiagramInfo();
    },
    onImageClick:function(cmp,x,y){
        this.fireEvent('click',this,cmp.processInstanceId,x,y);                
    },
    onNodeOver:function(cmp,processId,nodeName, nodeId,x,y,rX,rY){
        var data={};
        Abada.Ajax.requestJsonData({
            url:this.config.urlDocumentsNode,
            method:'GET',
            scope:this,
            params:{
                processId:processId,
                nodeId:nodeId
            },
            success:function(records){                
                data.doc=records;
                Abada.Ajax.requestJsonData({
                    url:this.config.urlDocumentsNodeProcessInstance,
                    method:'GET',
                    scope:this,
                    params:{
                        processInstanceId:this.processInstanceId
                    },
                    success:function(records){
                        data.patient=records; 
                        if (data.doc || data.patient){
                            this.imgTooltip.update(this.imgTpl.applyTemplate(data));
                            this.imgTooltip.showAt([rX,rY]);
                        }
                    },
                    failure:function(){
                        if (data.doc || data.patient){
                            this.imgTooltip.update(this.imgTpl.applyTemplate(data));
                            this.imgTooltip.showAt([rX,rY]);
                        }
                    }
                });
            },
            failure:function(){
                Abada.Ajax.requestJsonData({
                    url:this.config.urlDocumentsNodeProcessInstance,
                    method:'GET',
                    scope:this,
                    params:{
                        processInstanceId:this.processInstanceId
                    },
                    success:function(records){
                        data.patient=records; 
                        if (data.doc || data.patient){
                            this.imgTooltip.update(this.imgTpl.applyTemplate(data));
                            this.imgTooltip.showAt([rX,rY]);
                        }
                    },
                    failure:function(){
                        if (data.doc || data.patient){
                            this.imgTooltip.update(this.imgTpl.applyTemplate(data));
                            this.imgTooltip.showAt([rX,rY]);
                        }
                    }
                });
            }
        });                
    },
    onNodeSelected:function(cmp,processId,nodeName, nodeId,x,y,rX,rY){
        this.fireEvent('nodeselected',this,processId,nodeName,nodeId,x,y,rX,rY);    
    }
});
