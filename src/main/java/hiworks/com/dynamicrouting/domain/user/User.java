package hiworks.com.dynamicrouting.domain.user;



import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private int officeNo;

    private int officeAccountNo;

    private String name;

}
