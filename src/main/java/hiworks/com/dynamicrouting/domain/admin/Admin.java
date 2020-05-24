package hiworks.com.dynamicrouting.domain.admin;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "admin")
@Getter
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String subDbIpPort;

    private String dbPartitionNo;

    private String domain;

    private String mailPartitionNo;
}
