import { DecisionDTO } from "@/features/authorization/types/types";

export default function Source({ decision }: { decision: DecisionDTO }) {
  if (decision.powerOfAttorneyId === null) return null;

  return (
    <div className="flex flex-col">
      <span className="font-bold text-lg">Power of Attorney information:</span>
      <ul className="flex flex-col list-disc ml-5">
        <li>
          <span className="font-bold">Power:</span> {decision.powerOfAttorneyId}
        </li>
        <li>
          <span className="font-bold">Faculty:</span> {decision.facultyId}
        </li>
        <li>
          <span className="font-bold">Signature rule: </span>
          {decision.signingRuleId}
        </li>
      </ul>
    </div>
  );
}
