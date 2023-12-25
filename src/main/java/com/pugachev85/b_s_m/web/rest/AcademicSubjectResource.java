package com.pugachev85.b_s_m.web.rest;

import com.pugachev85.b_s_m.domain.AcademicSubject;
import com.pugachev85.b_s_m.repository.AcademicSubjectRepository;
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
 * REST controller for managing {@link com.pugachev85.b_s_m.domain.AcademicSubject}.
 */
@RestController
@RequestMapping("/api/academic-subjects")
@Transactional
public class AcademicSubjectResource {

    private final Logger log = LoggerFactory.getLogger(AcademicSubjectResource.class);

    private static final String ENTITY_NAME = "academicSubject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcademicSubjectRepository academicSubjectRepository;

    public AcademicSubjectResource(AcademicSubjectRepository academicSubjectRepository) {
        this.academicSubjectRepository = academicSubjectRepository;
    }

    /**
     * {@code POST  /academic-subjects} : Create a new academicSubject.
     *
     * @param academicSubject the academicSubject to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new academicSubject, or with status {@code 400 (Bad Request)} if the academicSubject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AcademicSubject> createAcademicSubject(@Valid @RequestBody AcademicSubject academicSubject)
        throws URISyntaxException {
        log.debug("REST request to save AcademicSubject : {}", academicSubject);
        if (academicSubject.getId() != null) {
            throw new BadRequestAlertException("A new academicSubject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcademicSubject result = academicSubjectRepository.save(academicSubject);
        return ResponseEntity
            .created(new URI("/api/academic-subjects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /academic-subjects/:id} : Updates an existing academicSubject.
     *
     * @param id the id of the academicSubject to save.
     * @param academicSubject the academicSubject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academicSubject,
     * or with status {@code 400 (Bad Request)} if the academicSubject is not valid,
     * or with status {@code 500 (Internal Server Error)} if the academicSubject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AcademicSubject> updateAcademicSubject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AcademicSubject academicSubject
    ) throws URISyntaxException {
        log.debug("REST request to update AcademicSubject : {}, {}", id, academicSubject);
        if (academicSubject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academicSubject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academicSubjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AcademicSubject result = academicSubjectRepository.save(academicSubject);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academicSubject.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /academic-subjects/:id} : Partial updates given fields of an existing academicSubject, field will ignore if it is null
     *
     * @param id the id of the academicSubject to save.
     * @param academicSubject the academicSubject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academicSubject,
     * or with status {@code 400 (Bad Request)} if the academicSubject is not valid,
     * or with status {@code 404 (Not Found)} if the academicSubject is not found,
     * or with status {@code 500 (Internal Server Error)} if the academicSubject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcademicSubject> partialUpdateAcademicSubject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AcademicSubject academicSubject
    ) throws URISyntaxException {
        log.debug("REST request to partial update AcademicSubject partially : {}, {}", id, academicSubject);
        if (academicSubject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academicSubject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academicSubjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcademicSubject> result = academicSubjectRepository
            .findById(academicSubject.getId())
            .map(existingAcademicSubject -> {
                if (academicSubject.getTitle() != null) {
                    existingAcademicSubject.setTitle(academicSubject.getTitle());
                }
                if (academicSubject.getHours() != null) {
                    existingAcademicSubject.setHours(academicSubject.getHours());
                }

                return existingAcademicSubject;
            })
            .map(academicSubjectRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academicSubject.getId().toString())
        );
    }

    /**
     * {@code GET  /academic-subjects} : get all the academicSubjects.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of academicSubjects in body.
     */
    @GetMapping("")
    public List<AcademicSubject> getAllAcademicSubjects() {
        log.debug("REST request to get all AcademicSubjects");
        return academicSubjectRepository.findAll();
    }

    /**
     * {@code GET  /academic-subjects/:id} : get the "id" academicSubject.
     *
     * @param id the id of the academicSubject to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the academicSubject, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AcademicSubject> getAcademicSubject(@PathVariable("id") Long id) {
        log.debug("REST request to get AcademicSubject : {}", id);
        Optional<AcademicSubject> academicSubject = academicSubjectRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(academicSubject);
    }

    /**
     * {@code DELETE  /academic-subjects/:id} : delete the "id" academicSubject.
     *
     * @param id the id of the academicSubject to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcademicSubject(@PathVariable("id") Long id) {
        log.debug("REST request to delete AcademicSubject : {}", id);
        academicSubjectRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
