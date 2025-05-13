package vn.hoidanit.laptopshop.controller.client;

import java.util.ArrayList;
import java.util.List;

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
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.CartDetailService;
import vn.hoidanit.laptopshop.service.CartService;
import vn.hoidanit.laptopshop.service.ProductService;

@Controller
public class ItemController {

    private final CartService cartService;
    private final CartDetailService cartDetailService;

    private final ProductService productService;

    public ItemController(ProductService productService, CartService cartService, CartDetailService cartDetailService) {
        this.productService = productService;
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
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
}
