package br.com.ipgest.ipgest.rest;

import br.com.ipgest.ipgest.model.GroupDTO;
import br.com.ipgest.ipgest.service.GroupService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupResource {

    private final GroupService groupService;

    public GroupResource(final GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAllGroups() {
        return ResponseEntity.ok(groupService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> getGroup(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(groupService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createGroup(@RequestBody @Valid final GroupDTO groupDTO) {
        final Long createdId = groupService.create(groupDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateGroup(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final GroupDTO groupDTO) {
        groupService.update(id, groupDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable(name = "id") final Long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
