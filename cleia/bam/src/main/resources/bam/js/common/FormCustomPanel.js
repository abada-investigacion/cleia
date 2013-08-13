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
