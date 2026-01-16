# Optimal Truck Load Planner

A stateless REST API that selects the optimal combination of shipments for a truck,
maximizing carrier payout while respecting weight, volume, route, hazmat, and
time-window constraints.

---

## Features

- Maximizes total payout (in cents, no floating point)
- Enforces truck weight & volume limits
- Route compatibility (same origin → destination)
- Hazmat isolation (hazmat and non-hazmat not mixed)
- Simplified time-window validation (pickup ≤ delivery)
- Stateless, in-memory only
- Optimized using recursive backtracking with pruning
- Designed for up to 22 orders

---

## Tech Stack

- Java 17
- Spring Boot
- Maven
- Docker & Docker Compose
- JUnit

---

## How to Run

```bash
git clone https://github.com/jatin9958/optimal_truck_load_planner.git
cd optimal_truck_load_planner
docker compose up --build
