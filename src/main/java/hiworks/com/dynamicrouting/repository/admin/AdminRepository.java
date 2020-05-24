package hiworks.com.dynamicrouting.repository.admin;

import hiworks.com.dynamicrouting.domain.admin.Admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface AdminRepository extends JpaRepository<Admin,Long> {
}
