package cc.rockbot.dds.controller;

import cc.rockbot.dds.entity.Organization;
import cc.rockbot.dds.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        return ResponseEntity.ok(organizationService.createOrganization(organization));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable String id) {
        return organizationService.getOrganizationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orgid/{orgId}")
    public ResponseEntity<Organization> getOrganizationByOrgId(@PathVariable String orgId) {
        Organization organization = organizationService.getOrganizationByOrgId(orgId);
        return organization != null ? ResponseEntity.ok(organization) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable String id, @RequestBody Organization organization) {
        organization.setId(id);
        return ResponseEntity.ok(organizationService.updateOrganization(organization));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable String id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok().build();
    }
} 