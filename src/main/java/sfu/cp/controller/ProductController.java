package sfu.cp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sfu.cp.model.Product;
import sfu.cp.model.User;
import sfu.cp.rep.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/product_evaluation")
    public String showForm() {
        return "product_evaluation";
    }

    @PostMapping("/submit-product")
    public String handleSubmit(
            @RequestParam("category") String category,
            @RequestParam("modelName") String modelName,
            @RequestParam("file") MultipartFile file,
            Model model,
            @AuthenticationPrincipal User user) {

        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            model.addAttribute("message", "Ошибка при загрузке файла.");
            return "product_evaluation";
        }

        Product product = new Product();
        product.setCategory(category);
        product.setModelName(modelName);
        product.setImagePath("/uploads/" + fileName);
        product.setUser(user);
        productRepository.save(product);

        model.addAttribute("message", "Продукт успешно сохранен!");
        return "product_evaluation";
    }

    @GetMapping("/products")
    public String showAllProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "home";
    }
}
