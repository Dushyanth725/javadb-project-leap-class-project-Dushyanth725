package com.lostfound.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "item_logs")
@Data
public class ItemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    
    @Column(nullable = false, length = 50)
    private String action; // REPORTED, UPDATED, RECOVERED, CLAIMED
    
    @ManyToOne
    @JoinColumn(name = "performed_by")
    private User performedBy;
    
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @Column(length = 255)
    private String remarks;
}