package vn.hoidanit.laptopshop.service;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.CartDetail;
import vn.hoidanit.laptopshop.repository.CartDetailRepository;

@Service
public class CartDetailService {
    private final CartDetailRepository cartDetailRepository;

    public CartDetailService(CartDetailRepository cartDetailRepository) {
        this.cartDetailRepository = cartDetailRepository;
    }

    public CartDetail getCartDetailById(long id) {
        return this.cartDetailRepository.findById(id);
    }

    public CartDetail deleteCartDetailById(long id) {
        return this.cartDetailRepository.deleteById(id);
    }

    public CartDetail savCartDetail(CartDetail cartDetail) {
        return this.cartDetailRepository.save(cartDetail);
    }
}
