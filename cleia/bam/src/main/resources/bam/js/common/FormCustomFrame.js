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

Ext.define('App.bam.js.common.FormCustomFrame',{  
    requires:['Ext.XTemplate','Ext.JSON'],
    extend:'Ext.Component',        
    confing:{
        url:undefined,
        fieldResponse:undefined,
        height:undefined
    },    
    initComponent:function(){                        
        this.renderTpl='<iframe ';        
        this.renderTpl+='name=\"'+this.getName()+'\" ';
        this.renderTpl+='src=\"'+this.url+'\" ';
        this.renderTpl+='frameborder=\"0\" style=\"width:100%;height:'+this.height+'\"';
        this.renderTpl+='onload=\"'+this.getCallBackFunctionName()+'()\";';
        this.renderTpl+='</iframe>';                
        
        this.callParent(); 
        
        this.addListener('render',this.insertCallBackScript,this);
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
        var jsonResponse=this.container.dom.childNodes[0].childNodes[0].contentDocument.body.textContent;
        var obResponse=Ext.JSON.decode(jsonResponse,true);
        if (obResponse && obResponse!=null){            
            return obResponse;
        }
        return undefined;
    },
    getName:function(){
        return this.id;
    },
    getCallBackFunctionName:function(){
        return this.getName().toString().replace('-','').replace('-','')+"CallBack";
    },
    getScriptContent:function(){
        return 'function '+this.getCallBackFunctionName()+'(){Ext.ComponentManager.get(\''+this.getName()+'\').onSubmit();}';
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
            
                scriptEl.remove();
            }
        }
    }
});
