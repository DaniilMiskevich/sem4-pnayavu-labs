export async function my_fetch<T>(input: RequestInfo | URL, init?: RequestInit) {
    const response = await fetch(input, init)
    if (!response.ok) throw new Error(`API error: ${response.status}`)
    return await response.json() as T
}
