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
package uk.ac.tgac.conan.core.data;

import org.w3c.dom.Element;
import uk.ac.tgac.conan.core.util.XmlHelper;

/**
 * User: maplesod
 * Date: 14/08/13
 * Time: 14:40
 */
public class Organism {

    public static final String KEY_ELEM_NAME = "name";
    public static final String KEY_ATTR_PLOIDY = "ploidy";
    public static final String KEY_ATTR_EST_GENOME_SIZE = "est_genome_size";

    private String name;
    private int ploidy;
    private int estGenomeSize;

    public Organism(String name, int ploidy, int estGenomeSize) {
        this.name = name;
        this.ploidy = ploidy;
        this.estGenomeSize = estGenomeSize;
    }

    public Organism(Element ele) {
        this.name = XmlHelper.getTextValue(ele, KEY_ELEM_NAME);
        this.ploidy = XmlHelper.getIntValue(ele, KEY_ATTR_PLOIDY);
        this.estGenomeSize = XmlHelper.getIntValue(ele, KEY_ATTR_EST_GENOME_SIZE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPloidy() {
        return ploidy;
    }

    public void setPloidy(int ploidy) {
        this.ploidy = ploidy;
    }

    public int getEstGenomeSize() {
        return estGenomeSize;
    }

    public void setEstGenomeSize(int estGenomeSize) {
        this.estGenomeSize = estGenomeSize;
    }


}
