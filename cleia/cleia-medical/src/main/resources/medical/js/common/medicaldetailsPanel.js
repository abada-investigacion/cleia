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

Ext.define('App.medical.js.common.medicaldetailsPanel', {
    extend:'Ext.panel.Panel',
    config:{
        record:undefined,
        i18n:undefined
    },
    title: '',
    margin:'20 10 10 20',
    border:0,
    renderTo: Ext.getBody(),
    constructor:function(config){
        this.initConfig(config); 
        
        this.html=createHtml(config);
        
        this.callParent([config]);
        
    }
});

function createHtml(config){
    
    var html,data=config.record.raw;
    html='<b>'+config.i18n.getMsg('medical.detailsPanel.fullName')+': </b>'+ data.patient.name +' '+data.patient.surname + ' '+ data.patient.surname1 +'<br/><br/>'+
    '<b>'+config.i18n.getMsg('medical.detailsPanel.birthday')+': </b>'+ data.patient.birthDay+'<br/><br/>'+
    '<b>'+config.i18n.getMsg('medical.detailsPanel.genre')+': </b>'+getGenre(data.patient.genre,config.i18n) +'<br/><br/>'+
    '<b>'+config.i18n.getMsg('medical.detailsPanel.phone')+': </b>'+data.patient.tlf +'<br/><br/>'+
    '<b>'+config.i18n.getMsg('medical.detailsPanel.address')+': </b>'+data.patient.address.address +'<br/><br/>'+
    '<b>'+config.i18n.getMsg('medical.detailsPanel.city')+': </b>'+ data.patient.address.city +'<br/><br/>'+
    '<b>'+config.i18n.getMsg('medical.detailsPanel.postalcode')+': </b>' + data.patient.address.cp +'<br/><br/>'+
    '<b>'+config.i18n.getMsg('medical.detailsPanel.country')+': </b>' + data.patient.address.countryAddress +'<br/><br/>' +
    '<b>'+config.i18n.getMsg('medical.detailsPanel.enabled')+': </b>'+ getEnabled(data.patient.user.enabled,config.i18n)+ '<br/><br/>' +
    '<b>'+config.i18n.getMsg('medical.detailsPanel.assignedServices')+': <b><br/>'+getGroups(data.patient.user.groups)+'<br/>'+
    '<b>'+config.i18n.getMsg('medical.detailsPanel.identifiers')+': </b><br/>'+getIdentifiers(data.patient.user.ids)+'<br/>'



    return html;
    
}

function getGenre(genre,i18n){
    
    if(genre=='MALE'){
        return i18n.getMsg('medical.detailsPanel.man');
    }else if(genre=='FEMALE') {
        return i18n.getMsg('medical.detailsPanel.woman');
    }else{
        return i18n.getMsg('medical.detailsPanel.undefined');
    }
}

function getGroups(groups){
    
    if(groups!='' && groups.length>0){
        var medicalsHtml='<ul>';
        
        for(var i=0; i<groups.length;i++){
            
            medicalsHtml=medicalsHtml+'<li>'+groups[i].value +'</li>';
            
        }
       
        medicalsHtml=medicalsHtml+'</ul>'
       
        return medicalsHtml;
        
    }else{
        return '';
    }
    
    
}

function getIdentifiers(ids){
    
    if(ids!='' && ids.length>0){
        var idsHtml='<ul>';
        
        for(var i=0; i<ids.length;i++){
            
            idsHtml=idsHtml+'<li>'+ids[i].type.value.toUpperCase() + ': '+ids[i].value +'</li>';
            
        }
       
        idsHtml=idsHtml+'</ul>'
       
        return idsHtml;
        
    }else{
        return '';
    }
}

function getEnabled(enabled,i18n){
    
    if(enabled==true){
        return i18n.getMsg('medical.detailsPanel.yes');
    }else{
        return i18n.getMsg('medical.detailsPanel.no');
    }
}


 
