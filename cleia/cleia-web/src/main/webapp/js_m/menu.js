/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

Ext.require(['Abada.menu.MainMenu']);

Ext.setup({
    viewport: {
        fullscreen: true
    },
    onReady: function() {

        var menu = Ext.create('Abada.menu.MainMenu', {
            url: getRelativeURI('mainmenu.do'),
            title:'Men&uacute;'
        });
        
        Ext.Viewport.add(menu);

    }});

