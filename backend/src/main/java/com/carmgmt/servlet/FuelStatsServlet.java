package com.carmgmt.servlet;

import com.carmgmt.dto.ApiResponse;
import com.carmgmt.exception.CarNotFoundException;
import com.carmgmt.model.FuelStats;
import com.carmgmt.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet for fuel statistics endpoint
 * GET /servlet/fuel-stats?carId={id}
 */
@Component
public class FuelStatsServlet extends HttpServlet {
    private final CarService carService;
    private final ObjectMapper objectMapper;

    @Autowired
    public FuelStatsServlet(CarService carService) {
        this.carService = carService;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Handle GET request
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String carIdParam = request.getParameter("carId");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        if (carIdParam == null || carIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<FuelStats> errorResponse = ApiResponse.error("carId parameter is required");
            String errorJson = objectMapper.writeValueAsString(errorResponse);
            out.print(errorJson);
            out.flush();
            return;
        }
        
        try {
            Long carId = Long.parseLong(carIdParam);
            FuelStats stats = carService.getFuelStats(carId);
            
            response.setStatus(HttpServletResponse.SC_OK);
            ApiResponse<FuelStats> apiResponse = ApiResponse.success("Fuel statistics retrieved successfully", stats);
            String jsonResponse = objectMapper.writeValueAsString(apiResponse);
            out.print(jsonResponse);
            out.flush();
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ApiResponse<FuelStats> errorResponse = ApiResponse.error("Invalid carId format: " + carIdParam);
            String errorJson = objectMapper.writeValueAsString(errorResponse);
            out.print(errorJson);
            out.flush();
            
        } catch (CarNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ApiResponse<FuelStats> errorResponse = ApiResponse.error(e.getMessage());
            String errorJson = objectMapper.writeValueAsString(errorResponse);
            out.print(errorJson);
            out.flush();
        }
    }
}

