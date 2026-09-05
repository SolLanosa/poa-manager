import { useMutation } from "@tanstack/react-query";
import { AuthorizationRequestDTO, DecisionDTO } from "../types/types";
import server from "@/services/api";
export const useAuthorization = () => {
  const mutation = useMutation({
    mutationKey: ["authorizations"],
    mutationFn: async (body: AuthorizationRequestDTO): Promise<DecisionDTO> => {
      const res = await server.post("/authorizations/", body);
      return res.data;
    },
  });
  return { ...mutation, decision: mutation.data };
};
