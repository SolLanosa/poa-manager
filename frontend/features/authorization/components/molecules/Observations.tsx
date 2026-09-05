import { OBSERVATIONS } from "@/features/authorization/lib/reasons";
import { ObservationDTO } from "@/features/authorization/types/types";

export default function Observations({
  observations,
}: {
  observations: ObservationDTO[];
}) {
  return (
    <div>
      <ul>
        {observations.map((observation, index) => (
          <li key={`${observation.code}-${index}`} className="flex flex-col">
            <span className="whitespace-pre-wrap">
              {OBSERVATIONS[observation.code]?.title ?? observation.code}
            </span>
            <span className="whitespace-pre-wrap">{observation.message}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
