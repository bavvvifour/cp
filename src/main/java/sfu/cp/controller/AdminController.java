package sfu.cp.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sfu.cp.model.PriceHistory;
import sfu.cp.model.Product;
import sfu.cp.model.User;
import sfu.cp.rep.PriceHistoryRepository;
import sfu.cp.rep.ProductRepository;
import sfu.cp.rep.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public AdminController(UserRepository userRepository, ProductRepository productRepository, PriceHistoryRepository priceHistoryRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    @GetMapping("/admin")
    public String adminPanel(@AuthenticationPrincipal User admin, Model model) {
        User user = userRepository.findById(admin.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Product> allProducts = productRepository.findAllWithPriceHistory();

        model.addAttribute("username", user.getFio());
        model.addAttribute("allProducts", allProducts);
        return "admin";
    }

    @PostMapping("/admin/update-price")
    public String updatePrice(@RequestParam Long productId,
                              @RequestParam Double newPrice,
                              @AuthenticationPrincipal User admin) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Double oldPrice = product.getPrice();

        PriceHistory history = new PriceHistory();
        history.setProduct(product);
        history.setOldPrice(oldPrice);
        history.setNewPrice(newPrice);
        history.setChangedBy(admin);
        priceHistoryRepository.save(history);

        product.setLoanAmount(newPrice);
        product.setPrice(newPrice);
        productRepository.save(product);

        return "redirect:/admin";
    }


    @GetMapping("/admin/price-history")
    public String getPriceHistory(@RequestParam Long productId, Model model) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<PriceHistory> history = priceHistoryRepository.findByProductOrderByChangeDateDesc(product);
        model.addAttribute("history", history);
        return "fragments/price-history :: history";
    }


    @PostMapping("/delete-product/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/mark-sold/{id}")
    public String markAsSold(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setStatus(Product.Status.SOLD);
        productRepository.save(product);
        return "redirect:/admin";
    }

    // Add this method to AdminController.java
    @PostMapping("/set-redemption-date/{id}")
    public String setRedemptionDate(@PathVariable Long id,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate redemptionDate,
                                    @RequestParam Double loanAmount) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setRedemptionDate(redemptionDate);
        product.setLoanAmount(loanAmount);
        productRepository.save(product);
        return "redirect:/admin";
    }

    @PostMapping("/clear-redemption-date/{id}")
    public String clearRedemptionDate(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setRedemptionDate(null);
        product.setLoanAmount(null);
        productRepository.save(product);
        return "redirect:/admin";
    }
}