/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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