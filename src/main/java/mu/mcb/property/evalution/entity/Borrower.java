package mu.mcb.property.evalution.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="borrower")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Borrower {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(nullable = false)
    private String name;

    @Size(max = 50)
    @Column(nullable = false)
    private String customerNumber;

    @Size(max = 20)
    @Column(nullable = false)
    private String contactNumber;

    @Size(max = 50)
    @Column(nullable = false)
    private String email;

    @Size(max = 200)
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isMainBorrower=Boolean.FALSE;
    
    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;
     
    @ManyToOne
    @JoinColumn(name="property_valuation",nullable = false , referencedColumnName = "id")
    private PropertyValuation propertyValuation;
    
    @ManyToOne
    @JoinColumn(name = "faciliy_id",nullable = false)
    private Facility facility;

}
