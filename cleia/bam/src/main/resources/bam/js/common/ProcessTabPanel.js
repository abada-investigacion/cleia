/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.bam.js.common.ProcessTabPanel',{
    requires: ['Abada.Ajax','Ext.window.MessageBox'],
    extend:'Ext.tab.Panel',        
    config:{
        urlImage:undefined,
        urlInfo:undefined,
        urlDiagramInfo:undefined        
    },
    initComponent:function(){        
        this.callParent();

        this.addEvents('click','nodeselected');                
    },
    loadProcessPanels:function(processId){
        this.processId=processId;
        this.removeAll();
        Abada.Ajax.requestJsonObject({
            url: getRelativeServerURI(this.urlInfo,[processId]),
            scope:this,
            method:'GET',
            success: function(json) {                                                       
                this.addProcessInstancePanel(json,0);                                    
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
            urlImageP:this.urlImage,
            urlDiagramInfo:this.urlDiagramInfo,
            autoScroll:true,
            processId:records[i],
            autoLoad:false
        });
        panel.addListener('click',this.onImageClick,this);
        panel.addListener('nodeselected',this.onNodeSelected,this);
        panel.addListener('diagramloadsuccess',handle,this);
        panel.addListener('diagramloadfailure',handle,this);        
        panel.loadDiagramInfo();
    },
    onImageClick:function(cmp,x,y){
        this.fireEvent('click',this,cmp.processId,x,y);                
    },
    onNodeSelected:function(cmp,processId,nodeName, nodeId,x,y,rX,rY){
        this.fireEvent('nodeselected',this,processId,nodeName,nodeId,x,y,rX,rY);    
    }
});
