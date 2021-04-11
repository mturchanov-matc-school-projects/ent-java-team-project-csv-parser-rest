package com.ee_java.team_project.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ee_java.team_project.csv_parser.CodingCompCsvUtil;
import com.ee_java.team_project.util.CSVFileWriter;
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

    private final Logger logger = LogManager.getLogger();

    /**
     * Handles GET requests by redirecting to index page.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException Whether or not the servlet encounters an error.
     * @throws IOException Whether or not an IO exception occurs.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        boolean success = processCSVFile(request);

        // Verify that CSV file was processed successfully
        if (success) {
            url = "/endpoints.jsp";
            String fileName = request.getParameter("fileName");
            feedback = "Successfully parsed CSV code.";
            if (fileName != null) {
                feedback = "Successfully parsed " + fileName + " to JSON.";
            }
        }

        request.setAttribute("feedback", feedback);

        RequestDispatcher dispatcher = request.getRequestDispatcher(url);
        dispatcher.forward(request, response);
    }

    /**
     * Processes parsing of the uploaded file parts and stores it into the json session attribute.
     * @param request The request made to the servlet for processing.
     * @return Whether or not the processing succeeded without error.
     */
    public boolean processCSVFile(HttpServletRequest request) {
        try {
            Collection<Part> parts = request.getParts();
            CSVFileWriter writer = new CSVFileWriter();
            for (Part part : parts) {
                String path = writer.write(part.getInputStream());

                // Verify that files were successfully written to disk
                if (path != null) {

                    // Parse CSV to JSON
                    CodingCompCsvUtil parser = new CodingCompCsvUtil();
                    Map<List<String>, String> values = parser.readCsvFileFileWithoutPojo(path);

                    // Retrieve columns list and parsed JSON, expecting only 1 entry in the map
                    Map.Entry<List<String>, String> entry = values.entrySet().iterator().next();
                    List<String> columns = entry.getKey();
                    String rawJson = entry.getValue();

                    // Store column and JSON data
                    HttpSession session = request.getSession();
                    session.setAttribute("columns", columns);
                    session.setAttribute("json", rawJson);

                    // Escape loop since only 1 CSV file is allowed at a time
                    return true;
                } else {
                    logger.error("Failed to write uploaded files to disk!");
                }
            }
        } catch (IOException exception) {
            logger.error("IO exception occurred while processing uploaded file parts.", exception);
        } catch (Exception exception) {
            logger.error("Unknown exception occurred while processing uploaded file parts.", exception);
        }
        return false;
    }

}
