package ru.vtb.javaCourse.Task5.CorporateSettlementInstance;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vtb.javaCourse.Task5.Service.InstanceService;
import ru.vtb.javaCourse.Task5.Service.InstanceServiceImpl;

@RestController
@RequestMapping("/corporate-settlement-instance")
public class InstanceController {

    private final InstanceService instanceService;

    @Autowired
    public InstanceController(InstanceServiceImpl instanceService) {
        this.instanceService = instanceService;
    }

    @PostMapping("/create")
    public InstanceResponse Create(@Valid @RequestBody InstanceRequest instanceRequest, Model model){
        return instanceService.performRequest(instanceRequest);
    }
}
