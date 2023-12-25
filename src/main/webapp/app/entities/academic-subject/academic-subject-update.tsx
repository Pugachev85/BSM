import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEducationalProgram } from 'app/shared/model/educational-program.model';
import { getEntities as getEducationalPrograms } from 'app/entities/educational-program/educational-program.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { getEntities as getEmployees } from 'app/entities/employee/employee.reducer';
import { IAcademicSubject } from 'app/shared/model/academic-subject.model';
import { getEntity, updateEntity, createEntity, reset } from './academic-subject.reducer';

export const AcademicSubjectUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const educationalPrograms = useAppSelector(state => state.educationalProgram.entities);
  const employees = useAppSelector(state => state.employee.entities);
  const academicSubjectEntity = useAppSelector(state => state.academicSubject.entity);
  const loading = useAppSelector(state => state.academicSubject.loading);
  const updating = useAppSelector(state => state.academicSubject.updating);
  const updateSuccess = useAppSelector(state => state.academicSubject.updateSuccess);

  const handleClose = () => {
    navigate('/academic-subject');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEducationalPrograms({}));
    dispatch(getEmployees({}));
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
    if (values.hours !== undefined && typeof values.hours !== 'number') {
      values.hours = Number(values.hours);
    }

    const entity = {
      ...academicSubjectEntity,
      ...values,
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
          ...academicSubjectEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="basicStudentsManagmentApp.academicSubject.home.createOrEditLabel" data-cy="AcademicSubjectCreateUpdateHeading">
            <Translate contentKey="basicStudentsManagmentApp.academicSubject.home.createOrEditLabel">
              Create or edit a AcademicSubject
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
                  id="academic-subject-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('basicStudentsManagmentApp.academicSubject.title')}
                id="academic-subject-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('basicStudentsManagmentApp.academicSubject.hours')}
                id="academic-subject-hours"
                name="hours"
                data-cy="hours"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/academic-subject" replace color="info">
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

export default AcademicSubjectUpdate;
