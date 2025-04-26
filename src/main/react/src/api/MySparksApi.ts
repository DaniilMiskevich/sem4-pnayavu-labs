import Spark from "../models/Spark";
import { my_fetch } from "./MyApi";
import SparksApi, { SparkRquestDto } from "./SparksApi";

class MySparksApi implements SparksApi {
    constructor(base_url: string) { this.base_uri = base_url }

    async create(space_id: number, spark: SparkRquestDto): Promise<Spark> {
        return my_fetch<Spark>(`${this.base_uri}?space_id=${space_id}`, {
            method: "POST", body: JSON.stringify(spark)
        })
    }
    async get(id: number): Promise<Spark> {
        return my_fetch<Spark>(`${this.base_uri}/${id}`)
    }
    async search(name?: string, spectres?: string): Promise<Spark[]> {
        const params = new URLSearchParams({
            ...name && { "name": name },
            ...spectres && { "spectres": spectres },
        });
        return my_fetch<Spark[]>(`${this.base_uri}?${params}`)
    }
    async update(id: number, spark: SparkRquestDto): Promise<Spark> {
        return my_fetch<Spark>(`${this.base_uri}/${id}`, { method: "PATCH", body: JSON.stringify(spark) })
    }
    async delete(id: number): Promise<void> {
        return my_fetch<void>(`${this.base_uri}/${id}`, { method: "DELETE" })
    }

    private readonly base_uri: string
}

export default MySparksApi

