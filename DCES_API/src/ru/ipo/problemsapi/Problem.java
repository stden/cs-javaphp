package ru.ipo.problemsapi;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Ilya
 * Date: 23.05.2010
 * Time: 15:51:05
 */
public interface Problem {

    static final String PROBLEM = "PROBLEM";
    static final String PROBLEM_EXT = "PROBLEM EXT";    

    /**
     * Opens a stream to read the entry. Fails with IOError if entry contains String
     * @param path to the data
     * @return stream to read data
     * @throws java.io.IOException if problem may not be read
     */
    InputStream getReadStream(String path) throws IOException;

    /**
     * Returns entry contents as a String. Fails with IOException if entry contains binary data 
     * @param path path to the data
     * @return entry as a String
     * @throws java.io.IOException if io stream failed to read
     */
    String getString(String path) throws IOException;

    /**
     * Returns if the entry is only for teachers
     * @param path path to the data
     * @return if the entry is only for teachers
     */
    boolean isTeacher(String path);

    /**
     * List all paths from the problem, no aliases are returned
     * @return list of paths
     */
    String[] list();    
}