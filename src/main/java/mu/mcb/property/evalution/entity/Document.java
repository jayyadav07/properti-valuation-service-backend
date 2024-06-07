package mu.mcb.property.evalution.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="document")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "faciliy_id",nullable = false)
    private Facility facility;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "document_type_id", referencedColumnName = "id")
    private DocumentType type;
  
    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;
    
    @Lob
    @Column(name="document", length=100000)
    private byte[] document;
    
    private String fileName;
    
    private Long fileSize;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate;
}
