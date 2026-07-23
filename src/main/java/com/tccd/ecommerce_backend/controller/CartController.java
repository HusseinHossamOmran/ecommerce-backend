package com.tccd.ecommerce_backend.controller;
import com.tccd.ecommerce_backend.dto.CartItemResponse;
import com.tccd.ecommerce_backend.dto.CartResponse;
import com.tccd.ecommerce_backend.model.Cart;
import com.tccd.ecommerce_backend.model.CartItem;
import com.tccd.ecommerce_backend.model.User;
import com.tccd.ecommerce_backend.repository.CartItemRepository;
import com.tccd.ecommerce_backend.repository.CartRepository;
import com.tccd.ecommerce_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController
{
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    private Long getCurrentUserId()
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.parseLong(userDetails.getUsername());
    }
    @PostMapping
    public ResponseEntity<CartResponse> createCart()
    {
        Long userId =getCurrentUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart existingCart = cartRepository.findByUserId(userId).orElse(null);
        if(existingCart != null)
        {
            return ResponseEntity.ok(buildCartResponse(existingCart));
        }
        Cart cart = new Cart();
        cart.setUser(user);
        Cart saved = cartRepository.save(cart);
        return ResponseEntity.ok(buildCartResponse(saved));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCart()
    {
        Long userId = getCurrentUserId();
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartItem> items = cartItemRepository.findByCartId(cart.getId());
        cartItemRepository.deleteAll(items);
        cartRepository.delete(cart);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable Long cartId)
    {
        Long userId = getCurrentUserId();
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        if (!cart.getUser().getId().equals(userId)) return ResponseEntity.status(403).body(null);
        List<CartItemResponse> items = cartItemRepository.findByCartId(cartId).stream().map(item -> new CartItemResponse(item.getId(),item.getProduct().getId(), item.getProduct().getName(), item.getQuantity(), item.getProduct().getPrice())).toList();
        return ResponseEntity.ok(items);

    }

    private CartResponse buildCartResponse(Cart cart) {
        List<CartItemResponse> items = cartItemRepository.findByCartId(cart.getId()).stream().map(item -> new CartItemResponse(item.getId(),item.getProduct().getId(), item.getProduct().getName(), item.getQuantity(), item.getProduct().getPrice())).toList();
        return new CartResponse(cart.getId(), cart.getUser().getId(), items);
    }


}
