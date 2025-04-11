package cc.rockbot.dds.controller;

import cc.rockbot.dds.model.OrganizationDO;
import cc.rockbot.dds.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ResponseEntity<OrganizationDO> createOrganization(@RequestBody OrganizationDO organization) {
        return ResponseEntity.ok(organizationService.createOrganization(organization));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDO> getOrganizationById(@PathVariable String id) {
        return organizationService.getOrganizationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orgid/{orgId}")
    public ResponseEntity<OrganizationDO> getOrganizationByOrgId(@PathVariable String orgId) {
        return organizationService.getOrganizationById(orgId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrganizationDO>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDO> updateOrganization(@PathVariable String id, @RequestBody OrganizationDO organization) {
        organization.setId(id);
        return ResponseEntity.ok(organizationService.updateOrganization(organization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok().build();
    }
} 