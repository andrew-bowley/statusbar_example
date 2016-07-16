/**
    Copyright (C) 2016  www.cybersearch2.com.au

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/> */
package au.com.cybersearch2.statusbar.example;

import org.eclipse.swt.graphics.Image;

import au.com.cybersearch2.controls.ImageFactory;
import au.com.cybersearch2.controls.ImageFactory.TypeMappingFactory;

/**
 * PresenceImageFactory
 * @author Andrew Bowley
 * 6 Jul 2016
 */
public class PresenceImageFactory implements TypeMappingFactory<Presence>
{
    @Override
    public Image getMappedImage(ImageFactory imageFactory,
            Object object)
    {
        Presence presence = (Presence)object;
        String imagePath = "";
        switch (presence)
        {
        case online: imagePath = "icons/online.gif"; break;
        case away: imagePath = "icons/away.gif"; break;
        case dnd: imagePath = "icons/dnd.gif"; break;
        case offline: imagePath = "icons/offline.gif"; break;
        case deleted: imagePath = "icons/deleted.gif"; break;
        }
        return imageFactory.getImage(imagePath);
    }

    @Override
    public Class<Presence> getFactoryClass()
    {
        return Presence.class;
    }
}
