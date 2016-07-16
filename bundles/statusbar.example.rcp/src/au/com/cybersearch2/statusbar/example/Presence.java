/**
    Copyright (C) 2015  www.cybersearch2.com.au

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

/**
 * Presence
 * @author Andrew Bowley
 * 22 Oct 2015
 */
public enum Presence implements DisplayText
{
    online,    // "Free to chat" or "Available"
    away,      // "Away" or "Extended Away"
    dnd,       // Do not disturb
    offline,  // Unavailable
    deleted ; // Deleted from roster (but still displayed in Contacts view)

    private String displayText;
    
    static
    {
        online.displayText = "Online";
        away.displayText = "Away";
        dnd.displayText = "Do not disturb";
        offline.displayText = "Offline";
        deleted.displayText = "";
    }
    
    /**
     * Returns flag set true if this Presence indicates contact is available
     * @return boolean
     */
    public boolean isAvailable()
    {
        return ordinal() < 2;
    }

    @Override
    public String getDisplayText()
    {
        return displayText;
    }
}
