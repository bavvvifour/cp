package sfu.cp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sfu.cp.model.Product;
import sfu.cp.rep.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ExportService {

    private final ProductRepository productRepository;

    public ExportService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void exportProductsToJson(String filePath) {
        try {
            List<Product> products = productRepository.findAll();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), products);
            System.out.println("Products exported successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error exporting products: " + e.getMessage());
        }
    }
}
