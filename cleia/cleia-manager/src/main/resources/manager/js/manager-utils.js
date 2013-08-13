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

/**
 * Devuelve una lista de objetos en formato JSON para una entidad a partir de 
 * los items seleccionados en un grid
 */
function getListForObject(selection,idName){
        
        var idArray=[];
        var selectedItems=selection.selected.items;
        
        for(var i=0; i<selectedItems.length; i++){
            var str='{'+"\""+idName+"\":"+"\""+selectedItems[i].data[idName]+"\""+'}';
            var obj = JSON.parse(str);
            idArray.push(obj);
        }
       
        return idArray;
    }
    
    /**
     * Devuelve una lista de objetos en formato JSON para una entidad a partir 
     * del store de un grid
     */
    function getListForObjectByGridStore(store,idName){
        
        var idArray=[];
        var items=store.data.items;
        
        for(var i=0; i<items.length; i++){
            var str='{'+"\""+idName+"\":"+"\""+items[i].data[idName]+"\""+'}';
            var obj = JSON.parse(str);
            idArray.push(obj);
        }
       
        return idArray;
    }
    
    
function entityListPreSelected(grid,idEntity,idEntityString,idList,url){
    Abada.Ajax.requestJsonData({
        url:url,
        scope:this,
        method:'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        params:{
            search:Ext.JSON.encode(generateSearchParam(idEntity,idEntityString))
        }
        ,
        failure:function(){            
            
        },
        success:function(object){
            for(var i=0;i<grid.getStore().getCount();i++){
                var record=grid.getStore().getAt(i);
                for(var j=0;j<object.total;j++){
                    if(record.get(idList)==object.data[j][idList])
                        grid.selModel.select(record,true,true);
                }
                   
            }              
              
        }
    });
}

function generateSearchParam (record,name){
    var result=[];
    result.push({
        value:record,
        key:name
    });
    return result;
}