import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGroup } from 'app/shared/model/group.model';
import { getEntities as getGroups } from 'app/entities/group/group.reducer';
import { IOrder } from 'app/shared/model/order.model';
import { getEntities as getOrders } from 'app/entities/order/order.reducer';
import { IPersonalGrade } from 'app/shared/model/personal-grade.model';
import { getEntities as getPersonalGrades } from 'app/entities/personal-grade/personal-grade.reducer';
import { IStudent } from 'app/shared/model/student.model';
import { getEntity, updateEntity, createEntity, reset } from './student.reducer';

export const StudentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const groups = useAppSelector(state => state.group.entities);
  const orders = useAppSelector(state => state.order.entities);
  const personalGrades = useAppSelector(state => state.personalGrade.entities);
  const studentEntity = useAppSelector(state => state.student.entity);
  const loading = useAppSelector(state => state.student.loading);
  const updating = useAppSelector(state => state.student.updating);
  const updateSuccess = useAppSelector(state => state.student.updateSuccess);

  const handleClose = () => {
    navigate('/student');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGroups({}));
    dispatch(getOrders({}));
    dispatch(getPersonalGrades({}));
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
    if (values.alphabetBookNumber !== undefined && typeof values.alphabetBookNumber !== 'number') {
      values.alphabetBookNumber = Number(values.alphabetBookNumber);
    }

    const entity = {
      ...studentEntity,
      ...values,
      orders: mapIdList(values.orders),
      group: groups.find(it => it.id.toString() === values.group.toString()),
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
          ...studentEntity,
          group: studentEntity?.group?.id,
          orders: studentEntity?.orders?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="basicStudentsManagmentApp.student.home.createOrEditLabel" data-cy="StudentCreateUpdateHeading">
            <Translate contentKey="basicStudentsManagmentApp.student.home.createOrEditLabel">Create or edit a Student</Translate>
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
                  id="student-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('basicStudentsManagmentApp.student.alphabetBookNumber')}
                id="student-alphabetBookNumber"
                name="alphabetBookNumber"
                data-cy="alphabetBookNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('basicStudentsManagmentApp.student.firstName')}
                id="student-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('basicStudentsManagmentApp.student.secondName')}
                id="student-secondName"
                name="secondName"
                data-cy="secondName"
                type="text"
              />
              <ValidatedField
                label={translate('basicStudentsManagmentApp.student.lastName')}
                id="student-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('basicStudentsManagmentApp.student.birthDate')}
                id="student-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="student-group"
                name="group"
                data-cy="group"
                label={translate('basicStudentsManagmentApp.student.group')}
                type="select"
              >
                <option value="" key="0" />
                {groups
                  ? groups.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('basicStudentsManagmentApp.student.order')}
                id="student-order"
                data-cy="order"
                type="select"
                multiple
                name="orders"
              >
                <option value="" key="0" />
                {orders
                  ? orders.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.date}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/student" replace color="info">
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

export default StudentUpdate;
