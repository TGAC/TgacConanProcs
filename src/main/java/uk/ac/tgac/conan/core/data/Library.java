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
import org.w3c.dom.NodeList;
import uk.ac.tgac.conan.core.util.XmlHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Library {

    // **** Sub types ****
	
	public enum SeqOrientation {
		FR,
		RF,
		FF,
		RR;
		
		public static final SeqOrientation FORWARD_REVERSE = FR;
		public static final SeqOrientation REVERSE_FORWARD = RF;
		public static final SeqOrientation FORWARD_FORWARD = FF;
		public static final SeqOrientation REVERSE_REVERSE = RR;
		
	}
	
	public enum Type {
		OPE,
        PE,
		MP,
		SE;

        public static final Type OVERLAPPING_PAIRED_END = OPE;
        public static final Type PAIRED_END = PE;
		public static final Type MATE_PAIR = MP;
		public static final Type SINGLE_END = SE;
	}

    public enum Phred {
        PHRED_33,
        PHRED_64
    }

    public enum Strandedness {
        UNSTRANDED,
        FIRST_STRAND,
        SECOND_STRAND
    }
	

    // **** Xml Key names ****

    private static final String KEY_ELEM_FILES                   = "files";
    private static final String KEY_ELEM_FILE_PATH               = "path";

    private static final String KEY_ATTR_NAME                    = "name";
    private static final String KEY_ATTR_AVG_INSERT_SIZE         = "avg_insert_size";
    private static final String KEY_ATTR_INSERT_ERROR_TOLERANCE  = "insert_err_tolerance";
    private static final String KEY_ATTR_READ_LENGTH             = "read_length";
    private static final String KEY_ATTR_SEQ_ORIENTATION         = "orientation";
    private static final String KEY_ATTR_TYPE                    = "type";
    private static final String KEY_ATTR_PHRED                   = "phred";
    private static final String KEY_ATTR_UNIFORM                 = "uniform";
    private static final String KEY_ATTR_STRANDEDNESS            = "strandedness";


    private static final int    DEFAULT_AVG_INSERT_SIZE          = 500;
    private static final double DEFAULT_INS_ERR_TOLERANCE        = 0.3;
    private static final SeqOrientation DEFAULT_SEQ_ORIENTATION  = SeqOrientation.FR;
    private static final Phred  DEFAULT_PHRED                    = Phred.PHRED_64;
    private static final Strandedness DEFAULT_STRANDEDNESS       = Strandedness.UNSTRANDED;



    // **** Class vars
	
	private String name;
	private int averageInsertSize;
	private double insertErrorTolerance;        // 0-1, 0- intolerant; 1-anything goes
	private int readLength;
	private SeqOrientation seqOrientation;
	private Type type;
    private Phred phred;
	private List<SeqFile> files;
    private boolean uniform;
    private Strandedness strandedness;


    /**
     * Set default values
     */
    public Library() {
        this.name = "lib";
        this.averageInsertSize = DEFAULT_AVG_INSERT_SIZE;
        this.insertErrorTolerance = DEFAULT_INS_ERR_TOLERANCE;
        this.readLength = 101;
        this.seqOrientation = DEFAULT_SEQ_ORIENTATION;
        this.type = Type.PE;
        this.phred = DEFAULT_PHRED;
        this.files = new ArrayList<SeqFile>();
        this.uniform = true;
        this.strandedness = DEFAULT_STRANDEDNESS;
    }

    /**
     * Set from Xml Element
     * @param ele
     */
    public Library(Element ele, File outputDir) {

        // Set defaults
        this();

        // Check there's nothing
        if (!XmlHelper.validate(ele,
                new String[] {
                        KEY_ATTR_NAME,
                        KEY_ATTR_READ_LENGTH,
                        KEY_ATTR_TYPE,
                        KEY_ATTR_PHRED
                },
                new String[]{
                        KEY_ATTR_SEQ_ORIENTATION,
                        KEY_ATTR_AVG_INSERT_SIZE,
                        KEY_ATTR_INSERT_ERROR_TOLERANCE,
                        KEY_ATTR_UNIFORM,
                        KEY_ATTR_STRANDEDNESS
                },
                new String[]{
                        KEY_ELEM_FILES
                },
                new String[0])) {
            throw new IllegalArgumentException("Found unrecognised element or attribute in Library");
        }

        // Required values
        this.name = XmlHelper.getTextValue(ele, KEY_ATTR_NAME);
        this.readLength = XmlHelper.getIntValue(ele, KEY_ATTR_READ_LENGTH);
        this.type = Type.valueOf(XmlHelper.getTextValue(ele, KEY_ATTR_TYPE).toUpperCase());
        this.phred = Phred.valueOf(XmlHelper.getTextValue(ele, KEY_ATTR_PHRED).toUpperCase());

        // Seq orientation is only required if type is not single end
        this.seqOrientation = this.type != Type.SINGLE_END ?
                SeqOrientation.valueOf(XmlHelper.getTextValue(ele, KEY_ATTR_SEQ_ORIENTATION).toUpperCase()) :
                null;

        // Optional
        this.averageInsertSize = ele.hasAttribute(KEY_ATTR_AVG_INSERT_SIZE) ?
                XmlHelper.getIntValue(ele, KEY_ATTR_AVG_INSERT_SIZE) :
                DEFAULT_AVG_INSERT_SIZE;

        this.insertErrorTolerance = ele.hasAttribute(KEY_ATTR_INSERT_ERROR_TOLERANCE) ?
                XmlHelper.getDoubleValue(ele, KEY_ATTR_INSERT_ERROR_TOLERANCE) :
                DEFAULT_INS_ERR_TOLERANCE;

        this.uniform = ele.hasAttribute(KEY_ATTR_UNIFORM) ?
                XmlHelper.getBooleanValue(ele, KEY_ATTR_UNIFORM) :
                true;

        this.strandedness = ele.hasAttribute(KEY_ATTR_STRANDEDNESS) ?
                Strandedness.valueOf(XmlHelper.getTextValue(ele, KEY_ATTR_STRANDEDNESS).toUpperCase()) :
                DEFAULT_STRANDEDNESS;

        // Some files (we test later if there are the correct number)
        Element fileElements = XmlHelper.getDistinctElementByName(ele, KEY_ELEM_FILES);
        NodeList nodes = fileElements.getElementsByTagName(KEY_ELEM_FILE_PATH);
        for(int i = 0; i < nodes.getLength(); i++) {
            String filePath = nodes.item(i).getFirstChild().getNodeValue();

            filePath = filePath.startsWith("/") ? filePath : outputDir.getAbsolutePath() + "/" + filePath;
            this.files.add(new SeqFile(filePath));
        }
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAverageInsertSize() {
		return averageInsertSize;
	}

	public void setAverageInsertSize(int averageInsertSize) {
		this.averageInsertSize = averageInsertSize;
	}

	public double getInsertErrorTolerance() {
		return insertErrorTolerance;
	}

	public void setInsertErrorTolerance(double insertErrorTolerance) {
		this.insertErrorTolerance = insertErrorTolerance;
	}
	
	public int getReadLength() {
		return readLength;
	}

	public void setReadLength(int readLength) {
		this.readLength = readLength;
	}
	
	public SeqOrientation getSeqOrientation() {
		return seqOrientation;
	}

	public void setSeqOrientation(SeqOrientation seqOrientation) {
		this.seqOrientation = seqOrientation;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

    public Phred getPhred() {
        return phred;
    }

    public void setPhred(Phred phred) {
        this.phred = phred;
    }

    public boolean isUniform() {
        return uniform;
    }

    public void setUniform(boolean uniform) {
        this.uniform = uniform;
    }

    public Strandedness getStrandedness() {
        return strandedness;
    }

    public void setStrandedness(Strandedness strandedness) {
        this.strandedness = strandedness;
    }

    public List<SeqFile> getSeqFiles() {
        return files;
    }

    public List<File> getFiles() {

        List<File> fs = new ArrayList<File>();
        for(SeqFile seqFile : this.files) {
            if (seqFile != null) {
                File f = seqFile.getFile();
                if (f != null) {
                    fs.add(f);
                }
            }
        }

        return fs;
    }

    public void setFiles(List<SeqFile> files) {
        this.files = files;
    }

    public File getFile1() {
        return this.files.get(0).getFile();
    }

    public SeqFile.FileType getFile1Type() {
        return this.files.get(0).getFileType();
    }

    public File getFile2() {
        return this.files.get(1).getFile();
    }

    public SeqFile.FileType getFile2Type() {
        return this.files.get(1).getFileType();
    }

    public void setFiles(String file1, String file2) {
        this.setFiles(new File(file1), file2 == null ? null : new File(file2));
    }

    public void setFiles(File file1, File file2) {
        this.files.clear();

        this.files.add(new SeqFile(file1));

        if (file2 != null) {
            this.files.add(new SeqFile(file2));
        }
    }

    public Library copy() {
        Library copy = new Library();

        copy.setName(this.name);
        copy.setAverageInsertSize(this.averageInsertSize);
        copy.setInsertErrorTolerance(this.insertErrorTolerance);
        copy.setReadLength(this.readLength);
        copy.setSeqOrientation(this.seqOrientation);
        copy.setType(this.type);
        copy.setPhred(this.phred);
        copy.setUniform(this.uniform);

        if (this.isPairedEnd()) {
            copy.setFiles(this.getFile1().getAbsolutePath(), this.getFile2().getAbsolutePath());
        }
        else {
            copy.setFiles(this.getFile1().getAbsolutePath(), null);
        }

        return copy;
    }


    public boolean isPairedEnd() {
        return this.type == Type.PE || this.type == Type.MP || this.type == Type.OPE;
    }

}


