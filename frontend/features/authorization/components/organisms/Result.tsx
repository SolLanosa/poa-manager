import { DecisionDTO } from "@/features/authorization/types/types";

import Observations from "../molecules/Observations";
import Source from "../molecules/Source";

interface DecisionResultProps {
  decision: DecisionDTO;
}

export default function Result({ decision }: DecisionResultProps) {
  return (
    <div
      className={`w-[50%] border-2 p-5 m-10 rounded-md ${decision.approved ? "bg-green-100 border-green-800" : "bg-red-200 border-red-800"}`}
    >
      <p
        className={`text-lg font-bold uppercase ${decision.approved ? "text-green-800" : "text-red-800"}`}
      >
        {decision.approved ? "APPROVED" : "REJECTED"}
      </p>

      {decision.observations.length > 0 && (
        <Observations observations={decision.observations} />
      )}
      <Source decision={decision} />
    </div>
  );
}
