import { Field, FieldLabel } from "@/components/ui/field";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { MemberDTO } from "@/features/authorization/types/types";

import { CircleMinus, CirclePlus } from "lucide-react";

export default function Signers({
  members,
  signers,
  disabled,
  onChangeSigners,
}: {
  members: MemberDTO[];
  signers: (MemberDTO | null)[];
  disabled: boolean;
  onChangeSigners: (next: (MemberDTO | null)[]) => void;
}) {
  const selected = new Set(
    signers
      .filter((signer): signer is MemberDTO => signer !== null)
      .map((s) => s.id),
  );

  return (
    <div className="flex flex-col">
      <Field className="p-3 ">
        <div className="flex">
          <FieldLabel>Signer</FieldLabel>

          <button
            type="button"
            disabled={disabled}
            className="flex items-center text-sm"
            onClick={() => onChangeSigners([...signers, null])}
          >
            <CirclePlus size={16} className="ml-3" />
          </button>
        </div>

        {signers.map((signer, i) => {
          const availableMembers = members?.filter(
            (member) => member.id === signer?.id || !selected.has(member?.id),
          );

          return (
            <div key={signer?.nationalId ?? i} className="flex items-center">
              <div>
                <Select
                  disabled={disabled}
                  id={String(signer?.nationalId) + "select"}
                  value={
                    signer !== null
                      ? `${signer.firstName} ${signer.lastName} -
                          ${signer.nationalId} `
                      : ""
                  }
                  onValueChange={(value) => {
                    const signerId = value ? Number(value) : null;
                    const newWW = signers.map((currentSigner, index) =>
                      index === i
                        ? (members.find((m) => m.id === signerId) ?? null)
                        : currentSigner,
                    );

                    onChangeSigners(newWW);
                  }}
                >
                  <SelectTrigger className="w-[400px]">
                    <SelectValue placeholder="Signer" />
                  </SelectTrigger>

                  <SelectContent className="w-[400px]">
                    <SelectGroup>
                      <SelectItem value={null}>Select</SelectItem>
                      {availableMembers?.map((member) => (
                        <SelectItem key={member.id} value={String(member.id)}>
                          {member.firstName} {member.lastName} -{" "}
                          {member.nationalId}
                        </SelectItem>
                      ))}
                    </SelectGroup>
                  </SelectContent>
                </Select>
              </div>

              <div>
                {
                  <button
                    type="button"
                    className="text-sm"
                    disabled={signers.length === 1}
                    onClick={() =>
                      onChangeSigners(signers.filter((_, index) => index !== i))
                    }
                  >
                    <CircleMinus size={16} className="ml-3" />
                  </button>
                }
              </div>
            </div>
          );
        })}
      </Field>
    </div>
  );
}
