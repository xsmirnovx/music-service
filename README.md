# How to Run
- `./mvnw spring-boot:run`
- `curl -X GET "http://localhost:8081/musify/music-artist/details/71fdb598-1967-435b-ab25-f4daad5e576b"`

# About
- Lombok to eliminate boilerplate code
- FeignClient as http client, since I'm mostly used to it
- Spring `@Async` to execute async requests to coverart and wiki
- Spring Cloud CircuitBreaker to address 3rd party APIs failures. I've used default configuration to save time
- Tests code is a bit of a mess, and the coverage is poor, but I was running out of time to refactor and add new tests

# Test MBIDs:
- 66fc5bf8-daa4-4241-b378-9bc9077939d2
- fa31e7d4-a03a-4110-98fc-f8826e95ec25
