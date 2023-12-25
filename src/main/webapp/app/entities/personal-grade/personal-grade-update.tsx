import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAcademicSubject } from 'app/shared/model/academic-subject.model';
import { getEntities as getAcademicSubjects } from 'app/entities/academic-subject/academic-subject.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { getEntities as getStudents } from 'app/entities/student/student.reducer';
import { IPersonalGrade } from 'app/shared/model/personal-grade.model';
import { getEntity, updateEntity, createEntity, reset } from './personal-grade.reducer';

export const PersonalGradeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const academicSubjects = useAppSelector(state => state.academicSubject.entities);
  const students = useAppSelector(state => state.student.entities);
  const personalGradeEntity = useAppSelector(state => state.personalGrade.entity);
  const loading = useAppSelector(state => state.personalGrade.loading);
  const updating = useAppSelector(state => state.personalGrade.updating);
  const updateSuccess = useAppSelector(state => state.personalGrade.updateSuccess);

  const handleClose = () => {
    navigate('/personal-grade');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAcademicSubjects({}));
    dispatch(getStudents({}));
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
    if (values.grade !== undefined && typeof values.grade !== 'number') {
      values.grade = Number(values.grade);
    }

    const entity = {
      ...personalGradeEntity,
      ...values,
      students: mapIdList(values.students),
      academicSubject: academicSubjects.find(it => it.id.toString() === values.academicSubject.toString()),
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
          ...personalGradeEntity,
          academicSubject: personalGradeEntity?.academicSubject?.id,
          students: personalGradeEntity?.students?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="basicStudentsManagmentApp.personalGrade.home.createOrEditLabel" data-cy="PersonalGradeCreateUpdateHeading">
            <Translate contentKey="basicStudentsManagmentApp.personalGrade.home.createOrEditLabel">
              Create or edit a PersonalGrade
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
                  id="personal-grade-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('basicStudentsManagmentApp.personalGrade.grade')}
                id="personal-grade-grade"
                name="grade"
                data-cy="grade"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  min: { value: 1, message: translate('entity.validation.min', { min: 1 }) },
                  max: { value: 5, message: translate('entity.validation.max', { max: 5 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="personal-grade-academicSubject"
                name="academicSubject"
                data-cy="academicSubject"
                label={translate('basicStudentsManagmentApp.personalGrade.academicSubject')}
                type="select"
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
              <ValidatedField
                label={translate('basicStudentsManagmentApp.personalGrade.student')}
                id="personal-grade-student"
                data-cy="student"
                type="select"
                multiple
                name="students"
              >
                <option value="" key="0" />
                {students
                  ? students.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.alphabetBookNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/personal-grade" replace color="info">
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

export default PersonalGradeUpdate;
