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

    public static boolean validate(Element ele, String[] requiredAttributes, String[] optionalAttributes, String[] requiredElements, String[] optionalElements) {

        boolean[] foundAttributes = new boolean[requiredAttributes.length];

        for(int i = 0; i < foundAttributes.length; i++) {
            foundAttributes[i] = false;
        }

        for(int i = 0; i < ele.getAttributes().getLength(); i++) {
            Node n = ele.getAttributes().item(i);

            boolean found = false;

            for(int j = 0; j < requiredAttributes.length; j++) {
                if (requiredAttributes[j].equalsIgnoreCase(n.getNodeName())) {
                    foundAttributes[j] = true;
                    found = true;
                    break;
                }
            }

            if (!found) {
                for (String c : optionalAttributes) {
                    if (n.getNodeName().equalsIgnoreCase(c)) {
                        found = true;
                        break;
                    }
                }
            }

            // If we've got this far and nothings been found then there must have been an unexpected attribute
            if (!found) {
                throw new IllegalArgumentException("Error validating XML Element: " + ele.getNodeName() +
                        "; Found unexpected attribute: " + n.getNodeName());
            }
        }

        // Check all required attributes were found
        for(int i = 0; i < foundAttributes.length; i++) {
            if (foundAttributes[i] == false) {
                throw new IllegalArgumentException("Error validating XML Element: " + ele.getNodeName() +
                        "; Failed to find expected attribute or child element named: " + requiredAttributes[i]);
            }
        }

        boolean[] foundElements = new boolean[requiredElements.length];

        for(int i = 0; i < foundElements.length; i++) {
            foundElements[i] = false;
        }

        for(int i = 0; i < ele.getChildNodes().getLength(); i++) {
            Node n = ele.getChildNodes().item(i);

            boolean found = false;

            for(int j = 0; j < requiredElements.length; j++) {
                if (requiredElements[j].equalsIgnoreCase(n.getNodeName())) {
                    foundElements[j] = true;
                    found = true;
                    break;
                }
            }

            if (!found) {
                for (String c : optionalElements) {
                    if (n.getNodeName().equalsIgnoreCase(c)) {
                        found = true;
                        break;
                    }
                }
            }

            // If we've got this far and nothings been found then there must have been an unexpected element
            if (!found) {

                if (n.getNodeName().startsWith("#") || n.getNodeName().startsWith("<!--")) {
                    // Then we just ignore these elements
                }
                else {
                    throw new IllegalArgumentException("Error validating XML Element: " + ele.getNodeName() +
                            "; Found unexpected child node: " + n.getNodeName());
                }
            }
        }

        // Check all required child elements were found
        for(int i = 0; i < foundElements.length; i++) {
            if (foundElements[i] == false) {
                throw new IllegalArgumentException("Error validating XML Element: " + ele.getNodeName() +
                        "; Failed to find expected attribute or child element named: " + requiredElements[i]);
            }
        }

        return true;
    }
}
