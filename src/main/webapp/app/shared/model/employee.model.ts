import dayjs from 'dayjs';
import { IAcademicSubject } from 'app/shared/model/academic-subject.model';

export interface IEmployee {
  id?: number;
  firstName?: string;
  secondName?: string | null;
  lastName?: string;
  birthDate?: dayjs.Dayjs;
  jobTitle?: string;
  academicSubjects?: IAcademicSubject[] | null;
}

export const defaultValue: Readonly<IEmployee> = {};
