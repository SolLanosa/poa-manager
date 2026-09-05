export type CompanyDTO = {
  id: number;
  name: string;
  type: string;
};

export type MemberDTO = {
  id: number;
  firstName: string;
  lastName: string;
  nationalId: string;
};

export type ObservationDTO = {
  code: string;
  message: string;
};

export type DecisionDTO = {
  approved: boolean;
  powerOfAttorneyId: number | null;
  facultyId: number | null;
  signingRuleId: number | null;
  observations: ObservationDTO[];
};

export type AuthorizationRequestDTO = {
  companyId: number;
  signersId: number[];
  action: string;
  object: string;
  itemRef: string | null;
  amount: number | null;
  currency: string | null;
  date: string | null;
};
