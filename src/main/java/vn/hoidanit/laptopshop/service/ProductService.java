package vn.hoidanit.laptopshop.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;
import vn.hoidanit.laptopshop.repository.CartRepository;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.repository.ProductRepository;
import vn.hoidanit.laptopshop.service.specification.ProductSpecs;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository,
            CartDetailRepository cartDetailRepository, UserService userService, OrderRepository orderRepository,
            OrderDetailRepository orderDetailRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public Page<Product> getAllProducts(Pageable pageable, ProductCriteriaDTO productCriteriaDTO) {
        Specification<Product> combinedSpec = Specification.where(null);
        if (productCriteriaDTO.getTarget() != null) {
            Specification<Product> currentSpec = ProductSpecs.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpec = combinedSpec.and(currentSpec);
        }
        if (productCriteriaDTO.getFactory() != null) {
            Specification<Product> currentSpec = ProductSpecs.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpec = combinedSpec.and(currentSpec);
        }
        if (productCriteriaDTO.getPrice() != null) {
            Specification<Product> currentSpec = this.buildPriceSpecification(productCriteriaDTO.getPrice().get());
            combinedSpec = combinedSpec.and(currentSpec);
        }
        return this.productRepository.findAll(combinedSpec, pageable);
    }

    public Specification<Product> buildPriceSpecification(List<String> price) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.disjunction();
            int count = 0;
            for (String p : price) {
                if (p.equals("duoi-1000-do")) {
                    double min = 0;
                    double max = 1000;
                    predicate = criteriaBuilder.or(predicate,
                            criteriaBuilder.between(root.get(Product_.PRICE), min, max));
                    count++;
                }
                if (p.equals("1000-1500-do")) {
                    double min = 1000;
                    double max = 1500;
                    predicate = criteriaBuilder.or(predicate,
                            criteriaBuilder.between(root.get(Product_.PRICE), min, max));
                    count++;
                }
                if (p.equals("1500-2000-do")) {
                    double min = 1500;
                    double max = 2000;
                    predicate = criteriaBuilder.or(predicate,
                            criteriaBuilder.between(root.get(Product_.PRICE), min, max));
                    count++;
                }
                if (p.equals("tren-2000-do")) {
                    double min = 2000;
                    predicate = criteriaBuilder.ge(root.get(Product_.PRICE), min);
                    count++;
                }
            }
            if (count == 0) {
                return null;
            }
            return predicate;
        };
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    public List<Product> getAllProducts() {
        return this.productRepository.findAll();
    }

    public Product save(Product product) {
        return this.productRepository.save(product);
    }

    public Product getProductById(long id) {
        return this.productRepository.findById(id);
    }

    public Product deleteProductById(long id) {
        return this.productRepository.deleteById(id);
    }

    public void handleAddProductToCart(String email, long productId, HttpSession session) {
        User user = this.userService.getOneUserByEmail(email);
        if (user != null) {
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setSum(0);

                cart = this.cartRepository.save(newCart);
            }
            Product product = this.productRepository.findById(productId);
            CartDetail oldCartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product);
            if (oldCartDetail == null) {
                CartDetail cartDetail = new CartDetail();
                cartDetail.setCart(cart);
                cartDetail.setProduct(product);
                cartDetail.setPrice(product.getPrice());
                cartDetail.setQuantity(1);
                this.cartDetailRepository.save(cartDetail);

                int s = cart.getSum() + 1;
                cart.setSum(s);
                this.cartRepository.save(cart);
                session.setAttribute("sum", s);
            } else {
                oldCartDetail.setQuantity(oldCartDetail.getQuantity() + 1);
                this.cartDetailRepository.save(oldCartDetail);
            }
        }
    }

    public Cart fetchByUser(User user) {
        return this.cartRepository.findByUser(user);
    }

    public void handlePlaceOrder(User user, HttpSession session, String receiverName, String receiverAddress,
            String receiverPhone) {
        Cart cart = this.fetchByUser(user);
        if (cart != null) {
            double totalPrice = 0;
            List<CartDetail> cartDetails = cart.getCartDetails();
            Order order = new Order();
            order.setUser(user);
            order.setReceiverName(receiverName);
            order.setReceiverAddress(receiverAddress);
            order.setReceiverPhone(receiverPhone);
            order.setStatus("PENDING");
            for (CartDetail cartDetail : cartDetails) {
                totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
            }
            order.setTotalPrice(totalPrice);
            order = this.orderRepository.save(order);

            for (CartDetail cartDetail : cartDetails) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setProduct(cartDetail.getProduct());
                orderDetail.setPrice(cartDetail.getPrice());
                orderDetail.setQuantity(cartDetail.getQuantity());

                this.orderDetailRepository.save(orderDetail);
            }

            for (CartDetail cartDetail : cartDetails) {
                this.cartDetailRepository.delete(cartDetail);
            }
            this.cartRepository.delete(cart);
            session.setAttribute("sum", 0);
        }

    }

    public void handleAddProductsToCart(String email, long productId, long quantity, HttpSession session) {
        User user = this.userService.getOneUserByEmail(email);
        if (user != null) {
            Cart cart = this.cartRepository.findByUser(user);
            if (cart == null) {
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setSum(0);

                cart = this.cartRepository.save(newCart);
            }
            Product product = this.productRepository.findById(productId);
            CartDetail oldCartDetail = this.cartDetailRepository.findByCartAndProduct(cart, product);
            if (oldCartDetail == null) {
                CartDetail cartDetail = new CartDetail();
                cartDetail.setCart(cart);
                cartDetail.setProduct(product);
                cartDetail.setPrice(product.getPrice());
                cartDetail.setQuantity(quantity);
                this.cartDetailRepository.save(cartDetail);

                int s = cart.getSum() + 1;
                cart.setSum(s);
                this.cartRepository.save(cart);
                session.setAttribute("sum", s);
            } else {
                oldCartDetail.setQuantity(oldCartDetail.getQuantity() + quantity);
                this.cartDetailRepository.save(oldCartDetail);
            }
        }
    }
}
