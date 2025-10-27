package com.shubham.portfolio.model;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author SinghShubham
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "projects")
public class Project {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

//    @Lob // Specifies a Large Object, for long text descriptions
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    // e.g., "PROFESSIONAL" or "PUBLIC_DEMO"
    @Column(nullable = false)
    private String projectType;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> technologiesUsed;

    private String githubLink; // Nullable (for your professional work)
    
    private String liveDemoLink; // Nullable
}
