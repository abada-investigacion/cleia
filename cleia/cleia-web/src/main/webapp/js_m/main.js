/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.setup({
    onReady: function() {
        var menu = Ext.create('Abada.menu.MainMenu', {
            url: getRelativeURI('mainmenu.do'),
            autoLoadData: true
        });
        
        Ext.Viewport.add(menu);
    }});

