/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.define('App.bam.js_m.common.FormCustomFrame',{  
    requires:['Ext.XTemplate','Ext.JSON'],
    extend:'Ext.Component',        
    confing:{
        url:undefined,
        height:undefined
    },    
    constructor:function(config){  

        var renderTpl='<iframe ';        
        renderTpl+='name=\"'+this.getName()+'\" ';
        renderTpl+='src=\"'+config.url+'\" ';
        //renderTpl+='frameborder=\"0\" style=\"width:100%;height:'+this.height+'\"';
        renderTpl+='frameborder=\"0\" style=\"width:100%;height:100%\"';
        renderTpl+='onload=\"'+this.getCallBackFunctionName()+'()\";';
        renderTpl+='</iframe>';                
        
        this.callParent(arguments); 
        
        this.setHtml(renderTpl);
        
        this.addListener('painted',this.insertCallBackScript,this);
        this.addListener('destroy',this.removeCallBackScript,this);
        
        this.addEvents('success','failure');

        this.counter=0;
    },
    onSubmit:function(){
        if (this.counter>0){
            var response=this.getResponse();
            if (response.success){
                if (response.errors && response.errors.reason)
                    this.fireEvent('success',this,response.errors.reason);
                else
                    this.fireEvent('success','');
            }
            else{
                if (response.errors && response.errors.reason)
                    this.fireEvent('failure',this,response.errors.reason);
                else
                    this.fireEvent('failure','');
            }
        }
        this.counter++;
    },
    getResponse:function(){     
        var jsonResponse=this.bodyElement.dom.firstChild.firstChild.contentDocument.body.textContent;
        var obResponse=Ext.JSON.decode(jsonResponse,true);
        if (obResponse && obResponse!=null){            
            return obResponse;
        }
        return undefined;
    },
    getName:function(){
        return this.getId();
    },
    getCallBackFunctionName:function(){
        return this.getName().toString().replace('-','').replace('-','')+"CallBack";
    },
    getScriptContent:function(){
        return 'function '+this.getCallBackFunctionName()+'(){Ext.getCmp(\''+this.getName()+'\').onSubmit();}';
    },
    findScript:function(head){
        var i;
        for (i=0;i<head.childNodes.length;i++){            
            if (head.childNodes[i].type && head.childNodes[i].type=='text/javascript'){
                if (head.childNodes[i].innerHTML==this.getScriptContent())
                    return head.childNodes[i];
            }
        }
        return undefined;
    },
    insertCallBackScript:function(){        
        var headID=document.getElementsByTagName('head')[0];
        if (headID){
            var newScript=this.findScript(headID);
            
            if (!newScript){
                //script callback de datos
                var newScript=document.createElement('script');
                newScript.type='text/javascript';
                newScript.text=this.getScriptContent();
                        
                var scriptEl=Ext.get(newScript);
            
                var headIDExt=Ext.get(headID);
            
                headIDExt.appendChild(scriptEl);
            }
        }
    },
    removeCallBackScript:function(){
        var headID=document.getElementsByTagName('head')[0];
        if (headID){
            var newScript=this.findScript(headID);
            
            if (newScript){

                var scriptEl=Ext.get(newScript);
            
                scriptEl.destroy();
            }
        }
    }
});
