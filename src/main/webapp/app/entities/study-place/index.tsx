import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StudyPlace from './study-place';
import StudyPlaceDetail from './study-place-detail';
import StudyPlaceUpdate from './study-place-update';
import StudyPlaceDeleteDialog from './study-place-delete-dialog';

const StudyPlaceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StudyPlace />} />
    <Route path="new" element={<StudyPlaceUpdate />} />
    <Route path=":id">
      <Route index element={<StudyPlaceDetail />} />
      <Route path="edit" element={<StudyPlaceUpdate />} />
      <Route path="delete" element={<StudyPlaceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StudyPlaceRoutes;
