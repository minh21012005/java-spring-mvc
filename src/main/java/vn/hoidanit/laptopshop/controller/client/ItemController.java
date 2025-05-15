package vn.hoidanit.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import vn.hoidanit.laptopshop.domain.Cart;
import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.domain.dto.ProductCriteriaDTO;
import vn.hoidanit.laptopshop.service.CartDetailService;
import vn.hoidanit.laptopshop.service.CartService;
import vn.hoidanit.laptopshop.service.OrderService;
import vn.hoidanit.laptopshop.service.ProductService;

@Controller
public class ItemController {

    private final CartService cartService;
    private final CartDetailService cartDetailService;
    private final OrderService orderService;

    private final ProductService productService;

    public ItemController(ProductService productService, CartService cartService, CartDetailService cartDetailService,
            OrderService orderService) {
        this.productService = productService;
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.orderService = orderService;
    }

    @GetMapping("/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        return "client/product/detail";
    }

    @PostMapping("/add-product-to-cart/{id}")
    public String addProductToCart(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long productId = id;
        String email = (String) request.getSession().getAttribute("email");
        this.productService.handleAddProductToCart(email, productId, session);
        return "redirect:/";
    }

    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {
        User user = new User();
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        user.setId(id);

        Cart cart = this.productService.fetchByUser(user);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        double totalPrice = 0;
        for (CartDetail cartDetail : cartDetails) {
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("size", cartDetails.size());
        return "client/cart/show";
    }

    @GetMapping("/delete-cart-product/{id}")
    public String handleDeleteCartProduct(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        CartDetail cartDetail = this.cartDetailService.getCartDetailById(id);
        Cart cart = cartDetail.getCart();
        this.cartDetailService.deleteCartDetailById(id);
        int sum = cart.getSum();
        if (sum > 1) {
            cart.setSum(sum - 1);
            this.cartService.saveCart(cart);
            session.setAttribute("sum", sum - 1);
        } else {
            this.cartService.deleteCart(cart);
            session.setAttribute("sum", 0);
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String getCheckOutPage(Model model, HttpServletRequest request, @RequestParam List<Long> cartId,
            @RequestParam List<Integer> quantity) {
        for (int i = 0; i < cartId.size(); i++) {
            Long id = cartId.get(i);
            Integer qty = quantity.get(i);
            CartDetail cartDetail = this.cartDetailService.getCartDetailById(id);
            cartDetail.setQuantity(qty);
            this.cartDetailService.savCartDetail(cartDetail);
        }

        User user = new User();
        HttpSession session = request.getSession(false);
        long userId = (long) session.getAttribute("id");
        user.setId(userId);

        Cart cart = this.productService.fetchByUser(user);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        double totalPrice = 0;
        for (CartDetail cartDetail : cartDetails) {
            totalPrice += cartDetail.getPrice() * cartDetail.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);
        return "client/cart/checkout";
    }

    @PostMapping("/place-order")
    public String handleConfirmCheckOut(HttpServletRequest request, @RequestParam("receiverName") String receiverName,
            @RequestParam("receiverAddress") String receiverAddress,
            @RequestParam("receiverPhone") String receiverPhone) {
        User user = new User();
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        user.setId(id);

        this.productService.handlePlaceOrder(user, session, receiverName,
                receiverAddress, receiverPhone);

        return "client/homepage/thanks";
    }

    @GetMapping("/order-history")
    public String getOrderHistoryPage(Model model, HttpServletRequest request) {
        User user = new User();
        HttpSession session = request.getSession();
        long id = (long) session.getAttribute("id");
        user.setId(id);

        List<Order> orders = this.orderService.fetchByUser(user);
        model.addAttribute("orders", orders);
        return "client/cart/order-history";
    }

    @PostMapping("/add-products-to-cart/{id}")
    public String addProductsToCart(@PathVariable("id") long id, @RequestParam("quantity") long quantity,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long productId = id;
        String email = (String) request.getSession().getAttribute("email");
        this.productService.handleAddProductsToCart(email, productId, quantity, session);
        return "redirect:/";
    }

    @GetMapping("/products")
    public String getProducts(Model model, ProductCriteriaDTO productCriteriaDTO, HttpServletRequest request) {
        int page = 1;
        try {
            if (productCriteriaDTO.getPage().isPresent()) {
                page = Integer.parseInt(productCriteriaDTO.getPage().get());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        Pageable pageable = null;

        if (productCriteriaDTO.getSort() != null) {
            String sort = productCriteriaDTO.getSort().get();
            if (sort.equals("gia-tang-dan")) {
                pageable = PageRequest.of(page - 1, 3, Sort.by(Product_.PRICE).ascending());
            } else if (sort.equals("gia-giam-dan")) {
                pageable = PageRequest.of(page - 1, 3, Sort.by(Product_.PRICE).descending());
            } else {
                pageable = PageRequest.of(page - 1, 3);
            }
        } else {
            pageable = PageRequest.of(page - 1, 3);
        }

        Page<Product> products = this.productService.getAllProducts(pageable, productCriteriaDTO);
        List<Product> list = products.getContent();

        String qs = request.getQueryString();
        if (qs != null && !qs.isBlank()) {
            qs = qs.replace("page=" + page, "");
        }

        model.addAttribute("products", list);
        model.addAttribute("size", list.size());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", products.getTotalPages());
        model.addAttribute("queryString", qs);
        return "client/product/show";
    }
}
