import dayjs from 'dayjs';
import { IGroup } from 'app/shared/model/group.model';
import { IOrder } from 'app/shared/model/order.model';
import { IPersonalGrade } from 'app/shared/model/personal-grade.model';

export interface IStudent {
  id?: number;
  alphabetBookNumber?: number;
  firstName?: string;
  secondName?: string | null;
  lastName?: string;
  birthDate?: dayjs.Dayjs;
  group?: IGroup | null;
  orders?: IOrder[] | null;
  personalGrades?: IPersonalGrade[] | null;
}

export const defaultValue: Readonly<IStudent> = {};
