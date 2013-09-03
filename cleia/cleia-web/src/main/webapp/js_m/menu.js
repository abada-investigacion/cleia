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

Ext.require(['Abada.menu.MainMenu']);

Ext.setup({
    viewport: {
        fullscreen: true
    },
    onReady: function() {
        Abada.i18n.Bundle.bundle.on('loaded', function() {
            principal();
        });
        Abada.i18n.Bundle.bundle.on('error', function() {
            Abada.i18n.Bundle.bundle.language=Abada.i18n.Bundle.bundle.defaultLanguage;
            Abada.i18n.Bundle.bundle.load();
        });
        Abada.i18n.Bundle.bundle.load();

        function principal() {
            var menu = Ext.create('Abada.menu.MainMenu', {
                url: getRelativeURI('mainmenu.do'),
                title: Abada.i18n.Bundle.bundle.getMsg('menu.title')
            });

            Ext.Viewport.add(menu);
        }
    }});

