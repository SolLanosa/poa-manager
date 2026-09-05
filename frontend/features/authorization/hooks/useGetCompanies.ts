import server from "@/services/api";
import { useQuery } from "@tanstack/react-query";
import { CompanyDTO } from "../types/types";

export const useGetCompanies = () => {
  const response = useQuery<{ companies: CompanyDTO[]; count: number }>({
    queryKey: ["getCompanies"],
    queryFn: async () => {
      const res = await server.get("/companies/");
      return res.data;
    },
    enabled: true,
    refetchOnWindowFocus: false,
  });

  return { ...response, companies: response.data };
};
