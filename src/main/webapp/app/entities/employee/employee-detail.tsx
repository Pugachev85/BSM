import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './employee.reducer';

export const EmployeeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const employeeEntity = useAppSelector(state => state.employee.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employeeDetailsHeading">
          <Translate contentKey="basicStudentsManagmentApp.employee.detail.title">Employee</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="basicStudentsManagmentApp.employee.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.firstName}</dd>
          <dt>
            <span id="secondName">
              <Translate contentKey="basicStudentsManagmentApp.employee.secondName">Second Name</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.secondName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="basicStudentsManagmentApp.employee.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.lastName}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="basicStudentsManagmentApp.employee.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {employeeEntity.birthDate ? <TextFormat value={employeeEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="jobTitle">
              <Translate contentKey="basicStudentsManagmentApp.employee.jobTitle">Job Title</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.jobTitle}</dd>
          <dt>
            <Translate contentKey="basicStudentsManagmentApp.employee.academicSubject">Academic Subject</Translate>
          </dt>
          <dd>
            {employeeEntity.academicSubjects
              ? employeeEntity.academicSubjects.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.title}</a>
                    {employeeEntity.academicSubjects && i === employeeEntity.academicSubjects.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/employee" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employee/${employeeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmployeeDetail;
