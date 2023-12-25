package com.pugachev85.b_s_m.web.rest;

import com.pugachev85.b_s_m.domain.EducationalProgram;
import com.pugachev85.b_s_m.repository.EducationalProgramRepository;
import com.pugachev85.b_s_m.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pugachev85.b_s_m.domain.EducationalProgram}.
 */
@RestController
@RequestMapping("/api/educational-programs")
@Transactional
public class EducationalProgramResource {

    private final Logger log = LoggerFactory.getLogger(EducationalProgramResource.class);

    private static final String ENTITY_NAME = "educationalProgram";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EducationalProgramRepository educationalProgramRepository;

    public EducationalProgramResource(EducationalProgramRepository educationalProgramRepository) {
        this.educationalProgramRepository = educationalProgramRepository;
    }

    /**
     * {@code POST  /educational-programs} : Create a new educationalProgram.
     *
     * @param educationalProgram the educationalProgram to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new educationalProgram, or with status {@code 400 (Bad Request)} if the educationalProgram has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EducationalProgram> createEducationalProgram(@Valid @RequestBody EducationalProgram educationalProgram)
        throws URISyntaxException {
        log.debug("REST request to save EducationalProgram : {}", educationalProgram);
        if (educationalProgram.getId() != null) {
            throw new BadRequestAlertException("A new educationalProgram cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EducationalProgram result = educationalProgramRepository.save(educationalProgram);
        return ResponseEntity
            .created(new URI("/api/educational-programs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /educational-programs/:id} : Updates an existing educationalProgram.
     *
     * @param id the id of the educationalProgram to save.
     * @param educationalProgram the educationalProgram to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationalProgram,
     * or with status {@code 400 (Bad Request)} if the educationalProgram is not valid,
     * or with status {@code 500 (Internal Server Error)} if the educationalProgram couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EducationalProgram> updateEducationalProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EducationalProgram educationalProgram
    ) throws URISyntaxException {
        log.debug("REST request to update EducationalProgram : {}, {}", id, educationalProgram);
        if (educationalProgram.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationalProgram.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationalProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EducationalProgram result = educationalProgramRepository.save(educationalProgram);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, educationalProgram.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /educational-programs/:id} : Partial updates given fields of an existing educationalProgram, field will ignore if it is null
     *
     * @param id the id of the educationalProgram to save.
     * @param educationalProgram the educationalProgram to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated educationalProgram,
     * or with status {@code 400 (Bad Request)} if the educationalProgram is not valid,
     * or with status {@code 404 (Not Found)} if the educationalProgram is not found,
     * or with status {@code 500 (Internal Server Error)} if the educationalProgram couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EducationalProgram> partialUpdateEducationalProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EducationalProgram educationalProgram
    ) throws URISyntaxException {
        log.debug("REST request to partial update EducationalProgram partially : {}, {}", id, educationalProgram);
        if (educationalProgram.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, educationalProgram.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationalProgramRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EducationalProgram> result = educationalProgramRepository
            .findById(educationalProgram.getId())
            .map(existingEducationalProgram -> {
                if (educationalProgram.getTitle() != null) {
                    existingEducationalProgram.setTitle(educationalProgram.getTitle());
                }
                if (educationalProgram.getMonthLength() != null) {
                    existingEducationalProgram.setMonthLength(educationalProgram.getMonthLength());
                }

                return existingEducationalProgram;
            })
            .map(educationalProgramRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, educationalProgram.getId().toString())
        );
    }

    /**
     * {@code GET  /educational-programs} : get all the educationalPrograms.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educationalPrograms in body.
     */
    @GetMapping("")
    public List<EducationalProgram> getAllEducationalPrograms(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all EducationalPrograms");
        if (eagerload) {
            return educationalProgramRepository.findAllWithEagerRelationships();
        } else {
            return educationalProgramRepository.findAll();
        }
    }

    /**
     * {@code GET  /educational-programs/:id} : get the "id" educationalProgram.
     *
     * @param id the id of the educationalProgram to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the educationalProgram, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EducationalProgram> getEducationalProgram(@PathVariable("id") Long id) {
        log.debug("REST request to get EducationalProgram : {}", id);
        Optional<EducationalProgram> educationalProgram = educationalProgramRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(educationalProgram);
    }

    /**
     * {@code DELETE  /educational-programs/:id} : delete the "id" educationalProgram.
     *
     * @param id the id of the educationalProgram to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationalProgram(@PathVariable("id") Long id) {
        log.debug("REST request to delete EducationalProgram : {}", id);
        educationalProgramRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
