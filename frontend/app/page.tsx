import AuthorizationManager from "@/features/authorization/components/views/AutorizationManager";
import Providers from "@/providers/ReactQueryProvider";

export default function Home() {
  return (
    <div className="w-full">
      <Providers>
        <AuthorizationManager />
      </Providers>
    </div>
  );
}
