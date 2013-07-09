/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
Ext.require([
    'Abada.Ajax',
    ]);

Ext.onReady(function() {            
    /*Abada.Ajax.request({
        url: Oggi.urlJbpmServer+'rs/identity/sid/invalidate',
        method:'POST',
        success: function(result,request) {          
            window.location='login.htm';
        },
        failure:function(){      
            window.location='login.htm';
        }
    });*/
    window.location='login.htm';
});