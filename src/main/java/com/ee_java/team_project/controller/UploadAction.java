package com.ee_java.team_project.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
public class UploadAction extends HttpServlet {
    Logger logger = LogManager.getLogger();

    /**
     * Processes files uploaded via the upload form for CSV parsing.
     * @param request The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException Whether or not the servlet encounters an error.
     * @throws IOException Whether or not an IO exception occurs.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tempFilePath = request.getParameter("file");
        logger.debug("Received POST request with file @ {}!", tempFilePath);
    }

}
