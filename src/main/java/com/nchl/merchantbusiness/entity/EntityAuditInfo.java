package com.nchl.merchantbusiness.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class EntityAuditInfo {

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "edited_at")
    private LocalDateTime editedAt;

    @Column(name = "edited_by")
    private String editedBy;

    @Override
    public String toString() {
        return "{" +
                "approvedAt=" + approvedAt +
                ", approvedBy='" + approvedBy + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", editedAt=" + editedAt +
                ", editedBy='" + editedBy + '\'' +
                '}';
    }
}