package uk.ac.tgac.conan.core.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import uk.ac.tgac.conan.core.data.Organism;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: maplesod
 * Date: 29/10/13
 * Time: 11:52
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractXmlJobConfiguration {

    // **** Xml Config Keys ****
    public static final String KEY_ELEM_AUTHOR          = "author";
    public static final String KEY_ELEM_COLLABORATOR    = "collaborator";
    public static final String KEY_ELEM_INSTITUTION     = "institution";
    public static final String KEY_ELEM_TITLE           = "title";
    public static final String KEY_ELEM_DESCRIPTION     = "description";
    public static final String KEY_ELEM_ORGANISM        = "organism";

    // **** Job Details ****
    private String author;
    private String collaborator;
    private String institution;
    private String title;
    private String description;
    private Organism organism;


    // **** System vars ****

    /**
     * Name of the Xml file containing job description
     */
    private File configFile;

    /**
     * The output directory for this job
     */
    private File outputDir;

    /**
     * A short identifier for this job, for use on scheduling systems
     */
    private String jobPrefix;


    public AbstractXmlJobConfiguration(File configFile, File outputDir, String jobPrefix) throws IOException {

        this.author = "";
        this.collaborator = "";
        this.institution = "";
        this.title = "";
        this.description = "";
        this.organism = null;

        this.configFile = configFile;
        this.outputDir = outputDir;
        this.jobPrefix = jobPrefix;


    }



    public void parseXml() throws IOException {

        // Get a document builder factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            Document dom = db.parse(this.configFile);

            // Get the root element
            Element root = dom.getDocumentElement();

            // Process all known elements
            this.author = XmlHelper.getTextValue(root, KEY_ELEM_AUTHOR);
            this.institution = XmlHelper.getTextValue(root, KEY_ELEM_INSTITUTION);
            this.collaborator = XmlHelper.getTextValue(root, KEY_ELEM_COLLABORATOR);
            this.title = XmlHelper.getTextValue(root, KEY_ELEM_TITLE);
            this.description = XmlHelper.getTextValue(root, KEY_ELEM_DESCRIPTION);

            // Organism
            Element organismElement = XmlHelper.getDistinctElementByName(root, KEY_ELEM_ORGANISM);
            this.organism = organismElement == null ? null : new Organism(organismElement);

            // Do whatever else the child class needs
            this.internalParseXml(root);
        }
        catch(ParserConfigurationException| SAXException pce) {
            throw new IOException(pce);
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(String collaborator) {
        this.collaborator = collaborator;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Organism getOrganism() {
        return organism;
    }

    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    public File getConfigFile() {
        return configFile;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public String getJobPrefix() {
        return jobPrefix;
    }

    public void setJobPrefix(String jobPrefix) {
        this.jobPrefix = jobPrefix;
    }

    protected abstract void internalParseXml(Element element) throws IOException;
}
