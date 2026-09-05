import server from "@/services/api";
import { useQuery } from "@tanstack/react-query";
import { MemberDTO } from "../types/types";

export const useGetMembers = (id: number) => {
  const response = useQuery<{ members: MemberDTO[]; count: number }>({
    queryKey: ["getMembers", id],
    queryFn: async () => {
      const res = await server.get(`/companies/${id}/members`);
      return res.data;
    },
    enabled: !!id,
    refetchOnWindowFocus: false,
  });

  return { ...response, members: response.data };
};
