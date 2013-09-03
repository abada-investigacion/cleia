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

Ext.define('App.patient.js.common.detailsPanel', {
    extend:'Ext.panel.Panel',
    config:{
        record:undefined
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
    
    var html,data=config.record.data;
    
    html='<b>Nombre y Apellidos: </b>'+ data.name +' '+data.surname + ' '+ data.surname1 +'<br/><br/>'+
    '<b>Fecha de Nacimiento: </b>'+ data.birthDay.getDate()+'-'+(data.birthDay.getMonth() + 1)+'-'+data.birthDay.getFullYear() +'<br/><br/>'+
    '<b>Sexo: </b>'+getGenre(data.genre) +'<br/><br/>'+
    '<b>Telefono: </b>'+data.tlf +'<br/><br/>'+
    '<b>Direccion: </b>'+data.address +'<br/><br/>'+
    '<b>Ciudad: </b>'+ data.city +'<br/><br/>'+
    '<b>Codigo Postal: </b>' + data.cp +'<br/><br/>'+
    '<b>Pais: </b>' + data.country +'<br/><br/>' +
    '<b>Habilitado: </b>'+ getEnabled(data.enabled)+ '<br/><br/>' +
    '<b>Servicios Asignados: <b><br/>'+getGroups(data.groups)+'<br/>'+
    '<b>Identificadores: </b><br/>'+getIdentifiers(data.ids)+'<br/>'



    return html;
    
}

function getGenre(genre){
    
    if(genre=='MALE'){
        return 'Hombre';
    }else if(genre=='FEMALE') {
        return 'Mujer';
    }else{
        return 'Indefinido';
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

function getEnabled(enabled){
    
    if(enabled==true){
        return 'SI';
    }else{
        return 'NO';
    }
}


 
