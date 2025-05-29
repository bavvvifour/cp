package sfu.cp.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sfu.cp.model.Product;
import sfu.cp.model.PriceHistory;
import sfu.cp.model.User;
import sfu.cp.rep.ProductRepository;
import sfu.cp.rep.PriceHistoryRepository;
import sfu.cp.rep.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ProfileController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PriceHistoryRepository priceHistoryRepository;

    public ProfileController(ProductRepository productRepository,
                             UserRepository userRepository,
                             PriceHistoryRepository priceHistoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.priceHistoryRepository = priceHistoryRepository;
    }

    @GetMapping("/profile")
    public String getProfilePage(@AuthenticationPrincipal User currentUser, Model model) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Product> products = productRepository.findByUserAndStatusNot(user, Product.Status.SOLD);

        products.forEach(product -> {
            if (product.getRedemptionDate() != null &&
                    product.getRedemptionDate().isBefore(LocalDate.now()) &&
                    product.getStatus() != Product.Status.CONFISCATED) {
                product.setStatus(Product.Status.CONFISCATED);
                product.setConfiscationDate(LocalDate.now());
                productRepository.save(product);
            }
        });

        List<PriceHistory> priceHistory = priceHistoryRepository.findByProduct_UserOrderByChangeDateDesc(user);

        model.addAttribute("userFio", user.getFio());
        model.addAttribute("products", products);
        model.addAttribute("priceHistory", priceHistory);

        return "profile";
    }

    @PostMapping("/profile/sold")
    public String markAsSold(@RequestParam Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStatus(Product.Status.SOLD);
        productRepository.save(product);

        return "redirect:/profile";
    }
}
