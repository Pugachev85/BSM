import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/academic-year">
        <Translate contentKey="global.menu.entities.academicYear" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/study-place">
        <Translate contentKey="global.menu.entities.studyPlace" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/group">
        <Translate contentKey="global.menu.entities.group" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/educational-program">
        <Translate contentKey="global.menu.entities.educationalProgram" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/academic-subject">
        <Translate contentKey="global.menu.entities.academicSubject" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/student">
        <Translate contentKey="global.menu.entities.student" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/employee">
        <Translate contentKey="global.menu.entities.employee" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/order">
        <Translate contentKey="global.menu.entities.order" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/personal-grade">
        <Translate contentKey="global.menu.entities.personalGrade" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
