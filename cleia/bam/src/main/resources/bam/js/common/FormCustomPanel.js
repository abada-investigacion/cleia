/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.bam.js.common.FormCustomPanel',{
    requires: ['Abada.Ajax'],
    extend:'Ext.panel.Panel',        
    config:{
        taskId:undefined,
        url:undefined,
        height:undefined
    },
    initComponent:function(){        
        this.callParent();
        this.createIFrameForm();
        
        this.addEvents('success','failure');
    },    
    createIFrameForm:function(){
        var urlComplete=this.url;
        this.iframe=Ext.create('App.bam.js.common.FormCustomFrame',{url:urlComplete,height:this.height});
        this.iframe.addListener('success',this.onSuccessSubmitIFrame,this);
        this.iframe.addListener('failure',this.onFailureSubmitIFrame,this);
        this.add(this.iframe);
    },
    onSuccessSubmitIFrame:function(frame,aux){        
        this.fireEvent('success',this,frame,aux);
    },
    onFailureSubmitIFrame:function(frame,text){        
        this.fireEvent('failure',this,frame,text);
    }
});
