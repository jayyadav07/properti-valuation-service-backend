package mu.mcb.property.evalution.entity;


import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    private String comments;
    
    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;
	
    @ManyToOne
    @JoinColumn(name = "faciliy_id",nullable = false)
    private Facility facility;
    
}
