export interface Compte {
  id: string;
  type: string;
  balance: number;
  createdAt: string;
  status: string | null;
  overDraft?: number;
  interestRate?: number;
}

export interface AccountDetails {
  accountId: string;
  balance: number;
  currentPage: number;
  totalPages: number;
  pageSize: number;
  accountOperationDTOS: OperationAccount[];
}

export interface OperationAccount {
  id: number;
  operationDate: Date;
  amount: number;
  type: string;
  description: string;
}
