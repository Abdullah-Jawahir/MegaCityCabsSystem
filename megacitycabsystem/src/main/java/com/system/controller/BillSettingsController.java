package com.system.controller;

import com.system.model.BillingSetting;
import com.system.service.BillingSettingService; 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillSettingsController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BillingSettingService billingSettingService; // Use the service
    private static final Logger logger = Logger.getLogger(BillSettingsController.class.getName());

    @Override
    public void init() throws ServletException {
        super.init();
        billingSettingService = new BillingSettingService(); // Initialize it
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Load all billing settings from the database
        Map<String, Float> billingSettings = billingSettingService.loadAllSettings();

        // Set billing settings as attributes in the request
        request.setAttribute("taxRate", billingSettings.get("tax_rate"));
        request.setAttribute("defaultDiscountRate", billingSettings.get("default_discount_rate"));

        // Forward to the bill settings JSP
        request.getRequestDispatcher("/WEB-INF/views/admin/billSettings/BillSettings.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     try {
            // Retrieve parameters from the request
            float taxRate = Float.parseFloat(request.getParameter("taxRate"));
            float defaultDiscountRate = Float.parseFloat(request.getParameter("defaultDiscountRate"));

            // Update the tax rate setting in the database
            BillingSetting taxRateSetting = billingSettingService.getSettingByName("tax_rate");
            if (taxRateSetting != null) {
                taxRateSetting.setSettingValue(taxRate);
                boolean taxRateUpdated = billingSettingService.updateSetting(taxRateSetting);
                if (!taxRateUpdated) {
                    logger.log(Level.SEVERE, "Failed to update tax rate setting.");
                    request.setAttribute("errorMessage", "Failed to update tax rate setting.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
            } else {
                logger.log(Level.SEVERE, "Tax rate setting not found.");
                request.setAttribute("errorMessage", "Tax rate setting not found.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // Update the default discount rate setting in the database
            BillingSetting defaultDiscountRateSetting = billingSettingService.getSettingByName("default_discount_rate");
            if (defaultDiscountRateSetting != null) {
                defaultDiscountRateSetting.setSettingValue(defaultDiscountRate);
                boolean defaultDiscountRateUpdated = billingSettingService.updateSetting(defaultDiscountRateSetting);
                if (!defaultDiscountRateUpdated) {
                    logger.log(Level.SEVERE, "Failed to update default discount rate setting.");
                    request.setAttribute("errorMessage", "Failed to update default discount rate setting.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
            } else {
                logger.log(Level.SEVERE, "Default discount rate setting not found.");
                request.setAttribute("errorMessage", "Default discount rate setting not found.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            response.sendRedirect("billSettings?success=Bill settings updated successfully");
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Invalid input data for bill settings: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "Invalid input data for bill settings.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing bill settings update: " + e.getMessage(), e);
            request.setAttribute("errorMessage", "An error occurred while processing your request.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}