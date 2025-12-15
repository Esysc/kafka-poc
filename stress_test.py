"""
Stress test script to send a high volume of HTTP POST requests to the claims API.
This is intended to be run against an instance of the claims service running locally.
The number of concurrent connections can be adjusted by changing the CONCURRENCY variable.
"""
import asyncio, aiohttp, time

URL = "http://localhost:8080/api/claims"
CLAIMS = 100000
CONCURRENCY = 200

# Example payload based on Claim.avsc and Claim.java structure
def build_claim(n: int) -> dict[str, object]:
    """Builds a claim dictionary from the given integer."""
    return {
        "id": f"claim-{n}",
        "patientId": f"p{n%100}",
        "amount": 1500.0,
        "createdAt": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime()),
        "diagnosis": "test-diagnosis",
        "providerId": f"provider-{n%10}",
        "status": "NEW"
    }

sem = asyncio.Semaphore(CONCURRENCY)

async def worker(session: aiohttp.ClientSession, n: int):
    """Worker function that sends a single request."""
    payload = build_claim(n)
    async with sem:
        async with session.post(URL, json=payload) as r:
                response_text = await r.text()
                print(f"Request: {payload}\nResponse: {r.status} {response_text}\n")

async def main():
    """Main function to run the stress test."""
    start = time.time()
    async with aiohttp.ClientSession() as session:
        tasks = [worker(session, i) for i in range(CLAIMS)]
        await asyncio.gather(*tasks)
    print("Duration:", time.time() - start, "seconds")

if __name__ == "__main__":
    asyncio.run(main())
