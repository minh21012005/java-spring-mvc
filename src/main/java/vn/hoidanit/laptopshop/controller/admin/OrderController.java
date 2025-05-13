package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;
import vn.hoidanit.laptopshop.service.OrderDetailService;
import vn.hoidanit.laptopshop.service.OrderService;

@Controller
public class OrderController {

    private final OrderDetailRepository orderDetailRepository;

    private final OrderRepository orderRepository;

    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    public OrderController(OrderService orderService, OrderDetailService orderDetailService,
            OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    @GetMapping("/admin/order")
    public String getDashboard(Model model) {
        List<Order> orders = this.orderService.getAllOrder();
        model.addAttribute("orders", orders);
        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getDetailPage(Model model, @PathVariable("id") long id) {
        Order order = this.orderService.getOrderById(id);
        List<OrderDetail> orderDetails = this.orderDetailService.getOrderDetailByOrder(order);
        model.addAttribute("orderDetails", orderDetails);
        return "admin/order/detail";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable("id") long id) {
        Order order = this.orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String updateOrder(@ModelAttribute("order") Order order) {
        String status = order.getStatus();
        long id = order.getId();
        Order currentOrder = this.orderService.getOrderById(id);
        currentOrder.setStatus(status);
        this.orderRepository.save(currentOrder);
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrderPage(Model model, @PathVariable("id") long id) {
        Order order = this.orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String deleteOrder(@ModelAttribute("order") Order order) {
        long id = order.getId();
        Order currentOrder = this.orderRepository.findById(id);
        List<OrderDetail> orderDetails = this.orderDetailService.getOrderDetailByOrder(currentOrder);
        for (OrderDetail orderDetail : orderDetails) {
            this.orderDetailRepository.delete(orderDetail);
        }
        this.orderRepository.delete(currentOrder);
        return "redirect:/admin/order";
    }
}
