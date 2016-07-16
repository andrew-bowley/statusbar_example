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

/**
 * Events
 * Defines application message types for Event Broker service. Must be distinct from system message types.
 * @author Andrew Bowley
 * 3 Jun 2016
 */
public class Events
{
    /** Use package name base for global uniqueness */
    public static final String  TOPIC_BASE = "au/com/cybersearch/example";
    /** User presence: online, away, dnd (do not disturb) and offline */
    public static final String  PRESENCE = TOPIC_BASE + "/presence";
    /** Messaging secure/unsecure */
    public static final String  SECURE = PRESENCE + "/secure";
    /** User message update */
    public static final String  USER_MESSAGE = TOPIC_BASE + "/userMessage";
    public static final String MENU_DEMO = TOPIC_BASE + "/menuDemo";
}
