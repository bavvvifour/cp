package sfu.cp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sfu.cp.model.PriceHistory;
import sfu.cp.model.Product;
import sfu.cp.model.Role;
import sfu.cp.model.User;
import sfu.cp.rep.ProductRepository;
import sfu.cp.rep.RoleRepository;
import sfu.cp.rep.UserRepository;

import java.time.LocalDate;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(userRole);

        User admin = new User();
        admin.setFio("Admin Adminovich");
        admin.setPhone("1234567890");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);

        User user = new User();
        user.setFio("User Userovich");
        user.setPhone("0987654321");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("user"));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        createProduct(user, "Electronics", "Smartphone", 500.0, Product.Status.PAWNED, LocalDate.now().minusDays(5), admin);
        createProduct(user, "Electronics", "Laptop", 1500.0, Product.Status.FOR_SALE, LocalDate.now().plusDays(30), admin);
        createProduct(user, "Home Appliances", "Microwave", 200.0, Product.Status.PAWNED, LocalDate.now().minusDays(2), admin);
        createProduct(user, "Home Appliances", "Refrigerator", 800.0, Product.Status.SOLD, LocalDate.now().plusDays(15), admin);
        createProduct(user, "Furniture", "Sofa", 1200.0, Product.Status.CONFISCATED, LocalDate.now().minusDays(10), admin);
        createProduct(user, "Furniture", "Desk", 400.0, Product.Status.EVALUATION, LocalDate.now().plusDays(7), admin);
        createProduct(user, "Clothing", "Jacket", 150.0, Product.Status.FOR_SALE, LocalDate.now().plusDays(20), admin);
        createProduct(user, "Clothing", "Jeans", 100.0, Product.Status.PAWNED, LocalDate.now().minusDays(3), admin);
        createProduct(user, "Jewelry", "Gold Ring", 2500.0, Product.Status.SOLD, LocalDate.now().plusDays(60), admin);
        createProduct(user, "Jewelry", "Silver Necklace", 500.0, Product.Status.CONFISCATED, LocalDate.now().minusDays(7), admin);
        createProduct(user, "Books", "Science Fiction Book", 30.0, Product.Status.FOR_SALE, LocalDate.now().plusDays(10), admin);
        createProduct(user, "Books", "Cookbook", 25.0, Product.Status.EVALUATION, LocalDate.now().plusDays(5), admin);
        createProduct(user, "Sports Equipment", "Treadmill", 1000.0, Product.Status.PAWNED, LocalDate.now().minusDays(1), admin);
        createProduct(user, "Sports Equipment", "Yoga Mat", 50.0, Product.Status.FOR_SALE, LocalDate.now().plusDays(15), admin);
        createProduct(user, "Toys", "Toy Car", 20.0, Product.Status.EVALUATION, LocalDate.now().plusDays(3), admin);
        createProduct(user, "Toys", "Doll", 15.0, Product.Status.PAWNED, LocalDate.now().minusDays(4), admin);
        createProduct(user, "Music Instruments", "Guitar", 600.0, Product.Status.FOR_SALE, LocalDate.now().plusDays(25), admin);
        createProduct(user, "Music Instruments", "Piano", 3000.0, Product.Status.CONFISCATED, LocalDate.now().minusDays(20), admin);
        createProduct(user, "Art", "Painting", 2000.0, Product.Status.SOLD, LocalDate.now().plusDays(90), admin);
        createProduct(user, "Art", "Sculpture", 5000.0, Product.Status.EVALUATION, LocalDate.now().plusDays(14), admin);
    }

    private void createProduct(User user, String category, String modelName, double price, Product.Status status, LocalDate redemptionDate, User changedBy) {
        Product product = new Product();
        product.setCategory(category);
        product.setModelName(modelName);
        product.setPrice(price);
        product.setUser(user);
        product.setStatus(status);
        product.setLoanAmount(price * 0.7);
        product.setRedemptionDate(redemptionDate);

        PriceHistory priceHistory = new PriceHistory();
        priceHistory.setOldPrice(price * 0.9);
        priceHistory.setNewPrice(price);
        priceHistory.setChangedBy(changedBy);
        product.getPriceHistory().add(priceHistory);

        if (status != Product.Status.CONFISCATED && redemptionDate.isBefore(LocalDate.now())) {
            product.setStatus(Product.Status.CONFISCATED);
            product.setConfiscationDate(LocalDate.now());
        }

        productRepository.save(product);
    }
}