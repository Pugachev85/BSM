import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAcademicYear } from 'app/shared/model/academic-year.model';
import { getEntities as getAcademicYears } from 'app/entities/academic-year/academic-year.reducer';
import { IAcademicSubject } from 'app/shared/model/academic-subject.model';
import { getEntities as getAcademicSubjects } from 'app/entities/academic-subject/academic-subject.reducer';
import { IEducationalProgram } from 'app/shared/model/educational-program.model';
import { getEntity, updateEntity, createEntity, reset } from './educational-program.reducer';

export const EducationalProgramUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const academicYears = useAppSelector(state => state.academicYear.entities);
  const academicSubjects = useAppSelector(state => state.academicSubject.entities);
  const educationalProgramEntity = useAppSelector(state => state.educationalProgram.entity);
  const loading = useAppSelector(state => state.educationalProgram.loading);
  const updating = useAppSelector(state => state.educationalProgram.updating);
  const updateSuccess = useAppSelector(state => state.educationalProgram.updateSuccess);

  const handleClose = () => {
    navigate('/educational-program');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAcademicYears({}));
    dispatch(getAcademicSubjects({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.monthLength !== undefined && typeof values.monthLength !== 'number') {
      values.monthLength = Number(values.monthLength);
    }

    const entity = {
      ...educationalProgramEntity,
      ...values,
      academicSubjects: mapIdList(values.academicSubjects),
      academicYear: academicYears.find(it => it.id.toString() === values.academicYear.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...educationalProgramEntity,
          academicYear: educationalProgramEntity?.academicYear?.id,
          academicSubjects: educationalProgramEntity?.academicSubjects?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="basicStudentsManagmentApp.educationalProgram.home.createOrEditLabel" data-cy="EducationalProgramCreateUpdateHeading">
            <Translate contentKey="basicStudentsManagmentApp.educationalProgram.home.createOrEditLabel">
              Create or edit a EducationalProgram
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="educational-program-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('basicStudentsManagmentApp.educationalProgram.title')}
                id="educational-program-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('basicStudentsManagmentApp.educationalProgram.monthLength')}
                id="educational-program-monthLength"
                name="monthLength"
                data-cy="monthLength"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 0.5, message: translate('entity.validation.min', { min: 0.5 }) },
                  max: { value: 60.0, message: translate('entity.validation.max', { max: 60.0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="educational-program-academicYear"
                name="academicYear"
                data-cy="academicYear"
                label={translate('basicStudentsManagmentApp.educationalProgram.academicYear')}
                type="select"
              >
                <option value="" key="0" />
                {academicYears
                  ? academicYears.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('basicStudentsManagmentApp.educationalProgram.academicSubject')}
                id="educational-program-academicSubject"
                data-cy="academicSubject"
                type="select"
                multiple
                name="academicSubjects"
              >
                <option value="" key="0" />
                {academicSubjects
                  ? academicSubjects.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/educational-program" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default EducationalProgramUpdate;
