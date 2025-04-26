import { createContext, JSX, use } from "react"

const DI = createContext({} as any);

export const useDI = () => use(DI)

export const DIProvider = ({ children, deps }: {
    children: JSX.Element,
    deps: any
}) => <DI.Provider value={deps}>
        {children}
    </DI.Provider>
