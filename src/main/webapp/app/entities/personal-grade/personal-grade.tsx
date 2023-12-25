import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './personal-grade.reducer';

export const PersonalGrade = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const personalGradeList = useAppSelector(state => state.personalGrade.entities);
  const loading = useAppSelector(state => state.personalGrade.loading);

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
      <h2 id="personal-grade-heading" data-cy="PersonalGradeHeading">
        <Translate contentKey="basicStudentsManagmentApp.personalGrade.home.title">Personal Grades</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="basicStudentsManagmentApp.personalGrade.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/personal-grade/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="basicStudentsManagmentApp.personalGrade.home.createLabel">Create new Personal Grade</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {personalGradeList && personalGradeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="basicStudentsManagmentApp.personalGrade.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('grade')}>
                  <Translate contentKey="basicStudentsManagmentApp.personalGrade.grade">Grade</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('grade')} />
                </th>
                <th>
                  <Translate contentKey="basicStudentsManagmentApp.personalGrade.academicSubject">Academic Subject</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="basicStudentsManagmentApp.personalGrade.student">Student</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {personalGradeList.map((personalGrade, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/personal-grade/${personalGrade.id}`} color="link" size="sm">
                      {personalGrade.id}
                    </Button>
                  </td>
                  <td>{personalGrade.grade}</td>
                  <td>
                    {personalGrade.academicSubject ? (
                      <Link to={`/academic-subject/${personalGrade.academicSubject.id}`}>{personalGrade.academicSubject.title}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {personalGrade.students
                      ? personalGrade.students.map((val, j) => (
                          <span key={j}>
                            <Link to={`/student/${val.id}`}>{val.alphabetBookNumber}</Link>
                            {j === personalGrade.students.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/personal-grade/${personalGrade.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/personal-grade/${personalGrade.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/personal-grade/${personalGrade.id}/delete`)}
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
              <Translate contentKey="basicStudentsManagmentApp.personalGrade.home.notFound">No Personal Grades found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PersonalGrade;
