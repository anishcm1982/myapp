NHIS Fraud Auditor Dashboard — MVP

A full-stack web application that loads NHIS healthcare claims, applies a fraud-likelihood heuristic, and provides a dashboard for auditors to review high-risk claims.

This MVP was built under an 8-hour rapid-prototyping constraint using AI/Large Language Models as required by the assignment.

🚀 1. Features
🔍 Fraud Detection Engine

Computes fraud score (0–100) using:

Amount Z-score

Provider volume normalization

Duplicate detection

Flags claims above threshold (default: 70)

🗂 CSV Upload

Upload NHIS claims in bulk:

Parses CSV

Stores records in MySQL

Runs fraud scoring pipeline

📊 React Dashboard

Display flagged claims

Adjustable fraud threshold slider

Mark claim as Fraud / Not Fraud / Needs Review

🧠 AI Tool Usage

Required AI prompts, tests, and architectural justification included in Part 2.

🏗 2. Architecture
+-----------------------------+
|        React Frontend       |
|  MUI Table + Axios + Slider |
+-------------+---------------+
              |
              v
+-------------+---------------+
|    Spring Boot Backend      |
| Controllers / Services      |
| FraudScoringEngine.java     |
+-------------+---------------+
              |
              v
+-------------+---------------+
|        MySQL Database       |
|   Claims, Scores, Reviews   |
+-----------------------------+

🛠 3. Technology Stack
Layer	Tech
Frontend	React 19, Material UI, Axios
Backend	Java 17, Spring Boot 3.x
Database	MySQL
Deployment	Local or AWS EKS (optional)
AI Usage	ChatGPT (code generation, debugging, architectural choices)
📥 4. Setup Instructions
Backend (Spring Boot)
1️⃣ Configure MySQL

Create database:

CREATE DATABASE nhis_fraud;

2️⃣ Update application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/nhis_fraud
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

3️⃣ Run Backend
mvn spring-boot:run


Backend will run on:
👉 http://localhost:8080

Frontend (React)
1️⃣ Install Dependencies
cd frontend
npm install

2️⃣ Start React Dev Server
npm start


Runs on:
👉 http://localhost:3000

Make sure backend CORS accepts frontend origin.

📄 5. CSV Format

Sample format supported by backend:

claim_id,patient_id,provider_id,date_of_service,claim_amount,diagnosis_code,procedure_code,service_type,is_emergency,patient_age,provider_specialty
CLM001,PAT001,PRV001,2024-01-12,24500.00,E11.9,99213,Outpatient,Yes,78,General Medicine

✔ Required Columns

claim_id

patient_id

provider_id

date_of_service

claim_amount

diagnosis_code

procedure_code

service_type

is_emergency

patient_age

provider_specialty

🧮 6. Fraud Scoring Heuristic

Located in:

src/main/java/.../FraudScoringEngine.java


Weights:

50% = Amount Z-score

30% = Provider volume normalization

20% = Duplicate detection

Fraud Score = 0 to 100

🌐 7. API Endpoints
POST /api/claims/upload

Upload CSV → process → score

GET /api/claims/flagged?threshold=70

Return high-risk claims

POST /api/claims/{id}/review?status=Fraud

Update auditor review status

📊 8. Dashboard Overview

React page: /src/pages/Dashboard.js

Shows:

Claim ID

Provider

Claim Amount

Fraud Score

Duplicate Flag

Review Actions

📸 9. Screenshots (Placeholders)
[ Dashboard Screen ]
[ CSV Upload Screen ]
[ Fraud Score Table ]


(Add your screenshots here)

🤖 10. Part 2 — AI Tool Fluency & Vibe Coding (MANDATED)
A. Top 5 Prompts (AI Prompt Journal)
Prompt 1 — Fraud Scoring Engine

"Give me a Java fraud-scoring engine that computes a weighted score using amount Z-score, provider volume normalization, and duplicate detection."

Used For: Generated the core scoring engine.

Prompt 2 — Spring Boot REST API

"Generate Spring Boot controller methods for uploading CSV, fetching flagged claims, and updating review status."

Used For: Created full REST layer.

Prompt 3 — React Dashboard (MUI Table & Slider)

"Create a React dashboard that shows flagged claims, with buttons to mark status and slider to adjust threshold."

Used For: Created UI foundation.

Prompt 4 — Fix ProviderNorm NaN

"ProviderNorm becomes NaN — fix min/max normalization safely."

Used For: Fixed divide-by-zero bug.

Prompt 5 — Generate Synthetic NHIS CSV Above Threshold

"Generate NHIS data that will produce fraud scores above 70."

Used For: Testing & validation.

B. AI-Generated Unit Test
Prompt

"Write a JUnit test to verify a high-amount claim produces a fraud score above 70."

Resulting Test Code
@Test
void testOutlierAmountProducesHighFraudScore() {
    FraudScoringEngine engine = new FraudScoringEngine();

    ClaimRecord normal = new ClaimRecord("C1", "P1", "PRV1", "2024-01-10", 1200);
    ClaimRecord outlier = new ClaimRecord("C2", "P2", "PRV1", "2024-01-11", 150000);

    List<ClaimRecord> claims = List.of(normal, outlier);
    engine.applyFraudScores(claims);

    assertTrue(outlier.getFraudScore() > 70);
}

C. AI-Assisted Architectural Choice
Prompt

"Compare MySQL vs SQLite for an NHIS fraud dashboard involving CSV ingestion and concurrent queries."

Decision Summary
Feature	MySQL	SQLite
Concurrency	✔ Great	❌ Limited
Bulk Uploads	✔ Fast	❌ Slows
Production Use	✔ Yes	❌ Not suitable
Integration with Spring	✔ Excellent	✔ Good
Scaling	✔ Horizontal/Vertical	❌ Not scalable
Final Decision

MySQL chosen due to concurrency, scalable ingestion, JVM/JPA compatibility, and future cloud deployment (EKS).

🛡 11. Validation & Edge Cases

Handles empty CSV

Handles duplicate detection

Avoids NaN provider normalization

Standardized date parsing

Scores range between 0–100

☁️ 12. Deployment Notes (Optional EKS)

If deployed to AWS EKS:

Backend → Dockerized Spring Boot

MySQL → RDS or Aurora

React → S3 + CloudFront

Ingress → ALB

Metrics → CloudWatch

📚 13. Future Enhancements

ML-based fraud prediction

Provider anomaly profiling

Role-based authentication

CSV upload history

Full audit logs

✅ 14. Conclusion

This MVP meets the assignment goals:

✔ CSV ingestion
✔ Fraud heuristic scoring
✔ Full dashboard
✔ Auditor review flow
✔ MySQL persistent storage
✔ AI usage documentation
