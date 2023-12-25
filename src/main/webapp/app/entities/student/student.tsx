import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './student.reducer';

export const Student = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const studentList = useAppSelector(state => state.student.entities);
  const loading = useAppSelector(state => state.student.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="student-heading" data-cy="StudentHeading">
        <Translate contentKey="basicStudentsManagmentApp.student.home.title">Students</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="basicStudentsManagmentApp.student.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/student/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="basicStudentsManagmentApp.student.home.createLabel">Create new Student</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {studentList && studentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="basicStudentsManagmentApp.student.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('alphabetBookNumber')}>
                  <Translate contentKey="basicStudentsManagmentApp.student.alphabetBookNumber">Alphabet Book Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('alphabetBookNumber')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  <Translate contentKey="basicStudentsManagmentApp.student.firstName">First Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('secondName')}>
                  <Translate contentKey="basicStudentsManagmentApp.student.secondName">Second Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('secondName')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  <Translate contentKey="basicStudentsManagmentApp.student.lastName">Last Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('birthDate')}>
                  <Translate contentKey="basicStudentsManagmentApp.student.birthDate">Birth Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('birthDate')} />
                </th>
                <th>
                  <Translate contentKey="basicStudentsManagmentApp.student.group">Group</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="basicStudentsManagmentApp.student.order">Order</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {studentList.map((student, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/student/${student.id}`} color="link" size="sm">
                      {student.id}
                    </Button>
                  </td>
                  <td>{student.alphabetBookNumber}</td>
                  <td>{student.firstName}</td>
                  <td>{student.secondName}</td>
                  <td>{student.lastName}</td>
                  <td>{student.birthDate ? <TextFormat type="date" value={student.birthDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{student.group ? <Link to={`/group/${student.group.id}`}>{student.group.title}</Link> : ''}</td>
                  <td>
                    {student.orders
                      ? student.orders.map((val, j) => (
                          <span key={j}>
                            <Link to={`/order/${val.id}`}>{val.date}</Link>
                            {j === student.orders.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/student/${student.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/student/${student.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/student/${student.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="basicStudentsManagmentApp.student.home.notFound">No Students found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Student;
