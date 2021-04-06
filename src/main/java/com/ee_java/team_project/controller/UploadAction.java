package com.ee_java.team_project.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Properties;

import com.ee_java.team_project.util.PropertiesLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Processes POST requests containing an uploaded CSV file.
 * @author pjcraig
 */
@WebServlet (
        name = "UploadAction",
        urlPatterns = {"/upload"}
)
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10, 	// 10 MB
        maxFileSize = 1024 * 1024 * 50,      	// 50 MB
        maxRequestSize = 1024 * 1024 * 100   	// 100 MB
)
public class UploadAction extends HttpServlet implements PropertiesLoader {
    private Properties properties;
    private String uploadPath;

    private final Logger logger = LogManager.getLogger();

    public static final String PROPERTIES_PATH = "/webapp.properties";
    public static final String UPLOAD_PATH_PROPERTY = "upload-path";

    /**
     * Initializes the servlet with a properties instance to load
     * data from.
     * @throws ServletException If the Servlet encounters an issue.
     */
    public void init() throws ServletException {
        properties = loadProperties(PROPERTIES_PATH);
        uploadPath = properties.getProperty(UPLOAD_PATH_PROPERTY);
        logger.debug("Loaded upload path of {}", uploadPath);
        initializeUploadPath(uploadPath);
    }

    /**
     * Processes files uploaded via the upload form for CSV parsing.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException Whether or not the servlet encounters an error.
     * @throws IOException Whether or not an IO exception occurs.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean success = writeUploadedFiles(request.getParts());
        logger.debug("Received POST request with upload success status: {}!", success);
    }

    /**
     * Creates the file path if it does not already exist.
     * @param path The String path of the directory to create.
     */
    public void initializeUploadPath(String path) {
        File filePath = new File(path);
        if (filePath == null) logger.debug("File path is NULL!");
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
    }

    /**
     * Writes the provided uploaded parts into the upload directory for this web
     * application.
     * @param parts The Collection of Part objects to write.
     * @return Whether or not the file was able to be written in the upload directory.
     */
    public boolean writeUploadedFiles(Collection<Part> parts) {
        boolean success = false;
        for (Part part : parts) {
            try {
                Timestamp stamp = new Timestamp(System.currentTimeMillis());

                String analysisPath = uploadPath + "/";
                initializeUploadPath(analysisPath);

                String fileName = analysisPath + stamp.toString() + ".csv";
                part.write(fileName);

                success = true;
            } catch (IOException exception) {
                System.out.println("Error writing uploaded file!");
                exception.printStackTrace();
            } catch (Exception exception) {
                System.out.println("Unknown exception!");
                exception.printStackTrace();
            }
        }
        return success;
    }

}
