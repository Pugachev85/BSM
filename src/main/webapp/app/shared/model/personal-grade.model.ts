import { IAcademicSubject } from 'app/shared/model/academic-subject.model';
import { IStudent } from 'app/shared/model/student.model';

export interface IPersonalGrade {
  id?: number;
  grade?: number;
  academicSubject?: IAcademicSubject | null;
  students?: IStudent[] | null;
}

export const defaultValue: Readonly<IPersonalGrade> = {};
