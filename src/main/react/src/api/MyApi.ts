export class ApiError extends Error {
    constructor(error: Error) {
        super(error.message)
        this.name = "API Error"
        Object.setPrototypeOf(this, ApiError.prototype);
    }
}

export async function my_fetch<T>(input: RequestInfo | URL, init?: RequestInit) {
    try {
        const response = await fetch(input, init)
        if (!response.ok) throw new Error(`API error: ${response.status}`)
        return await response.json() as T
    } catch (e) {
        if (e instanceof ApiError) throw e
        if (e instanceof Error) throw new ApiError(e)
        else throw e
    }
}
