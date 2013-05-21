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

import org.ini4j.Profile.Section;
import uk.ac.ebi.fgpt.conan.util.StringJoiner;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema="rampart",name="library")
public class Library implements Serializable {
	
	private static final long serialVersionUID = 9110367505701278888L;

    public boolean testUsage(Usage qualityTrimming) {

        return this.usage.contains(qualityTrimming.name());
    }

    public boolean isPairedEnd() {
        return this.type == Type.PE || this.type == Type.MP;
    }

    public enum Usage {
		
		ASM {
			@Override
			public String toString() {
				return "Assembly";
			}
		},
		SCF {
			@Override
			public String toString() {
				return "Scaffolding";
			}
		},
        GC {
            @Override
            public String toString() {
                return "Gap Closing";
            }
        },
		QT {
			@Override
			public String toString() {
				return "Quality Trimming";
			}
		};
		
		public abstract String toString();

        public static final Usage ASSEMBLING = ASM;
        public static final Usage SCAFFOLDING = SCF;
        public static final Usage GAP_CLOSING = GC;
        public static final Usage QUALITY_TRIMMING = QT;
	}
	
	public enum Dataset {
		RAW,
		QT;
		
		public static final Dataset QUALITY_TRIMMED = QT;
	}
	
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
		PE,
		MP,
		SE;
		
		public static final Type PAIRED_END = PE;
		public static final Type MATE_PAIR = MP;
		public static final Type SINGLE_END = SE;
	}
	
	
	public static final String SECTION_PREFIX = "LIB";
	
	public static final String KEY_NAME = "name";
	public static final String KEY_DATASET = "dataset";
	
	public static final String KEY_AVG_INSERT_SIZE = "avg_insert_size";
	public static final String KEY_INSERT_ERROR_TOLERANCE = "insert_err_tolerance";
	public static final String KEY_READ_LENGTH = "read_length";
	public static final String KEY_SEQ_ORIENTATION = "seq_orientation";
	public static final String KEY_USAGE = "usage";
	public static final String KEY_ORDER = "order";
    public static final String KEY_TYPE = "type";

    public static final String KEY_FILE_TYPE = "file_type";
	public static final String KEY_FILE_1 = "file_paired_1";
	public static final String KEY_FILE_2 = "file_paired_2";
	public static final String KEY_FILE_SE = "file_single_end";
	
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
		
	@Column(name="name")
	private String name;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="job_id")
	private Job job;
	
	@Enumerated(EnumType.STRING)
	@Column(name="dataset")
	private Dataset dataset;
	
	@Column(name=KEY_AVG_INSERT_SIZE)
	private Integer averageInsertSize;
	
	@Column(name=KEY_INSERT_ERROR_TOLERANCE)
	private Double insertErrorTolerance;
	
	@Column(name=KEY_READ_LENGTH)
	private Integer readLength;
	
	@Enumerated(EnumType.STRING)
	@Column(name="seq_orientation")
	private SeqOrientation seqOrientation;
	
	@Column(name="lib_usage")
	private String usage;
	
	@Enumerated(EnumType.STRING)
	@Column(name="lib_type")
	private Type type;
	
	@Column(name="process_order")
	private Integer index;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true, cascade=CascadeType.ALL)
	@JoinColumn(name="file_paired_1")
	private SeqFile filePaired1;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true, cascade=CascadeType.ALL)
	@JoinColumn(name="file_paired_2")
	private SeqFile filePaired2;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=true, cascade=CascadeType.ALL)
	@JoinColumn(name="file_se")
	private SeqFile seFile;
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public Integer getAverageInsertSize() {
		return averageInsertSize;
	}

	public void setAverageInsertSize(Integer averageInsertSize) {
		this.averageInsertSize = averageInsertSize;
	}

	public Double getInsertErrorTolerance() {
		return insertErrorTolerance;
	}

	public void setInsertErrorTolerance(Double insertErrorTolerance) {
		this.insertErrorTolerance = insertErrorTolerance;
	}
	
	public Integer getReadLength() {
		return readLength;
	}

	public void setReadLength(Integer readLength) {
		this.readLength = readLength;
	}
	
	public SeqOrientation getSeqOrientation() {
		return seqOrientation;
	}

	public void setSeqOrientation(SeqOrientation seqOrientation) {
		this.seqOrientation = seqOrientation;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

    public Set<Usage> getUsageSet() {

        Set<Usage> usageSet = new HashSet<Usage>();

        String[] parts = this.usage.split(",");

        for(String part : parts) {
            usageSet.add(Usage.valueOf(part));
        }

        return usageSet;
    }

    public void setUsageSet(Set<Usage> usageSet) {

        StringJoiner sj = new StringJoiner(",");

        for(Usage u : usageSet) {
            sj.add(u.name());
        }

        this.usage = sj.toString();
    }
	

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public SeqFile getFilePaired1() {
		return filePaired1;
	}

	public void setFilePaired1(SeqFile filePaired1) {
		if (filePaired1 != null) {
			filePaired1.setLibrary(this);
		}
		this.filePaired1 = filePaired1;
	}

	public SeqFile getFilePaired2() {
		return filePaired2;
	}

	public void setFilePaired2(SeqFile filePaired2) {
		if (filePaired2 != null) {
			filePaired2.setLibrary(this);
		}
		this.filePaired2 = filePaired2;
	}

	public SeqFile getSeFile() {
		return seFile;
	}

	public void setSeFile(SeqFile seFile) {
		if (seFile != null) {
			seFile.setLibrary(this);
		}
		this.seFile = seFile;
	}

    public Library copy() {
        Library copy = new Library();

        copy.setIndex(this.index);
        copy.setName(this.name);
        copy.setDataset(this.dataset);
        copy.setAverageInsertSize(this.averageInsertSize);
        copy.setInsertErrorTolerance(this.insertErrorTolerance);
        copy.setReadLength(this.readLength);
        copy.setSeqOrientation(this.seqOrientation);
        copy.setUsage(this.usage);
        copy.setType(this.type);
        copy.setFilePaired1(this.filePaired1);
        copy.setFilePaired2(this.filePaired2);
        copy.setSeFile(this.seFile);

        return copy;
    }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[" + SECTION_PREFIX + this.getIndex().toString() + "]\n")
		.append(KEY_NAME + "=" + this.getName() + "\n")
        .append(KEY_USAGE + "=" + this.getUsage().toString() + "\n")
        .append(KEY_TYPE + "=" + this.getType().toString() + "\n");

        if (this.getAverageInsertSize() != null) {
            sb.append(KEY_AVG_INSERT_SIZE + "=" + this.getAverageInsertSize().toString() + "\n");
        }

        if (this.getInsertErrorTolerance() != null) {
		    sb.append(KEY_INSERT_ERROR_TOLERANCE + "=" + this.getInsertErrorTolerance().toString() + "\n");
        }

        if (this.getReadLength() != null) {
		    sb.append(KEY_READ_LENGTH + "=" + this.getReadLength() + "\n");
        }

        if (this.getSeqOrientation() != null) {
            sb.append(KEY_SEQ_ORIENTATION + "=" + this.getSeqOrientation().toString() + "\n");
        }

        if (this.getDataset() != null) {
            sb.append(KEY_DATASET + "=" + this.getDataset().toString() + "\n");
        }

        if (this.getFilePaired1() != null && this.getFilePaired2() != null &&
                (this.getType() == Type.PE || this.getType() == Type.MP)) {
            sb.append(KEY_FILE_1 + "=" + this.getFilePaired1().getFile().getAbsolutePath() + "\n");
            sb.append(KEY_FILE_2 + "=" + this.getFilePaired2().getFile().getAbsolutePath() + "\n");
        }

        if (this.getSeFile() != null && (this.getType() == Type.SE || this.getDataset() == Dataset.QT))
            sb.append(KEY_FILE_SE + "=" + this.getSeFile().getFile().getAbsolutePath() + "\n");

        return sb.toString();
	}
	
	public static Library parseIniSection(Section iniSection, int index) {
		
		Library ld = new Library();
		
		ld.setIndex(index);
		ld.setName(iniSection.get(KEY_NAME));
        ld.setUsage(iniSection.get(KEY_USAGE));
        ld.setType(Type.valueOf(iniSection.get(KEY_TYPE).toUpperCase()));

        if (iniSection.containsKey(KEY_AVG_INSERT_SIZE)) {
            ld.setAverageInsertSize(Integer.parseInt(iniSection.get(KEY_AVG_INSERT_SIZE)));
        }

        if (iniSection.containsKey(KEY_INSERT_ERROR_TOLERANCE)) {
            ld.setInsertErrorTolerance(Double.parseDouble(iniSection.get(KEY_INSERT_ERROR_TOLERANCE)));
        }

		if (iniSection.containsKey(KEY_READ_LENGTH)) {
            ld.setReadLength(Integer.parseInt(iniSection.get(KEY_READ_LENGTH)));
        }

		if (iniSection.containsKey(KEY_SEQ_ORIENTATION)) {
            ld.setSeqOrientation(SeqOrientation.valueOf(iniSection.get(KEY_SEQ_ORIENTATION).toUpperCase()));
        }

		if (iniSection.containsKey(KEY_FILE_1)) {
            ld.setFilePaired1(new SeqFile(iniSection.get(KEY_FILE_1)));
        }

        if (iniSection.containsKey(KEY_FILE_2)) {
		    ld.setFilePaired2(new SeqFile(iniSection.get(KEY_FILE_2)));
        }

        if (iniSection.containsKey(KEY_FILE_SE)) {
            ld.setSeFile(new SeqFile(iniSection.get(KEY_FILE_SE)));
        }

        if (iniSection.containsKey(KEY_DATASET)) {
		    ld.setDataset(Dataset.valueOf(iniSection.get(KEY_DATASET)));
        }

		return ld;
	}
}


