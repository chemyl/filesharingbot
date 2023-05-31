
Telegram bot. Sharing content by link.

- [User workflow](#User workflow)
- [Work plan](#Work Plan)

### Features
- Email registration
- Uploading Document / Photo content
- Link for downloading

[//]: # (      | DataBase | Broker  |)

[//]: # (      |---------| --------  |)

[//]: # (      | PostgreSQL | RabbitMQ|)

### User workflow ###
1. Open bot
2. Registration (with email)
3. Ending up the registration by email link
4. Uploading content (photo/video) to telegram bot
5. Get link to downloading content

### Work Plan 
1. Create Dispatcher microservice. =>> Validation and processing message for the broker.
2. Implement message broker. =>> RabbitMQ
3. Implement scalable Node-microservice. =>> Processing messages from broker.
4. RESTFul api microservice. User http request for registration, downloading content..
5. Implement mail microservice for user registration 
6. Perform high-load testing with JMeter (try to 10000 rps)
7. VPS (24/7) + need white ip address (for Web-hooks)
