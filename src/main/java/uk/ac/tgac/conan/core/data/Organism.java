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

    private static final String KEY_ELEM_NAME = "name";
    private static final String KEY_ATTR_PLOIDY = "ploidy";
    private static final String KEY_ATTR_EST_GENOME_SIZE = "est_genome_size";
    private static final String KEY_ATTR_EST_GC_PERC = "est_gc_percentage";
    private static final String KEY_ATTR_MIN_INTRON_SIZE = "min_intron_size";
    private static final String KEY_ATTR_MAX_INTRON_SIZE = "max_intron_size";

    public static final int DEFAULT_PLOIDY = 1; // Haploid
    public static final int DEFAULT_MIN_INTRON_SIZE = 0;
    public static final int DEFAULT_MAX_INTRON_SIZE = 1000000;


    private String name;
    private int ploidy;
    private int estGenomeSize;
    private double estGcPercentage;
    private int minIntronSize;
    private int maxIntronSize;

    public Organism() {
        this("Something", DEFAULT_PLOIDY, 0, 0.0);
    }

    public Organism(String name, int ploidy, int estGenomeSize, double estGcPercentage) {
        this.name = name;
        this.ploidy = ploidy;
        this.estGenomeSize = estGenomeSize;
        this.estGcPercentage = estGcPercentage;
        this.minIntronSize = DEFAULT_MIN_INTRON_SIZE;
        this.maxIntronSize = DEFAULT_MAX_INTRON_SIZE;
    }

    public Organism(Element ele) {

        // Required
        this.name = XmlHelper.getTextValue(ele, KEY_ELEM_NAME);

        // Optional
        this.ploidy = ele.hasAttribute(KEY_ATTR_PLOIDY) ? XmlHelper.getIntValue(ele, KEY_ATTR_PLOIDY) : DEFAULT_PLOIDY;

        this.estGenomeSize = ele.hasAttribute(KEY_ATTR_EST_GENOME_SIZE) ?
                XmlHelper.getIntValue(ele, KEY_ATTR_EST_GENOME_SIZE) : 0;

        this.estGcPercentage = ele.hasAttribute(KEY_ATTR_EST_GC_PERC) ?
                XmlHelper.getDoubleValue(ele, KEY_ATTR_EST_GC_PERC) : 0.0;

        this.minIntronSize = ele.hasAttribute(KEY_ATTR_MIN_INTRON_SIZE) ?
                XmlHelper.getIntValue(ele, KEY_ATTR_MIN_INTRON_SIZE) : DEFAULT_MIN_INTRON_SIZE;

        this.maxIntronSize = ele.hasAttribute(KEY_ATTR_MAX_INTRON_SIZE) ?
                XmlHelper.getIntValue(ele, KEY_ATTR_MAX_INTRON_SIZE) : DEFAULT_MAX_INTRON_SIZE;
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

    public double getEstGcPercentage() {
        return estGcPercentage;
    }

    public void setEstGcPercentage(double estGcPercentage) {
        this.estGcPercentage = estGcPercentage;
    }

    public int getMinIntronSize() {
        return minIntronSize;
    }

    public void setMinIntronSize(int minIntronSize) {
        this.minIntronSize = minIntronSize;
    }

    public int getMaxIntronSize() {
        return maxIntronSize;
    }

    public void setMaxIntronSize(int maxIntronSize) {
        this.maxIntronSize = maxIntronSize;
    }
}
