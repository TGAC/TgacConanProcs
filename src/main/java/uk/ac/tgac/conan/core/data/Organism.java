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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import uk.ac.tgac.conan.core.util.XmlHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: maplesod
 * Date: 14/08/13
 * Time: 14:40
 */
public class Organism {

    private static Logger log = LoggerFactory.getLogger(Organism.class);

    private static final String KEY_ATTR_NAME = "name";
    private static final String KEY_ATTR_PLOIDY = "ploidy";
    private static final String KEY_ATTR_MIN_INTRON_SIZE = "min_intron_size";
    private static final String KEY_ATTR_MAX_INTRON_SIZE = "max_intron_size";
    private static final String KEY_ELEM_REFERENCE = "reference";
    private static final String KEY_ELEM_ESTIMATED = "estimated";

    public static final int DEFAULT_PLOIDY = 1; // Haploid
    public static final int DEFAULT_MIN_INTRON_SIZE = 0;
    public static final int DEFAULT_MAX_INTRON_SIZE = 1000000;
    public static final boolean DEFAULT_COMPACT_GENOME = false;

    private String name;
    private int ploidy;
    private int minIntronSize;
    private int maxIntronSize;
    private boolean compactGenome;
    private Reference reference;
    private Estimated estimated;

    private long genomeSize;

    public Organism() {
        this("Something", DEFAULT_PLOIDY, null, null);
    }

    public Organism(String name, int ploidy, Estimated estimated, Reference reference) {
        this.name = name;
        this.ploidy = ploidy;
        this.minIntronSize = DEFAULT_MIN_INTRON_SIZE;
        this.maxIntronSize = DEFAULT_MAX_INTRON_SIZE;
        this.compactGenome = false;
        this.estimated = estimated;
        this.reference = reference;

        this.genomeSize = 0;
    }

    public Organism(Element ele) {

        // Check there's nothing
        if (!XmlHelper.validate(ele,
                new String[] {
                        KEY_ATTR_NAME
                },
                new String[]{
                        KEY_ATTR_PLOIDY,
                        KEY_ATTR_MIN_INTRON_SIZE,
                        KEY_ATTR_MAX_INTRON_SIZE
                },
                new String[0],
                new String[] {
                        KEY_ELEM_ESTIMATED,
                        KEY_ELEM_REFERENCE
                })) {
            throw new IllegalArgumentException("Found unrecognised element or attribute in Organism");
        }

        // Required
        this.name = XmlHelper.getTextValue(ele, KEY_ATTR_NAME);

        // Optional
        this.ploidy = ele.hasAttribute(KEY_ATTR_PLOIDY) ? XmlHelper.getIntValue(ele, KEY_ATTR_PLOIDY) : DEFAULT_PLOIDY;

        this.minIntronSize = ele.hasAttribute(KEY_ATTR_MIN_INTRON_SIZE) ?
                XmlHelper.getIntValue(ele, KEY_ATTR_MIN_INTRON_SIZE) : DEFAULT_MIN_INTRON_SIZE;

        this.maxIntronSize = ele.hasAttribute(KEY_ATTR_MAX_INTRON_SIZE) ?
                XmlHelper.getIntValue(ele, KEY_ATTR_MAX_INTRON_SIZE) : DEFAULT_MAX_INTRON_SIZE;

        if (ele.hasChildNodes()) {
            Element refEl = XmlHelper.getDistinctElementByName(ele, KEY_ELEM_REFERENCE);
            if (refEl != null) {
                this.reference = new Reference(refEl);
            }

            Element estEl = XmlHelper.getDistinctElementByName(ele, KEY_ELEM_ESTIMATED);
            if (estEl != null) {
                this.estimated = new Estimated(estEl);
            }
        }
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

    public boolean isCompactGenome() {
        return compactGenome;
    }

    public void setCompactGenome(boolean compactGenome) {
        this.compactGenome = compactGenome;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public Estimated getEstimated() {
        return estimated;
    }

    public void setEstimated(Estimated estimated) {
        this.estimated = estimated;
    }

    public boolean isGenomeSizeAvailable() {

        if (this.genomeSize > 0) {
            return true;
        }
        else if (this.reference != null && this.reference.getPath() != null) {
            return true;
        }
        else if (this.estimated != null && this.estimated.getEstGenomeSize() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    protected long getGenomeSizeFromFile(File path) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(path));

        String line = null;
        long baseCount = 0;

        while ((line = reader.readLine()) != null) {

            if (!line.isEmpty()) {
                char firstChar = line.charAt(0);

                // Ignore everything but the sequences
                // While loop handles multi-line sequences
                while (firstChar != '>' && line != null) {
                    // Get the next line (should be the sequence line)
                    baseCount += line.length();
                    line = reader.readLine();
                }
            }
        }

        reader.close();

        return baseCount;
    }

    public long getGenomeSize() throws IOException {

        if (genomeSize > 0) {
            return genomeSize;
        }
        else if (this.reference != null && this.reference.getPath() != null) {

            log.info("Acquiring genome size from fasta file: " + this.reference.getPath());
            this.genomeSize = this.getGenomeSizeFromFile(this.reference.getPath());
            log.info("Genome size is (b): " + this.genomeSize);
            return this.genomeSize;
        }
        else if (this.estimated != null) {
            return this.estimated.getEstGenomeSize();
        }
        else {
            return 0;
        }
    }

    public static class Reference {

        private static final String KEY_ATTR_REF_NAME = "name";
        private static final String KEY_ATTR_REF_PATH = "path";

        private String name;
        private File path;

        public Reference() {
            this.name = "";
            this.path = null;
        }

        public Reference(Element ele) {

            this();

            // Check there's nothing
            if (!XmlHelper.validate(ele,
                    new String[] {
                            KEY_ATTR_REF_NAME,
                            KEY_ATTR_REF_PATH
                    },
                    new String[0],
                    new String[0],
                    new String[0])) {
                throw new IllegalArgumentException("Found unrecognised element or attribute in Organism.Reference");
            }

            // Required
            this.name = XmlHelper.getTextValue(ele, KEY_ATTR_REF_NAME);
            this.path = new File(XmlHelper.getTextValue(ele, KEY_ATTR_REF_PATH));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public File getPath() {
            return path;
        }

        public void setPath(File path) {
            this.path = path;
        }
    }

    public static class Estimated {

        private static final String KEY_ATTR_EST_GENOME_SIZE = "est_genome_size";
        private static final String KEY_ATTR_EST_GC_PERC = "est_gc_percentage";
        private static final String KEY_ATTR_EST_NB_GENES = "est_nb_genes";

        private long estGenomeSize;
        private double estGcPercentage;
        private int estNbGenes;

        public Estimated() {
            this(0, 0.0, 0);
        }

        public Estimated(long estGenomeSize, double estGcPercentage, int estNbGenes) {
            this.estGenomeSize = estGenomeSize;
            this.estGcPercentage = estGcPercentage;
            this.estNbGenes = estNbGenes;
        }

        public Estimated(Element ele) {

            // Check there's nothing
            if (!XmlHelper.validate(ele,
                    new String[0],
                    new String[]{
                            KEY_ATTR_EST_GENOME_SIZE,
                            KEY_ATTR_EST_GC_PERC,
                            KEY_ATTR_EST_NB_GENES
                    },
                    new String[0],
                    new String[0]
            )) {
                throw new IllegalArgumentException("Found unrecognised element or attribute in Organism");
            }

            this.estGenomeSize = ele.hasAttribute(KEY_ATTR_EST_GENOME_SIZE) ?
                    XmlHelper.getIntValue(ele, KEY_ATTR_EST_GENOME_SIZE) : 0;

            this.estGcPercentage = ele.hasAttribute(KEY_ATTR_EST_GC_PERC) ?
                    XmlHelper.getDoubleValue(ele, KEY_ATTR_EST_GC_PERC) : 0.0;

            this.estNbGenes = ele.hasAttribute(KEY_ATTR_EST_NB_GENES) ?
                    XmlHelper.getIntValue(ele, KEY_ATTR_EST_NB_GENES) : 0;
        }


        public long getEstGenomeSize() {
            return estGenomeSize;
        }

        public void setEstGenomeSize(long estGenomeSize) {
            this.estGenomeSize = estGenomeSize;
        }

        public double getEstGcPercentage() {
            return estGcPercentage;
        }

        public void setEstGcPercentage(double estGcPercentage) {
            this.estGcPercentage = estGcPercentage;
        }

        public int getEstNbGenes() {
            return estNbGenes;
        }

        public void setEstNbGenes(int estNbGenes) {
            this.estNbGenes = estNbGenes;
        }
    }
}
