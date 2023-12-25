import { IAcademicYear } from 'app/shared/model/academic-year.model';
import { IAcademicSubject } from 'app/shared/model/academic-subject.model';

export interface IEducationalProgram {
  id?: number;
  title?: string;
  monthLength?: number;
  academicYear?: IAcademicYear | null;
  academicSubjects?: IAcademicSubject[] | null;
}

export const defaultValue: Readonly<IEducationalProgram> = {};
