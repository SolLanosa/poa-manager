"use client";

import { useState } from "react";
import { useGetCompanies } from "../../hooks/useGetCompanies";
import { useGetMembers } from "../../hooks/useGetMembers";
import Form from "../organisms/Form";
import { useAuthorization } from "../../hooks/useAuthorization";
import Result from "../organisms/Result";

export default function AuthorizationManager() {
  const [companyId, setCompanyId] = useState<number | null>(null);
  const { companies, isLoading } = useGetCompanies();

  const { members, isLoading: isLoadingMembers } = useGetMembers(companyId!);

  const handleCompanyChange = (newVal: number | null) => {
    setCompanyId(newVal);
    reset();
  };

  const { mutate, decision, isPending, reset } = useAuthorization();
  const onClear = () => {
    setCompanyId(null);
    reset();
  };

  return (
    <div className="w-full m-auto flex items-center justify-center h-full flex-col">
      <Form
        companies={companies?.companies}
        isLoadingCompanies={isLoading}
        companyId={companyId}
        handleCompanyChange={handleCompanyChange}
        members={members?.members}
        onSubmit={mutate}
        isPendingSubmission={isPending}
        isLoadingMembers={isLoadingMembers}
        onClear={onClear}
      />

      {isPending && <span>Loading...</span>}
      {decision && <Result decision={decision} />}
    </div>
  );
}
