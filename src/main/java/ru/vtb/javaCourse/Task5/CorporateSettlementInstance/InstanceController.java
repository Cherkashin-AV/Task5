package ru.vtb.javaCourse.Task5.CorporateSettlementInstance;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;

@RestController
@RequestMapping("/corporate-settlement-instance")
public class InstanceController {

    private final InstanceService instanceService;

    @Autowired
    public InstanceController(InstanceService instanceService) {
        this.instanceService = instanceService;
    }

    @PostMapping("/create")
    public InstanceResponse Create(@Valid @RequestBody InstanceRequest instanceRequest, Model model){
        return instanceService.performRequest(instanceRequest);
    }
    //return ResponseEntity.ok().body(InstanceResponse.createAccountResponceBody("123", Set.of("456"), Set.of("789")));

}
