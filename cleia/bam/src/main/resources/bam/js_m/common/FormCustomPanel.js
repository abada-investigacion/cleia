/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.bam.js_m.common.FormCustomPanel',{
    extend:'Ext.Container',        
    config:{
        taskId:undefined,
        url:undefined,
        height:undefined
    },
    constructor:function(config){        
        this.callParent(arguments);
        
        this.createIFrameForm(config.url); 
    },    
    createIFrameForm:function(url){        
        this.iframe=Ext.create('App.bam.js_m.common.FormCustomFrame',{url:url});
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
