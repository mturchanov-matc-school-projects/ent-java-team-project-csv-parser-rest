package com.ee_java.team_project.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.Timestamp;
import java.util.Properties;

/**
 * This class serves to provide several utility methods for writing files in the web application.
 * @author pjcraig
 */
public class CSVFileWriter implements PropertiesLoader {
    private Properties properties;
    private String uploadPath;

    private final Logger logger = LogManager.getLogger();

    public static final String PROPERTIES_PATH = "/webapp.properties";
    public static final String UPLOAD_PATH_PROPERTY = "upload.path";

    /**
     * Constructs a new FileProcessor with access to the web app properties.
     */
    public CSVFileWriter() {
        properties = loadProperties(PROPERTIES_PATH);
        uploadPath = properties.getProperty(UPLOAD_PATH_PROPERTY);
    }

    /**
     * Attempts to write a CSV file using the provided input stream to disk. The
     * file path is returned if the file was written successfully or null if not.
     * @param stream The input stream to load data from.
     * @return The path to the file or null.
     */
    public String write(InputStream stream) {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        String fileName = stamp.toString() + ".csv";
        String filePath = uploadPath + "/" + fileName;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
             PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {
            while (reader.ready()) {
                writer.println(reader.readLine());
            }
        } catch (IOException exception) {
            filePath = null;
            logger.error("IO Exception occurred while writing uploaded file '{}'!", fileName, exception);
        } catch (Exception exception) {
            filePath = null;
            logger.error("Unknown exception occurred while writing uploaded file '{}'!", fileName, exception);
        }
        return filePath;
    }

    /**
     * Attempts to write a CSV file using the provided file to disk. The
     * file path is returned if the file was written successfully or null if not.
     * @param file The file to write to disk.
     * @return The path to the file or null.
     */
    public String write(File file) {
        String path = null;
        if (file != null) {
            try (FileInputStream stream = new FileInputStream(file)) {
                path = write(stream);
            } catch (IOException exception) {
                logger.error("Unable to convert file to file input stream.", exception);
            } catch (Exception exception) {
                logger.error("Unknown exception occurred while converting file to file input stream.", exception);
            }
        }
        return path;
    }

    /**
     * Deletes the CSV file at a given path if it exists.
     * @param path The String path of the directory to create.
     * @return Whether or not the file was able to be deleted.
     */
    public boolean delete(String path) {
        File filePath = new File(path);
        if (filePath.exists()) {
            try {
                return filePath.delete();
            } catch (SecurityException exception) {
                logger.error("Unable to delete file due to lack of permissions.", exception);
            } catch (Exception exception) {
                String message = String.format("Unknown error occurred while removing uploaded file %s.", path);
                logger.error(message, exception);
            }
        }
        return false;
    }
}
