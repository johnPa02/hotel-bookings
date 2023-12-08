package com.tdtu.backend.service;

import com.tdtu.backend.model.*;
import com.tdtu.backend.repository.CartItemRepository;
import com.tdtu.backend.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }
    public void addItemToCart(User user, ServiceModel service) {
        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));
        CartItem newItem = new CartItem(service, 1);
        cart.getItems().add(newItem);
        cartRepository.save(cart);
    }

    public void addItemToCart(User user, Room room) {
        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));
        CartItem newItem = new CartItem(room, 1);
        cart.getItems().add(newItem);
        cartRepository.save(cart);
    }


    public void updateCartItem(User user, Long itemId, int quantity) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user"));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found with ID: " + itemId));

        if (cart.getItems().contains(cartItem)) {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            throw new IllegalArgumentException("Cart item does not belong to the user's cart");
        }
    }

    public void removeItemFromCart(Long itemId) {
        cartItemRepository.findById(itemId).ifPresent(cartItemRepository::delete);
    }
    public Optional<Cart> getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }
    public void clearCart(Cart cart) {
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        } else {
            throw new IllegalArgumentException("Cart cannot be null");
        }
    }
}
