package hiworks.com.dynamicrouting.controller;

import hiworks.com.dynamicrouting.domain.admin.Admin;


import hiworks.com.dynamicrouting.domain.user.User;
import hiworks.com.dynamicrouting.multiTenancy.SubDBmanager;
import hiworks.com.dynamicrouting.multiTenancy.ThreadLocalStorage;
import hiworks.com.dynamicrouting.repository.admin.AdminRepository;
import hiworks.com.dynamicrouting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RoutingController {



    private final AdminRepository adminRepository;

    private final SubDBmanager subDBmanager;

    private final UserRepository userRepository;

    @GetMapping("/office-no/{officeNo}/office-account-no/{officeAccountNo}")
    public User testGet(@PathVariable("officeNo") Long officeNo,@PathVariable("officeAccountNo") Long officeAccountNo) throws SQLException {
        Admin admin = adminRepository.findById(officeNo).get();
        subDBmanager.setCurrentTenant(admin.getSubDbIpPort()+"-user"+admin.getDbPartitionNo(),admin.getSubDbIpPort(),admin.getDbPartitionNo());
        User user = userRepository.findById(officeAccountNo).get();
        ThreadLocalStorage.clear();
        return user;

    }

}
