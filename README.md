# ECOM Order Service
Orchestrates order lifecycle and distributed transactions.

## Architecture
- **Tech Stack:** Spring Boot, MySQL (Cloud SQL).
- **Communication:** Uses **Spring Cloud OpenFeign** to trigger stock deduction (Product Service), validate identity (User Service), and dispatch alerts (Notification Service).
- **Deployment:** Google Cloud Run.

## Project Navigation
| Service Name | Primary Database | Data Characteristic | Repository Link |
| --- | --- | --- | --- |
| **User Service** | PostgreSQL (Cloud SQL) | Structured user data | [ECOM-User-Service](https://github.com/CODERonak/ECOM-User-Service) |
| **Product Service** | MongoDB (Cloud Atlas) | Flexible schema | [ECOM-Product-Service](https://github.com/CODERonak/ECOM-Product-Service) |
| **Order Service** | MySQL (Cloud SQL) | Transactional nature | [ECOM-Order-Service](https://github.com/CODERonak/ECOM-Order-Service) |
| **Notification Service** | MongoDB (Cloud Atlas) | Notification history/logs | [ECOM-Notification-Service](https://github.com/CODERonak/ECOM-Notification-Service) |
| **API Gateway** | N/A | Request Routing | [ECOM-API-Gateway](https://github.com/CODERonak/ECOM-API-Gateway) |

## API Docs & Resources
- **[Swagger UI](https://ecom-order-service-934927433282.us-central1.run.app/swagger-ui/index.html#/)**
- **[Postman Collection](https://www.postman.com/flight-technologist-23919603/microservices/collection/lqxpccy/ecom?action=share&source=copy-link&creator=42910295)**
