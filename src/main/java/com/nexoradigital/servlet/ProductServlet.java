package com.nexoradigital.servlet;

import com.nexoradigital.model.Product;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
    private static final String DATA_FILE = "WEB-INF/data/products.txt";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            // Read JSON body
            BufferedReader reader = request.getReader();
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }

            JSONObject json = new JSONObject(jsonBody.toString());
            String name = json.getString("name");
            String type = json.getString("type");
            double price = json.getDouble("price");
            String description = json.getString("description");
            String imageUrl = json.getString("imageUrl");

            // Validate input
            if (name == null || type == null || description == null || imageUrl == null || price < 0) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Invalid input data.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(jsonResponse);
                return;
            }

            // Generate ID
            List<Product> products = loadProducts(request);
            int id = products.isEmpty() ? 1 : products.get(products.size() - 1).getId() + 1;

            // Create product
            Product product = new Product(id, name, type, price, description, imageUrl);

            // Save to file
            String filePath = request.getServletContext().getRealPath(DATA_FILE);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                writer.write(product.toString());
                writer.newLine();
            }

            jsonResponse.put("success", true);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.print(jsonResponse);
            out.flush();
        }
    }

    // Load existing products from file
    private List<Product> loadProducts(HttpServletRequest request) throws IOException {
        List<Product> products = new ArrayList<>();
        String filePath = request.getServletContext().getRealPath(DATA_FILE);
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Product product = new Product(
                            Integer.parseInt(parts[0]),
                            parts[1],
                            parts[2],
                            Double.parseDouble(parts[3]),
                            parts[4],
                            parts[5]
                    );
                    products.add(product);
                }
            }
        }
        return products;
    }
}