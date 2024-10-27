## InsightBC Platform
InsightBC is a specialized online platform for adult vocational education in British Columbia, Canada. This project includes content management, media asset management, and an order payment system tailored for large-scale online learning platforms.

### Features
1. Content Management and Media Asset Management
- Designed and developed modules for efficient content organization and handling.
- Integrated Minio to handle large file uploads within the media asset management system.
- Implemented XXL-JOB as a distributed scheduling component to manage and process video content efficiently.
2. Static Resource Deployment and Load Balancing
- Used Nginx for serving static assets, optimizing load distribution, and managing cache to enhance platform performance and user experience.
3. Page Staticization
- Employed Freemarker to create static versions of high-traffic pages like the portal homepage and course detail pages, minimizing server load and improving page load speed.
4. Authentication and Security
- Integrated Spring Security, OAuth2.0, and JWT to provide distributed and unified authentication, ensuring robust access control and session management for all users.
5. Distributed Transaction Management
- Overcame the challenges of distributed transactions by employing a local message table and task scheduling.
- This approach, coupled with effective task scheduling, maintains data consistency across distributed services.
### Tech Stack
- Languages: Java
- Frameworks: Spring Boot, Spring Cloud
- Database: MySQL, Redis
- Caching and Search: Elasticsearch, Redis
- Scheduling: XXL-JOB
- Static Resource Deployment: Nginx
- Authentication: Spring Security, OAuth2.0, JWT
- Page Templating: Freemarker
