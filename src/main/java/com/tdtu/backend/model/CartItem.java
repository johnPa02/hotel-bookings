package com.tdtu.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceModel serviceModel;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public Double getTotalPrice() {
        if (serviceModel != null) {
            return serviceModel.getPrice() * quantity;
        } else if (room != null) {
            return room.getPrice() * quantity;
        }
        return 0.0;
    }
    public CartItem(ServiceModel serviceModel, int quantity) {
        this.serviceModel = serviceModel;
        this.quantity = quantity;
        this.room = null;
    }

    public CartItem(Room room, int quantity) {
        this.room = room;
        this.quantity = quantity;
        this.serviceModel = null;
    }
}
