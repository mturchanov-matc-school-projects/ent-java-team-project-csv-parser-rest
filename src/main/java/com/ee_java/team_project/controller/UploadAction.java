package com.ee_java.team_project.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ee_java.team_project.csv_parser.CodingCompCsvUtil;
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
    public static final String UPLOAD_PATH_PROPERTY = "upload.path";

    /**
     * Initializes the servlet with a properties instance to load
     * data from.
     * @throws ServletException If the Servlet encounters an issue.
     */
    public void init() throws ServletException {
        properties = loadProperties(PROPERTIES_PATH);
        uploadPath = properties.getProperty(UPLOAD_PATH_PROPERTY);
        initializeUploadPath(uploadPath);
    }

    /**
     * Handles GET requests by redirecting to index page.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException Whether or not the servlet encounters an error.
     * @throws IOException Whether or not an IO exception occurs.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }

    /**
     * Processes files uploaded via the upload form for CSV parsing.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException Whether or not the servlet encounters an error.
     * @throws IOException Whether or not an IO exception occurs.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = "/index.jsp";
        String feedback = "Failed to upload the file. Please try again later.";
        String path = writeUploadedFiles(request.getParts());

        // Verify that a file was successfully written in the upload directory
        if (path != null) {
            CodingCompCsvUtil parser = new CodingCompCsvUtil();
            Map<List<String>, String> values = parser.readCsvFileFileWithoutPojo(path);

            // Retrieves the raw JSON text from the values map, expecting only 1 entry in the map
            Map.Entry<List<String>, String> entry = values.entrySet().iterator().next();
            String rawJson = entry.getValue();

            request.setAttribute("json", rawJson);

            url = "/endpoints.jsp";
            feedback = "Successfully uploaded CSV file";

            boolean success = deleteFile(path);
            if (!success) {
                logger.error("Unable to remove file {} for cleanup.", path);
            }
        }

        request.setAttribute("feedback", feedback);

        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

    /**
     * Creates the file path if it does not already exist.
     * @param path The String path of the directory to create.
     */
    public void initializeUploadPath(String path) {
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
    }

    /**
     * Writes the provided uploaded parts into the upload directory for this web
     * application.
     * @param parts The Collection of Part objects to write.
     * @return The path to the new file or null.
     */
    public String writeUploadedFiles(Collection<Part> parts) {
        String path = null;
        for (Part part : parts) {
            try {
                Timestamp stamp = new Timestamp(System.currentTimeMillis());

                String analysisPath = uploadPath + "/";
                initializeUploadPath(analysisPath);

                path = analysisPath + stamp.toString() + ".csv";
                part.write(path);
            } catch (IOException exception) {
                logger.error("Error writing uploaded file!", exception);
                exception.printStackTrace();
            } catch (Exception exception) {
                logger.error("Unknown exception!", exception);
                exception.printStackTrace();
            }
        }
        return path;
    }

    /**
     * Deletes the file at a given path if it exists.
     * @param path The String path of the directory to create.
     * @return Whether or not the file was able to be deleted.
     */
    public boolean deleteFile(String path) {
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
