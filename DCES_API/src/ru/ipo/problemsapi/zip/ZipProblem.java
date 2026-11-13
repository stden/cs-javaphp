package ru.ipo.problemsapi.zip;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.ipo.problemsapi.Problem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 23.05.2010
 * Time: 17:51:27
 */
public class ZipProblem implements Problem {

    /*public final static String STATEMENT_TEXT = "STATEMENT TEXT";
    public final static String STATEMENT_TEXT_EXT = "STATEMENT TEXT EXT";
    public final static String STATEMENT = "STATEMENT";*/

    private static final String PATH_SEPARATOR = "/";

    private final static String MANIFEST = "MANIFEST";
    private final static String TAG_CONTENTS = "contents";
    private final static String TAG_ALIAS = "alias";
    private final static String TAG_TEACHER = "teacher";
    private final static String ATTR_PATH = "path";

    private final static String ATTR_TARGET = "target";
    private final File zip;
    private final Set<String> teacherResources = new HashSet<String>(); //set of paths
    private final Map<String, String> manifestResources = new HashMap<String, String>();
    private final Map<String, String> aliases = new HashMap<String, String>();

    public ZipProblem(String fname) throws IOException {
        this(new File(fname));
    }

    public ZipProblem(File file) throws IOException {
        zip = file;
        readManifest();
    }

    private void readManifest() throws IOException {
        InputStream manifest = getReadStream(MANIFEST);
        if (manifest == null)
            throw new IOException("No manifest in problem archive");

        //analyze manifest as XML
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document document = builder.parse(manifest);
            Element root = document.getDocumentElement();

            NodeList list;

            //read aliases
            list = root.getElementsByTagName(TAG_ALIAS);
            for (int i = 0; i < list.getLength(); i++) {
                Element alias = (Element) list.item(i);
                aliases.put(alias.getAttribute(ATTR_PATH), alias.getAttribute(ATTR_TARGET));
            }

            //read contents
            list = root.getElementsByTagName(TAG_CONTENTS);
            for (int i = 0; i < list.getLength(); i++) {
                Element contents = (Element) list.item(i);
                String text = contents.getTextContent();
                manifestResources.put(
                        contents.getAttribute(ATTR_PATH),
                        text
                );
            }

            //read teacher resources
            list = root.getElementsByTagName(TAG_TEACHER);
            for (int i = 0; i < list.getLength(); i++) {
                Element teacher = (Element) list.item(i);
                teacherResources.add(teacher.getAttribute(ATTR_PATH));
            }

        } catch (ParserConfigurationException e) {
            throw new IOException("Failed to read manifest");
        } catch (SAXException e) {
            throw new IOException("Error in manifest file");
        } finally {
            manifest.close();
        }
    }

    public InputStream getReadStream(String path) throws IOException {
        //resolve alias
        String alias = aliases.get(path);
        if (alias != null)
            path = alias;

        //search for entry in manifest first
        String chars = manifestResources.get(path);
        if (chars != null)
            //return new ByteArrayInputStream(chars.getBytes(manifestCharset));
            throw new IOException("Failed to get string data as binary stream");

        //search in zip
        ZipInputStream in = new ZipInputStream(new FileInputStream(zip));
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null)
            if (entry.getName().equals(path))
                return in;
        return null;
    }

    /**
     * Treats path entry as a string and returns. When resource is not in manifest, uses the default charset
     *
     * @param path path to the data
     * @return string with path contents
     */
    public String getString(String path) throws IOException {
        String s = manifestResources.get(path);
        if (s == null)
            throw new IOException("Failed to get binary data as a string");
        return s;
    }    

    public boolean isTeacher(String path) {
        String[] dirs = path.split(PATH_SEPARATOR);

        StringBuilder p = new StringBuilder();
        for (String dir : dirs) {
            if (dir.length() == 0)
                continue;
            p.append(dir);
            if (teacherResources.contains(p.toString()))
                return true;
            p.append(PATH_SEPARATOR);
            if (teacherResources.contains(p.toString()))
                return true;
        }

        return false;
    }

    //no aliases are listed

    public String[] list() {
        return null;
    }
}
