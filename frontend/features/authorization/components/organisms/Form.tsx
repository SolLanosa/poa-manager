"use client";
import { Field, FieldLabel } from "@/components/ui/field";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  FACULTY_ACTIONS,
  FACULTY_OBJECTS_CATEGORY,
} from "@/features/authorization/constants/FACULTY";
import { CompanyDTO, MemberDTO } from "@/features/authorization/types/types";
import { useState } from "react";
import Signers from "../molecules/Signers";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { DatePicker } from "@/components/common/atoms/DatePicker";
import { Skeleton } from "@/components/ui/skeleton";

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

interface FormProps {
  companies: CompanyDTO[] | undefined;
  isLoadingCompanies: boolean;
  companyId: number | null;
  members: MemberDTO[] | undefined;
  isPendingSubmission: boolean;
  isLoadingMembers: boolean;
  handleCompanyChange: (companyId: number | null) => void;
  onSubmit: (body: AuthorizationRequestDTO) => void;
  onClear: () => void;
}

export default function Form({
  companies,
  companyId,
  members,
  handleCompanyChange,
  onSubmit,
  isLoadingCompanies,
  isLoadingMembers,
  isPendingSubmission,
  onClear,
}: FormProps) {
  const [signers, setSigners] = useState<(MemberDTO | null)[]>([null]);
  const [action, setAction] = useState<string>("");
  const [object, setObject] = useState<string>("");
  const [particularObject, setParticularObject] = useState<string>("");
  const [amount, setAmount] = useState("");
  const [date, setDate] = useState(new Date());
  const isButtonDisabled =
    companyId === null ||
    signers.length === 0 ||
    action === "" ||
    object === "";

  const handleSubmit = () => {
    onSubmit({
      companyId: companyId ?? -1,
      signersId: signers.flatMap((s) => s?.id ?? []),
      action: action,
      object: object,
      itemRef: particularObject,
      amount: Number(amount),
      currency: "USD",
      date: date.toISOString(),
    });
  };

  const clearForm = () => {
    onClear();
    setSigners([null]);
    setAction("");
    setObject("");
    setParticularObject("");
    setAmount("");
    setDate(new Date());
  };

  return (
    <div className="border-2 p-5 m-10 rounded-md">
      {isLoadingCompanies ? (
        <div className="flex flex-col gap-3">
          <Skeleton className="h-4 w-20" />
          <Skeleton className="h-8 w-full" />
        </div>
      ) : (
        <Field className="p-3">
          <FieldLabel htmlFor="company">Company</FieldLabel>
          <Select
            items={companies?.map((company) => ({
              label: company.name,
              value: company.id,
            }))}
            value={companyId}
            onValueChange={handleCompanyChange}
          >
            <SelectTrigger>
              <SelectValue placeholder="Company" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value={null}>Select</SelectItem>
                {companies?.map((company) => (
                  <SelectItem key={company.id} value={company.id}>
                    {company.name}
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
        </Field>
      )}

      {isLoadingMembers ? (
        <div className="flex flex-col gap-3">
          <Skeleton className="h-4 w-20" />
          <Skeleton className="h-8 w-full" />
        </div>
      ) : (
        <Signers
          members={members!}
          signers={signers}
          onChangeSigners={setSigners}
          disabled={!companyId}
        />
      )}

      <div className="flex items-center">
        <Field className="p-3 w-[33%]">
          <FieldLabel htmlFor="action">Action</FieldLabel>
          <Select
            items={FACULTY_ACTIONS.map((action) => ({
              label: action.label,
              value: action.value,
            }))}
            value={action}
            onValueChange={(e) => setAction(e ?? "")}
          >
            <SelectTrigger>
              <SelectValue placeholder="Action" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value={null}>Select</SelectItem>
                {FACULTY_ACTIONS.map((action) => (
                  <SelectItem key={action.value} value={action.value}>
                    {action.label}
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
        </Field>

        <Field className="p-3 w-[33%]">
          <FieldLabel htmlFor="object">Object</FieldLabel>
          <Select
            items={FACULTY_OBJECTS_CATEGORY.map((object) => ({
              label: object.label,
              value: object.value,
            }))}
            value={object}
            onValueChange={(e) => setObject(e ?? "")}
          >
            <SelectTrigger>
              <SelectValue placeholder="Object" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectItem value={null}>Select</SelectItem>
                {FACULTY_OBJECTS_CATEGORY.map((object) => (
                  <SelectItem key={object.value} value={object.value}>
                    {object.label}
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
        </Field>

        <Field className="p-3 w-[33%]">
          <FieldLabel htmlFor="particularObject">Particular Object</FieldLabel>
          <Input
            placeholder="Particular Object"
            value={particularObject}
            onChange={(e) => setParticularObject(e.target.value)}
          />
        </Field>
      </div>
      <Field className="p-3">
        <FieldLabel htmlFor="amount">Amount</FieldLabel>
        <Input
          className="w-52"
          inputMode="decimal"
          placeholder="Amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
      </Field>
      <Field className="p-3">
        <FieldLabel htmlFor="date">Date</FieldLabel>
        <DatePicker date={date} setDate={setDate} />
      </Field>
      <div className="p-3 flex justify-between">
        <Button variant={"secondary"} onClick={clearForm}>
          Clear
        </Button>
        <Button
          onClick={handleSubmit}
          disabled={isPendingSubmission || isButtonDisabled}
        >
          Verify Authorization
        </Button>
      </div>
    </div>
  );
}
