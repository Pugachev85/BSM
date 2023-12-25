import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PersonalGrade from './personal-grade';
import PersonalGradeDetail from './personal-grade-detail';
import PersonalGradeUpdate from './personal-grade-update';
import PersonalGradeDeleteDialog from './personal-grade-delete-dialog';

const PersonalGradeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PersonalGrade />} />
    <Route path="new" element={<PersonalGradeUpdate />} />
    <Route path=":id">
      <Route index element={<PersonalGradeDetail />} />
      <Route path="edit" element={<PersonalGradeUpdate />} />
      <Route path="delete" element={<PersonalGradeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PersonalGradeRoutes;
