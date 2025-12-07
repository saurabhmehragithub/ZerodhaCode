package com.saurabhtech.kiteconnect;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "zerodhaholdings")
public class HoldingEntity implements Serializable { 
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tradingSymbol;
    private Integer quantity;
    private Double lastPrice;

    // Add other fields as needed

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTradingSymbol() { return tradingSymbol; }
    public void setTradingSymbol(String tradingSymbol) { this.tradingSymbol = tradingSymbol; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getLastPrice() { return lastPrice; }
    public void setLastPrice(Double lastPrice) { this.lastPrice = lastPrice; }

    @Override
    public String toString() {
    return "HoldingEntity{" +
            "id=" + id +
            ", tradingSymbol='" + tradingSymbol + '\'' +
            ", quantity=" + quantity +
            ", lastPrice=" + lastPrice +
            '}';
}

}