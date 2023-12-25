import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './student.reducer';

export const StudentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const studentEntity = useAppSelector(state => state.student.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="studentDetailsHeading">
          <Translate contentKey="basicStudentsManagmentApp.student.detail.title">Student</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{studentEntity.id}</dd>
          <dt>
            <span id="alphabetBookNumber">
              <Translate contentKey="basicStudentsManagmentApp.student.alphabetBookNumber">Alphabet Book Number</Translate>
            </span>
          </dt>
          <dd>{studentEntity.alphabetBookNumber}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="basicStudentsManagmentApp.student.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.firstName}</dd>
          <dt>
            <span id="secondName">
              <Translate contentKey="basicStudentsManagmentApp.student.secondName">Second Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.secondName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="basicStudentsManagmentApp.student.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{studentEntity.lastName}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="basicStudentsManagmentApp.student.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {studentEntity.birthDate ? <TextFormat value={studentEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="basicStudentsManagmentApp.student.group">Group</Translate>
          </dt>
          <dd>{studentEntity.group ? studentEntity.group.title : ''}</dd>
          <dt>
            <Translate contentKey="basicStudentsManagmentApp.student.order">Order</Translate>
          </dt>
          <dd>
            {studentEntity.orders
              ? studentEntity.orders.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.date}</a>
                    {studentEntity.orders && i === studentEntity.orders.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/student" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/student/${studentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StudentDetail;
