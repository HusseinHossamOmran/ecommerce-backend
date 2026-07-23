package com.tccd.ecommerce_backend.controller;
import com.tccd.ecommerce_backend.dto.CartItemRequest;
import com.tccd.ecommerce_backend.dto.CartItemResponse;
import com.tccd.ecommerce_backend.model.Cart;
import com.tccd.ecommerce_backend.model.CartItem;
import com.tccd.ecommerce_backend.model.Product;
import com.tccd.ecommerce_backend.repository.CartItemRepository;
import com.tccd.ecommerce_backend.repository.CartRepository;
import com.tccd.ecommerce_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cartItems")
public class CartItemController
{
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;
    private Long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Long.parseLong(userDetails.getUsername());
    }
    @PostMapping("/{cartId}")
    public ResponseEntity<CartItemResponse> addToCart(@PathVariable Long cartId, @RequestBody CartItemRequest cartItemRequest)
    {
        Long userId = getCurrentUserId();
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        if (!cart.getUser().getId().equals(userId)) return ResponseEntity.status(403).body(null);
        Product product = productRepository.findById(cartItemRequest.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cartId, cartItemRequest.getProductId()).orElse(null);
        CartItem item;
        if (existingItem != null)
        {
            existingItem.setQuantity(existingItem.getQuantity() + cartItemRequest.getQuantity());
            item = cartItemRepository.save(existingItem);
        }
        else
        {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(cartItemRequest.getQuantity());
            item = cartItemRepository.save(newItem);

        }
        return ResponseEntity.ok(new CartItemResponse(item.getId(), item.getProduct().getId(), item.getProduct().getName(), item.getQuantity(), item.getProduct().getPrice()));

    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> getCartItem(@PathVariable Long cartItemId)
    {
        Long userId = getCurrentUserId();
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getCart().getUser().getId().equals(userId)) return ResponseEntity.status(403).body(null);
        return ResponseEntity.ok(new CartItemResponse(item.getId(), item.getProduct().getId(), item.getProduct().getName(), item.getQuantity(), item.getProduct().getPrice()));

    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable Long cartItemId, @RequestBody CartItemRequest cartItemRequest)
    {
        Long userId = getCurrentUserId();
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!item.getCart().getUser().getId().equals(userId)) return ResponseEntity.status(403).body(null);
        if (cartItemRequest.getQuantity() != null) item.setQuantity(cartItemRequest.getQuantity());
        CartItem updated = cartItemRepository.save(item);
        return ResponseEntity.ok(new CartItemResponse(updated.getId(), updated.getProduct().getId(), updated.getProduct().getName(), updated.getQuantity(), updated.getProduct().getPrice()));
    }

    }



