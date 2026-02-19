# Object Storage Service (S3 Clone)

A high-performance, secure, and scalable **Object Storage REST API** built with **Java 25** and **Spring Boot 4**. This system mimics the core functionality of cloud storage services like AWS S3 or Google Cloud Storage, supporting bucket management, file streaming, and secure metadata handling.

---

## Key Features

**Bucket Management**
Create, list, and delete buckets with globally unique names following standard cloud architecture patterns.

**File Operations**
Stream-based file upload and download with low memory footprint using `InputStream` for efficient data handling.

**Security First**
- **Stateless Authentication:** JSON Web Tokens (JWT) with Spring Security
- **Path Traversal Protection:** Physical files are stored using UUIDs to prevent malicious file execution or overwriting
- **Password Encryption:** BCrypt hashing for user credentials

**Clean Architecture**
Strict separation of concerns following the Controller → Service → Repository → Storage Backend pattern.

**Database**
PostgreSQL for robust metadata management, containerized via Docker for easy deployment.

---

## Technology Stack

| Component | Technology |
|-----------|------------|
| **Language** | Java 25 (OpenJDK) |
| **Framework** | Spring Boot 4 |
| **Security** | Spring Security, JJWT (0.13.0) |
| **Database** | PostgreSQL 16 |
| **DevOps** | Docker & Docker Compose |
| **Build Tool** | Maven |
| **Utilities** | Lombok, Jakarta Validation |

---

## Architecture & Design

### Storage Strategy: The Flat Design

The system decouples the **Virtual Filename** (what the user sees) from the **Physical Filename** (what is stored on disk).

**Database Layer**
Stores metadata including the user-friendly filename (`resume.pdf`), Owner ID, File Size, and MIME Type.

**Disk Layer**
Stores actual content as a **UUID** (`550e8400-e29b-41d4...`), ensuring files are named safely.

**Why This Approach?**
This architecture prevents **Directory Traversal Attacks** where malicious users attempt to upload files with paths like `../../hack.sh`. Even if a malicious filename is sent, it is sanitized and stored safely as a UUID, making exploitation impossible.

### Database Schema

The system maintains a relational structure with the following relationships:

- **Users** `1 -- N` **Buckets**
- **Buckets** `1 -- N` **FileMetadata**

**Global Namespace:** Bucket names are unique across the entire system to support potential DNS-style routing in the future.

---

## Setup & Installation

### Prerequisites

- Java 25 SDK
- Docker & Docker Compose
- Maven

### Step 1: Clone the Repository

```bash
git clone https://github.com/abdkhaleel/objectstorage.git
cd objectstorage
```

### Step 2: Start the Database (Docker)

This project uses `docker-compose.yml` to spin up a PostgreSQL instance.

```bash
docker compose up -d
```

### Step 3: Configure Application

Open `src/main/resources/application.properties` and ensure the settings match your environment:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/objectstorage
spring.datasource.username=khaleel
spring.datasource.password=khaleel

# JWT Configuration (Change this in production!)
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000

# File Storage Root
app.storage.location=storage-data
```

### Step 4: Run the Application

```bash
mvn spring-boot:run
```

---

## API Endpoints

You can test these endpoints using **Postman** or **cURL**.

### Authentication Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `POST` | `/api/auth/register` | Register a new user | `{"username": "user", "password": "123"}` |
| `POST` | `/api/auth/login` | Login & Get JWT Token | `{"username": "user", "password": "123"}` |

**Important Note:** All subsequent requests must include the authorization header:
```
Authorization: Bearer <your_token>
```

### Bucket Management Endpoints

| Method | Endpoint | Description | Body Type |
|--------|----------|-------------|-----------|
| `POST` | `/api/buckets` | Create a new unique bucket | `{"bucketName": "anyName"}` |
| `GET` | `/api/buckets` | List all buckets owned by you | N/A |
| `DELETE` | `/api/buckets/{bucket}` | Delete a bucket (must be empty) | N/A |

### File Operations Endpoints

| Method | Endpoint | Description | Body Type |
|--------|----------|-------------|-----------|
| `POST` | `/api/buckets/{bucket}/files` | Upload a file | `form-data` (Key: `file`) |
| `GET` | `/api/buckets/{bucket}/files/{filename}` | Download a file | N/A |
| `GET` | `/api/buckets/{bucket}/files` | List files in bucket | N/A |
| `DELETE` | `/api/buckets/{bucket}/files/{filename}` | Delete a file | N/A |

---

## Testing

### Default Admin User

On the first run, the `DataSeeder` automatically creates an admin user for testing:

- **Username:** `admin`
- **Password:** `password`

Use these credentials to test the API endpoints.

### Running Unit Tests

Run the test suite to verify the application logic:

```bash
mvn test
```

---

## Project Structure

```
src/main/java/com/khaleel/objectstorage
├── config/           # Security & App Configuration
├── controller/       # REST API Layer (Handles HTTP Requests)
├── dto/              # Data Transfer Objects
├── exception/        # Global Exception Handling
├── model/            # JPA Entities (User, Bucket, FileMetadata)
├── repository/       # Database Access (Spring Data JPA)
├── security/         # JWT Logic, Filters, UserDetails Wrapper
├── service/          # Business Logic
└── storage/          # Low-level File System Operations
```

---

## Contributing

We welcome contributions to improve this project. Feel free to fork this repository and submit Pull Requests.

**Future Roadmap**
1. Implementing Global Search across buckets
2. Adding File Versioning support
3. Enhanced access control and sharing features

---

## License

This project is licensed under the [MIT License]([https://github.com/abdkhaleel/objectstorage/blob/master/LICENSE.md](https://github.com/abdkhaleel/objectstorage?tab=MIT-1-ov-file)).
