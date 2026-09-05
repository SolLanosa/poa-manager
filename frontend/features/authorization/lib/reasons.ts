type Observation = { title: string; message: string };

export const OBSERVATIONS: Record<string, Observation> = {
  OUT_OF_SCOPE: {
    title: "Out Of Scope",
    message: "Faculty is limited to determine goods/accounts",
  },
  NO_FACULTY: {
    title: "NO Faculty",
    message: "No Power of Attorney exists for this faculty",
  },
  APPROVED: {
    title: "Authorized",
    message: "Signing rules are satisfied",
  },
  NO_POA_IN_FORCE: {
    title: "No active Power of Attorney found",
    message: "No power of attorney is valid in the requested date",
  },
  AMOUNT_OUT_OF_BAND: {
    title: "Amount out of bands",
    message: "Faculty is ok, but not the amount",
  },
  INSUFFICIENT_SIGNATURES: {
    title: "Missing signers",
    message: "Signers are missing one of the valid combination",
  },
};
