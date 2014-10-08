/**
 * RAMPART - Robust Automatic MultiPle AssembleR Toolkit
 * Copyright (C) 2013  Daniel Mapleson - TGAC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **/
package uk.ac.tgac.conan.core.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Some Xml parsing helper routines.  Assumes elements do not contain subelements and attributes of the same name.
 * This doesn't pretend to be fast so don't use this for big files.
 * User: maplesod
 * Date: 14/08/13
 * Time: 14:52
 */
public class XmlHelper {

    /**
     * Retrieves the first element, within the supplied element that has the specified tagName.
     * @param ele Parent element to search through
     * @param tagName Element name to find
     * @return Element within the parent element that has the specfied name, or null.
     */
    public static Element getDistinctElementByName(Element ele, String tagName) {
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            return (Element)nl.item(0);
        }

        return null;
    }

    /**
     * I take a xml element and the tag name, look for the tag and get
     * the text content
     * i.e for <employee><name>John</name></employee> xml snippet if
     * the Element points to employee node and tagName is 'name' I will return John
     */
    public static String getTextValue(Element ele, String tagName) {

        // Look for elements first
        NodeList nl = ele.getElementsByTagName(tagName);
        if(nl != null && nl.getLength() > 0) {
            Element el = (Element)nl.item(0);
            return el.getFirstChild().getNodeValue();
        }

        // If there are no elements look for an attribute
        return ele.getAttribute(tagName);
    }


    /**
     * Calls getTextValue and returns a int value
     */
    public static int getIntValue(Element ele, String tagName) {
        return Integer.parseInt(getTextValue(ele,tagName));
    }

    /**
     * Calls getTextValue and returns a double value
     */
    public static double getDoubleValue(Element ele, String tagName) {
        return Double.parseDouble(getTextValue(ele,tagName));
    }

    /**
     * Calls getTextValue and returns a boolean value
     */
    public static boolean getBooleanValue(Element ele, String tagName) {
        return Boolean.parseBoolean(getTextValue(ele,tagName));
    }

    public static boolean validate(Element ele, String[] validContents) {

        // Return true if there is nothing to check
        if (ele.getAttributes().getLength() == 0 && ele.getChildNodes().getLength() == 0) {
            return true;
        }

        for(int i = 0; i < ele.getAttributes().getLength(); i++) {
            Node n = ele.getAttributes().item(i);

            for(String c : validContents) {
                 if (n.getNodeName().equalsIgnoreCase(c)) {
                     return true;
                 }
            }
        }

        for(int i = 0; i < ele.getChildNodes().getLength(); i++) {
            Node n = ele.getChildNodes().item(i);

            for(String c : validContents) {
                if (n.getNodeName().equalsIgnoreCase(c)) {
                    return true;
                }
            }
        }

        return false;
    }
}
