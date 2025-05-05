import Spark from "../models/Spark";

type SparkRquestDto = { name: string, spectre_names: string[] }
interface SparksApi {
    create(space_id: number, spark: SparkRquestDto): Promise<Spark>
    get(id: number): Promise<Spark>
    search(name?: string, spectres?: string): Promise<Spark[]>
    update(id: number, spark: SparkRquestDto): Promise<Spark>
    delete(id: number): Promise<void>
}

export default SparksApi
export type { SparkRquestDto }
