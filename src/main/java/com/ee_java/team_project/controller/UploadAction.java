package com.ee_java.team_project.controller;

import com.ee_java.team_project.csv_parser.CodingCompCsvUtil;
import com.ee_java.team_project.util.CSVFileWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
public class UploadAction extends HttpServlet {

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
        String feedback = "Successfully parsed CSV code.";

        // Retrieve uploaded file name
        String fileName = request.getParameter("fileName");

        // Generate success message including file name
        if (fileName != null) {
            feedback = "Successfully parsed " + fileName + " to JSON.";
        }

        boolean success = processCSVFile(request);

        // Verify that CSV file was processed successfully
        if (success) {
            url = "/endpoints.jsp";
        } else {
            feedback = "Failed to upload the file. Please try again later.";
            if (fileName != null && !fileName.toLowerCase().endsWith(".csv")) {
                feedback = "You must upload a valid CSV file.";
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
        CSVFileWriter writer = new CSVFileWriter();
        String csvText = request.getParameter("csvText");
        String path = null;
        // Verify CSV text input exists before checking uploaded file
        if (csvText != null) {
            path = writer.write(new ByteArrayInputStream(csvText.getBytes()));
        } else {
            String fileName = request.getParameter("fileName");
            if (fileName != null && fileName.toLowerCase().endsWith(".csv")) {
                try {
                    Collection<Part> parts = request.getParts();
                    Part part = parts.iterator().next();
                    path = writer.write(part.getInputStream());
                } catch (IOException exception) {
                    logger.error("IO exception occurred while processing uploaded file parts.", exception);
                } catch (Exception exception) {
                    logger.error("Unknown exception occurred while processing uploaded file parts.", exception);
                }
            }
        }

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

            writer.delete(path);

            // Escape loop since only 1 CSV file is allowed at a time
            return true;
        } else {
            logger.error("Failed to write uploaded files to disk!");
        }

        return false;
    }

}
